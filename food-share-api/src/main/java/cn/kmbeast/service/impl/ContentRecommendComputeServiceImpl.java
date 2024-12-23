package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.ContentFeatureMapper;
import cn.kmbeast.mapper.ContentSimilarityMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.entity.ContentFeature;
import cn.kmbeast.pojo.entity.ContentSimilarity;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.ContentRecommendComputeService;
import cn.kmbeast.utils.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容推荐计算服务实现类
 */
@Service
@Slf4j
public class ContentRecommendComputeServiceImpl implements ContentRecommendComputeService {
    @Autowired
    private GourmetMapper gourmetMapper;
    
    @Autowired
    private ContentFeatureMapper contentFeatureMapper;
    
    @Autowired
    private ContentSimilarityMapper contentSimilarityMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // 特征权重配置
    private static final double CATEGORY_WEIGHT = 2.0;    // 分类特征权重
    private static final double TITLE_WEIGHT = 1.5;      // 标题特征权重
    private static final double CONTENT_WEIGHT = 0.6;    // 内容特征权重
    private static final double TAG_WEIGHT = 0.7;        // 标签特征权重（如果有）
    
    // 特征数量配置
    private static final int TITLE_FEATURES_LIMIT = 10;    // 标题特征数量
    private static final int CONTENT_FEATURES_LIMIT = 50; // 内容特征数量
    
    /**
     * 压缩特征向量
     * 1. 移除接近0的特征
     * 2. 使用更紧凑的键名
     */
    private Map<String, Double> compressFeatures(Map<String, Double> features) {
        Map<String, Double> compressed = new HashMap<>();
        features.forEach((key, value) -> {
            // 移除接近0的特征
            if (Math.abs(value) > 0.01) {
                // 压缩键名
                String compressedKey = key
                    .replace("category_", "c_")
                    .replace("title_", "t_")
                    .replace("content_", "n_");
                compressed.put(compressedKey, value);
            }
        });
        return compressed;
    }

