package cn.kmbeast.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 内容推荐计算服务接口
 * 负责处理特征提取和相似度计算的核心逻辑
 */
public interface ContentRecommendComputeService {
    /**
     * 计算并保存美食的特征向量
     * @param gourmetId 美食ID
     * @return 是否成功保存特征
     */
    boolean computeAndSaveFeatures(Integer gourmetId);
    
    /**
     * 计算并保存两个美食之间的相似度
     * @param sourceId 源美食ID
     * @param targetId 目标美食ID
     * @return 相似度得分
     */
    BigDecimal computeAndSaveSimilarity(Integer sourceId, Integer targetId);
    
    /**
     * 更新指定美食的所有相关推荐
     * @param gourmetId 美食ID
     */
    void updateRecommendations(Integer gourmetId);
    
    /**
     * 批量更新推荐
     * @param gourmetIds 美食ID列表
     */
    void batchUpdateRecommendations(List<Integer> gourmetIds);
}
