package cn.kmbeast.utils;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 内容特征工具类
 */
@Component
public class ContentFeatureUtils {
    
    /**
     * 提取文本特征
     *
     * @param title 标题
     * @param content 内容
     * @return Map<String, Double> 特征向量
     */
    public Map<String, Double> extractFeatures(String title, String content) {
        Map<String, Double> features = new HashMap<>();
        
        // 1. 分词
        List<String> words = segmentText(title + " " + content);
        
        // 2. 计算词频
        Map<String, Integer> termFrequency = calculateTermFrequency(words);
        
        // 3. 计算TF-IDF
        for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
            String term = entry.getKey();
            double tf = entry.getValue() / (double) words.size();
            double idf = calculateIDF(term); // 这里需要实现IDF计算
            features.put(term, tf * idf);
        }
        
        return features;
    }
    
    /**
     * 计算余弦相似度
     *
     * @param vector1 向量1
     * @param vector2 向量2
     * @return double 相似度
     */
    public double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        // 1. 计算点积
        double dotProduct = 0.0;
        for (String term : vector1.keySet()) {
            if (vector2.containsKey(term)) {
                dotProduct += vector1.get(term) * vector2.get(term);
            }
        }
        
        // 2. 计算向量模长
        double norm1 = calculateNorm(vector1);
        double norm2 = calculateNorm(vector2);
        
        // 3. 计算余弦相似度
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (norm1 * norm2);
    }
    
    /**
     * 分词
     */
    private List<String> segmentText(String text) {
        // TODO: 实现中文分词，可以使用HanLP或其他分词工具
        return Arrays.asList(text.split("\\s+"));
    }
    
    /**
     * 计算词频
     */
    private Map<String, Integer> calculateTermFrequency(List<String> words) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.merge(word, 1, Integer::sum);
        }
        return frequency;
    }
    
    /**
     * 计算IDF
     */
    private double calculateIDF(String term) {
        // TODO: 实现IDF计算，需要统计词在所有文档中的出现情况
        return 1.0;
    }
    
    /**
     * 计算向量模长
     */
    private double calculateNorm(Map<String, Double> vector) {
        double sumSquares = 0.0;
        for (double value : vector.values()) {
            sumSquares += value * value;
        }
        return Math.sqrt(sumSquares);
    }
}
