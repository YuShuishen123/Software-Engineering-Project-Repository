package cn.kmbeast.controller;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.ContentRecommendVO;
import cn.kmbeast.pojo.dto.query.extend.ContentBasedQueryDto;
import cn.kmbeast.service.RecommendService;
import cn.kmbeast.service.ContentRecommendService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 内容推荐的控制器
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private RecommendService recommendService;

    @Resource
    private ContentRecommendService contentRecommendService;

    /**
     * 查询推荐的美食做法数据
     *
     * @param item 需要的推荐的条数
     * @return Result<List<GourmetVO>> 响应结果
     */
    @GetMapping(value = "/{item}")
    @ResponseBody
    public Result<List<GourmetVO>> recommendGourmet(@PathVariable Integer item) {
        return recommendService.recommendGourmet(item);
    }

    /**
     * 获取相似美食推荐
     *
     * @param gourmetId 美食ID
     * @param size 推荐数量
     * @return Result<List<ContentRecommendVO>> 响应结果
     */
    @GetMapping("/similar")
    @ResponseBody
    public Result<List<ContentRecommendVO>> getSimilarRecommends(
        @RequestParam Integer gourmetId,
        @RequestParam(defaultValue = "5") Integer size
    ) {
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(gourmetId);
        queryDto.setSize(size);
        return contentRecommendService.recommendSimilar(queryDto);
    }
}
