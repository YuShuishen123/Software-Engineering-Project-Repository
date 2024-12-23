package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentBasedQueryDto;
import cn.kmbeast.pojo.vo.ContentRecommendVO;
import java.util.List;
import java.util.Map;

/**
 * 基于内容的推荐服务接口
 */
public interface ContentRecommendService {
    /**
     * 推荐相似内容
     *
     * @param queryDto 查询参数
     * @return Result<List<ContentRecommendVO>> 推荐结果
     */
    Result<List<ContentRecommendVO>> recommendSimilar(ContentBasedQueryDto queryDto);

    /**
     * 获取内容特征
     *
     * @param gourmetId 美食ID
     * @return Result<Map<String, Double>> 特征权重映射
     */
    Result<Map<String, Double>> getContentFeatures(Integer gourmetId);
}
