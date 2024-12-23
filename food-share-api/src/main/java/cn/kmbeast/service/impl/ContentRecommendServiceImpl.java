package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.ContentFeatureMapper;
import cn.kmbeast.mapper.ContentSimilarityMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.api.ResultCode;
import cn.kmbeast.pojo.dto.query.extend.ContentBasedQueryDto;
import cn.kmbeast.pojo.entity.ContentFeature;
import cn.kmbeast.pojo.entity.ContentSimilarity;
import cn.kmbeast.pojo.vo.ContentRecommendVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.ContentRecommendComputeService;
import cn.kmbeast.service.ContentRecommendService;
import cn.kmbeast.utils.TextUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于内容的推荐服务实现类
 */
@Service
public class ContentRecommendServiceImpl implements ContentRecommendService {

    private static final Logger log = LoggerFactory.getLogger(ContentRecommendServiceImpl.class);

    @Autowired
    private ContentSimilarityMapper contentSimilarityMapper;

    @Autowired
    private GourmetMapper gourmetMapper;
    
    @Autowired
    private ContentRecommendComputeService computeService;

    @Autowired
    private ContentFeatureMapper contentFeatureMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Result<List<ContentRecommendVO>> recommendSimilar(ContentBasedQueryDto queryDto) {
        // 参数验证
        if (queryDto.getGourmetId() == null || queryDto.getGourmetId() <= 0) {
            return new ApiResult<>(ResultCode.REQUEST_ERROR.getCode(), "美食ID不能为空");
        }
        if (queryDto.getSize() == null || queryDto.getSize() <= 0) {
            queryDto.setSize(5);  // 默认5条
        }

        try {
            // 1. 获取相似度数据
            List<ContentSimilarity> similarities = contentSimilarityMapper.selectBySourceId(
                queryDto.getGourmetId(), 
                queryDto.getSize()
            );


            // 如果没有相似度数据，先计算一次
            if (CollectionUtils.isEmpty(similarities)) {
                computeService.updateRecommendations(queryDto.getGourmetId());
                // 重新获取相似度数据
                similarities = contentSimilarityMapper.selectBySourceId(
                    queryDto.getGourmetId(), 
                    queryDto.getSize()
                );
            }

            if (CollectionUtils.isEmpty(similarities)) {
                return ApiResult.success(Collections.emptyList());
            }

            // 2. 获取目标美食信息
            Set<Integer> targetIds = similarities.stream()
                .map(ContentSimilarity::getTargetId)
                .collect(Collectors.toSet());
            List<GourmetVO> gourmetVOs = gourmetMapper.queryByIds(targetIds);

            if (CollectionUtils.isEmpty(gourmetVOs)) {
                return ApiResult.success(Collections.emptyList());
            }

            // 3. 构建推荐结果
            Map<Integer, GourmetVO> gourmetMap = gourmetVOs.stream()
                .collect(Collectors.toMap(GourmetVO::getId, vo -> vo));

            List<ContentRecommendVO> recommendVOs = similarities.stream()
                .map(similarity -> {
                    ContentRecommendVO vo = new ContentRecommendVO();
                    GourmetVO gourmet = gourmetMap.get(similarity.getTargetId());
                    if (gourmet != null) {
                        vo.setId(gourmet.getId());
                        vo.setTitle(gourmet.getTitle());
                        vo.setContent(gourmet.getContent());
                        vo.setCategoryName(gourmet.getCategoryName());
                        vo.setUserName(gourmet.getUserName());
                        vo.setUserAvatar(gourmet.getUserAvatar());
                        vo.setCover(gourmet.getCover());
                        vo.setViewCount(gourmet.getViewCount());
                        vo.setUpvoteCount(gourmet.getUpvoteCount());
                        vo.setSaveCount(gourmet.getSaveCount());
                        // 转换 Double 到 BigDecimal
                        vo.setRating(gourmet.getRating() != null ? 
                            BigDecimal.valueOf(gourmet.getRating()) : null);
                        vo.setSimilarity(similarity.getSimilarity());
                        vo.setCreateTime(gourmet.getCreateTime());
                    }
                    return vo;
                })
                .filter(vo -> vo.getId() != null)
                .collect(Collectors.toList());

            return new Result<>(
                ResultCode.REQUEST_SUCCESS.getCode(),
                "操作成功",
                recommendVOs
            );
        } catch (Exception e) {
            log.error("Failed to get similar recommendations for gourmet: {}", queryDto.getGourmetId(), e);
            return ApiResult.success(Collections.emptyList());  // 出错时返回空列表
        }
    }

    @Override
    public Result<Map<String, Double>> getContentFeatures(Integer gourmetId) {
        // 1. 获取美食信息
        GourmetVO gourmet = gourmetMapper.queryById(gourmetId);
        if (gourmet == null) {
            return ApiResult.error("美食不存在");
        }

        // 2. 计算并保存特征
        boolean success = computeService.computeAndSaveFeatures(gourmetId);
        if (!success) {
            return ApiResult.error("特征计算失败");
        }

        // 3. 从数据库获取特征
        ContentFeature feature = contentFeatureMapper.selectByGourmetId(gourmetId);
        if (feature == null || !StringUtils.hasText(feature.getFeatureVector())) {
            return ApiResult.error("特征不存在");
        }

        // 4. 返回特征数据
        try {
            @SuppressWarnings("unchecked")
            Map<String, Double> features = objectMapper.readValue(
                feature.getFeatureVector(),
                new TypeReference<Map<String, Double>>() {}
            );
            return ApiResult.success(features);
        } catch (Exception e) {
            log.error("Failed to parse feature vector for gourmet: {}", gourmetId, e);
            return ApiResult.error("特征解析失败");
        }
    }
}
