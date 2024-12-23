package cn.kmbeast.controller;

import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentBasedQueryDto;
import cn.kmbeast.pojo.vo.ContentRecommendVO;
import cn.kmbeast.service.ContentRecommendService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 基于内容的推荐控制器
 */
@RestController
@RequestMapping("/content/recommend")
public class ContentRecommendController {
    
    @Resource
    private ContentRecommendService contentRecommendService;
    
    /**
     * 获取相似内容推荐
     *
     * @param gourmetId 美食ID
     * @param size 推荐数量
     * @return Result<List<ContentRecommendVO>> 推荐结果
     */
    @GetMapping("/similar")
    public Result<List<ContentRecommendVO>> getSimilarContent(
            @RequestParam Integer gourmetId,
            @RequestParam(defaultValue = "5") Integer size) {
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(gourmetId);
        queryDto.setSize(size);
        System.out.println("后端返回的："+contentRecommendService.recommendSimilar(queryDto));

         return contentRecommendService.recommendSimilar(queryDto);
    }

    
    /**
     * 获取内容特征
     *
     * @param gourmetId 美食ID
     * @return Result<Map<String, Double>> 特征权重映射
     */
    @GetMapping("/features")
    public Result<Map<String, Double>> getContentFeatures(@RequestParam Integer gourmetId) {
        return contentRecommendService.getContentFeatures(gourmetId);
    }
}
