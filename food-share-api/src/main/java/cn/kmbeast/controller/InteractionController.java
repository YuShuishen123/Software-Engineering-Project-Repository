package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.vo.*;
import cn.kmbeast.service.InteractionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 互动表的控制器
 */
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    /**
     * 查询用户的评分数据
     *
     * @return Result<List < RatingVO>>
     */
    @Pager
    @PostMapping(value = "/queryUserRating")
    @ResponseBody
    public Result<List<RatingVO>> queryUserRating(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.queryUserRating(interactionQueryDto);
    }

    /**
     * 评分操作
     *
     * @param contentId 内容ID
     * @param score     评分的分数
     * @return Result<List < GourmetVO>> 响应结果 --- 返回最新的美食数据
     */
    @PostMapping(value = "/ratingOperation/{contentId}/{score}")
    @ResponseBody
    public Result<List<GourmetVO>> ratingOperation(@PathVariable Integer contentId,
                                                   @PathVariable Integer score) {
        return interactionService.ratingOperation(contentId, score);
    }

    /**
     * 收藏操作（收藏 --- 取消收藏）
     *
     * @return Result<Void> 响应结果
     */
    @PostMapping(value = "/saveOperation/{contentId}")
    @ResponseBody
    public Result<Integer> saveOperation(@PathVariable Integer contentId) {
        return interactionService.saveOperation(contentId);
    }

    /**
     * 点赞操作（点赞 --- 取消点赞）
     *
     * @return Result<Void> 响应结果
     */
    @PostMapping(value = "/upvoteOperation/{contentId}")
    @ResponseBody
    public Result<Integer> upvoteOperation(@PathVariable Integer contentId) {
        return interactionService.upvoteOperation(contentId);
    }

    /**
     * 浏览操作
     *
     * @return Result<Void> 响应结果
     */
    @PostMapping(value = "/viewOperation/{contentId}")
    @ResponseBody
    public Result<Void> viewOperation(@PathVariable Integer contentId) {
        return interactionService.viewOperation(contentId);
    }

    /**
     * 查询点赞状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/upvoteStatus/{contentId}")
    @ResponseBody
    public Result<Integer> upvoteStatus(@PathVariable Integer contentId) {
        return interactionService.upvoteStatus(contentId);
    }

    /**
     * 查询收藏状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/saveStatus/{contentId}")
    @ResponseBody
    public Result<Integer> saveStatus(@PathVariable Integer contentId) {
        return interactionService.saveStatus(contentId);
    }

    /**
     * 查询评分状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/ratingStatus/{contentId}")
    @ResponseBody
    public Result<Integer> ratingStatus(@PathVariable Integer contentId) {
        return interactionService.ratingStatus(contentId);
    }

    /**
     * 查询互动行为
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < InteractionVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<InteractionVO>> query(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.query(interactionQueryDto);
    }


    /**
     * 查询我的收藏列表
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryCollectionList")
    @ResponseBody
    public Result<List<GourmetListVO>> queryCollectionList(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.queryCollectionList(interactionQueryDto);
    }

    /**
     * 查询所有的互动类型
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/types")
    @ResponseBody
    public Result<List<SelectedVO>> types() {
        InteractionTypeEnum[] interactionTypeEnums = InteractionTypeEnum.values();
        List<SelectedVO> selectedVOList = new ArrayList<>();
        for (InteractionTypeEnum interactionTypeEnum : interactionTypeEnums) {
            SelectedVO selectedVO = new SelectedVO(
                    interactionTypeEnum.getType(),
                    interactionTypeEnum.getDetail()
            );
            selectedVOList.add(selectedVO);
        }
        return ApiResult.success(selectedVOList);
    }

    /**
     * 删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return interactionService.batchDelete(ids);
    }

    /**
     * 统计帖子流量状况
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @PostMapping(value = "/daysQuery")
    @ResponseBody
    public Result<List<ChartVO>> daysQuery(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.daysQuery(interactionQueryDto);
    }

}