    @Override
    @Transactional
    public boolean computeAndSaveFeatures(Integer gourmetId) {
        try {
            // 1. 获取美食信息
            GourmetVO gourmet = gourmetMapper.queryById(gourmetId);
            System.out.println("gourmet:" + gourmet);
            if (gourmet == null) {
                log.warn("Gourmet not found with id: {}", gourmetId);
                return false;
            }
            
            // 2. 提取特征
            Map<String, Double> features = new HashMap<>();
            
            // 2.1 分类特征
            if (StringUtils.hasText(gourmet.getCategoryName())) {
                // 主分类特征
                features.put("category_" + gourmet.getCategoryName(), CATEGORY_WEIGHT);
                // 分类词特征（分词后的每个词也作为特征）
                Map<String, Integer> categoryTerms = TextUtils.extractTerms(gourmet.getCategoryName());
                categoryTerms.entrySet().stream()
                    .filter(e -> !TextUtils.isStopWord(e.getKey()))
                    .forEach(e -> features.put(
                        "category_term_" + e.getKey(),
                        e.getValue() * CATEGORY_WEIGHT * 0.5  // 分类词特征权重为主分类的一半
                    ));
            }
            
            // 2.2 标题特征
            if (StringUtils.hasText(gourmet.getTitle())) {
                try {
                    Map<String, Integer> titleTerms = TextUtils.extractTerms(gourmet.getTitle());
                    System.out.println("title_terms:" + titleTerms);
                    
                    // 单个词特征
                    titleTerms.entrySet().stream()
                        .filter(e -> !TextUtils.isStopWord(e.getKey()))
                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                        .limit(TITLE_FEATURES_LIMIT)
                        .forEach(e -> features.put(
                            "title_" + e.getKey(), 
                            e.getValue() * TITLE_WEIGHT
                        ));
                        
                    // 词组合特征（相邻词的组合）
                    List<String> titleWords = new ArrayList<>(titleTerms.keySet());
                    for (int i = 0; i < titleWords.size() - 1; i++) {
                        String word1 = titleWords.get(i);
                        String word2 = titleWords.get(i + 1);
                        if (!TextUtils.isStopWord(word1) && !TextUtils.isStopWord(word2)) {
                            features.put(
                                "title_phrase_" + word1 + "_" + word2,
                                TITLE_WEIGHT * 0.8  // 词组特征权重稍低
                            );
                        }
                    }
                } catch (Exception e) {
                    log.warn("Failed to extract title features for gourmet: {}", gourmetId, e);
                }
            }
            
            // 2.3 内容特征
            if (StringUtils.hasText(gourmet.getContent())) {
                try {
                    Map<String, Integer> contentTerms = TextUtils.extractTerms(gourmet.getContent());
                    System.out.println("content_terms:" + contentTerms);
                    contentTerms.entrySet().stream()
                        .filter(e -> !TextUtils.isStopWord(e.getKey()))
                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                        .limit(CONTENT_FEATURES_LIMIT)
                        .forEach(e -> features.put(
                            "content_" + e.getKey(),
                            e.getValue() * CONTENT_WEIGHT
                        ));
                } catch (Exception e) {
                    log.warn("Failed to extract content features for gourmet: {}", gourmetId, e);
                }
            }
            
            // 3. 确保特征不为空
            if (features.isEmpty()) {
                log.warn("No features extracted for gourmet: {}, using default feature", gourmetId);
                features.put("default", 1.0);
            }
            
            // 4. L2归一化
            double l2Norm = Math.sqrt(features.values().stream()
                .mapToDouble(v -> v * v)
                .sum());
            if (l2Norm > 0) {
                features.replaceAll((k, v) -> v / l2Norm);
            }
            
            // 5. 保存特征
            LocalDateTime now = LocalDateTime.now();
            ContentFeature feature = new ContentFeature();
            feature.setGourmetId(gourmetId);
            feature.setFeatureVector(objectMapper.writeValueAsString(features));
            feature.setCreateTime(now);
            feature.setUpdateTime(now);
            
            contentFeatureMapper.insertOrUpdate(feature);
            log.info("Successfully saved features for gourmet: {}", gourmetId);
            return true;
        } catch (Exception e) {
            log.error("Failed to compute and save features for gourmet: {}", gourmetId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public void updateRecommendations(Integer gourmetId) {
        log.info("Updating recommendations for gourmet: {}", gourmetId);
        
        try {
            // 1. 验证源美食是否存在
            GourmetVO sourceGourmet = gourmetMapper.queryById(gourmetId);
            if (sourceGourmet == null) {
                log.error("Source gourmet not found: {}", gourmetId);
                return;
            }
            
            // 2. 计算源美食特征
            boolean success = computeAndSaveFeatures(gourmetId);
            if (!success) {
                log.error("Failed to compute features for source gourmet: {}", gourmetId);
                return;
            }
            
            // 3. 获取所有其他美食ID
            List<Integer> allIds = gourmetMapper.getAllIdsExcept(gourmetId);
            if (allIds.isEmpty()) {
                log.info("No other gourmet items found");
                return;
            }
            
            // 4. 为每个目标美食计算特征和相似度
            for (Integer targetId : allIds) {
                try {
                    // 4.1 验证目标美食
                    GourmetVO targetGourmet = gourmetMapper.queryById(targetId);
                    if (targetGourmet == null) {
                        continue;
                    }
                    
                    // 4.2 计算目标美食特征
                    if (computeAndSaveFeatures(targetId)) {
                        // 4.3 计算相似度
                        computeAndSaveSimilarity(gourmetId, targetId);
                    }
                } catch (Exception e) {
                    log.error("Failed to process similarity for {} -> {}", gourmetId, targetId, e);
                }
            }
            
            log.info("Successfully updated recommendations for gourmet: {}", gourmetId);
        } catch (Exception e) {
            log.error("Failed to update recommendations for gourmet: {}", gourmetId, e);
        }
    }

    @Override
    @Transactional
    public BigDecimal computeAndSaveSimilarity(Integer sourceId, Integer targetId) {

        try {
            // 1. 检查是否已存在相似度记录
            ContentSimilarity existingSimilarity = contentSimilarityMapper.selectBySourceAndTargetId(sourceId, targetId);
            if (existingSimilarity != null && !isFeatureExpired(contentFeatureMapper.selectByGourmetId(sourceId))
                && !isFeatureExpired(contentFeatureMapper.selectByGourmetId(targetId))) {
                return existingSimilarity.getSimilarity();
            }
            
            // 2. 获取特征向量
            ContentFeature sourceFeature = contentFeatureMapper.selectByGourmetId(sourceId);

            ContentFeature targetFeature = contentFeatureMapper.selectByGourmetId(targetId);

            
            if (sourceFeature == null || targetFeature == null) {
                log.warn("Feature not found for gourmet: {} or {}", sourceId, targetId);
                // 尝试重新计算特征
                if (sourceFeature == null) {
                    boolean success = computeAndSaveFeatures(sourceId);
                    if (success) {
                        sourceFeature = contentFeatureMapper.selectByGourmetId(sourceId);
                    }
                }
                if (targetFeature == null) {
                    boolean success = computeAndSaveFeatures(targetId);
                    if (success) {
                        targetFeature = contentFeatureMapper.selectByGourmetId(targetId);
                    }
                }
                
                // 如果仍然没有特征，返回零相似度
                if (sourceFeature == null || targetFeature == null) {
                    log.error("Failed to compute features for gourmet: {} or {}", sourceId, targetId);
                    return BigDecimal.ZERO;
                }
            }
            
            // 3. 解析特征向量
            Map<String, Double> sourceFeatures = objectMapper.readValue(
                sourceFeature.getFeatureVector(),
                new TypeReference<Map<String, Double>>() {}
            );
            Map<String, Double> targetFeatures = objectMapper.readValue(
                targetFeature.getFeatureVector(),
                new TypeReference<Map<String, Double>>() {}
            );
            
            // 4. 计算余弦相似度
            double similarity = calculateCosineSimilarity(sourceFeatures, targetFeatures);
            
            // 5. 保存相似度
            ContentSimilarity similarityEntity = new ContentSimilarity();
            similarityEntity.setSourceId(sourceId);
            similarityEntity.setTargetId(targetId);
            similarityEntity.setSimilarity(BigDecimal.valueOf(similarity));
            similarityEntity.setCreateTime(LocalDateTime.now());
            
            contentSimilarityMapper.insertOrUpdate(similarityEntity);
            
            return BigDecimal.valueOf(similarity);
        } catch (Exception e) {
            log.error("Failed to compute similarity for gourmet: {} -> {}", sourceId, targetId, e);
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    @Transactional
    public void batchUpdateRecommendations(List<Integer> gourmetIds) {
        log.info("Batch updating recommendations for {} gourmets", gourmetIds.size());
        
        for (Integer gourmetId : gourmetIds) {
            try {
                updateRecommendations(gourmetId);
            } catch (Exception e) {
                log.error("Failed to update recommendations for gourmet: {}", gourmetId, e);
            }
        }
    }
    
    /**
     * 检查特征是否过期（超过7天）
     */
    private boolean isFeatureExpired(ContentFeature feature) {
        return feature.getUpdateTime()
            .plusDays(7)
            .isBefore(LocalDateTime.now());
    }
    
    /**
     * 计算余弦相似度
     */
    private double calculateCosineSimilarity(Map<String, Double> features1, Map<String, Double> features2) {
        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(features1.keySet());
        allTerms.addAll(features2.keySet());
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (String term : allTerms) {
            double value1 = features1.getOrDefault(term, 0.0);
            double value2 = features2.getOrDefault(term, 0.0);
            
            dotProduct += value1 * value2;
            norm1 += value1 * value1;
            norm2 += value2 * value2;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
