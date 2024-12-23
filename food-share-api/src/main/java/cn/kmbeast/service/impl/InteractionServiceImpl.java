package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionStaticQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.*;
import cn.kmbeast.service.InteractionService;
import cn.kmbeast.utils.DateUtil;
import cn.kmbeast.utils.TextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容互动业务逻辑接口实现类
 */
@Service
public class InteractionServiceImpl implements InteractionService {

    @Resource
    private InteractionMapper interactionMapper;
    @Resource
    private GourmetMapper gourmetMapper;

    /**
     * 查询
     *
     * @param interactionQueryDto 查询参数实体
     * @return Result<List < InteractionVO>> 通用的响应类
     */
    @Override
    public Result<List<InteractionVO>> query(InteractionQueryDto interactionQueryDto) {
        List<InteractionVO> categoryList = interactionMapper.query(interactionQueryDto);
        Integer totalCount = interactionMapper.queryCount(interactionQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 浏览操作
     * 逻辑：
     * 1. 传入内容ID，查询用户与这篇内容有没有产生浏览关系
     * 2. 如果没有，则新增这种关系；如果有，不进行处理，直接返回。
     *
     * @param contentId 内容
     * @return Result<Void> 通用的响应封装类
     */
    @Override
    public Result<Void> viewOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.VIEW.getType());
        // 有的情况，不处理
        if (operationCount > 0) {
            return ApiResult.success();
        }
        saveOperation(contentId, InteractionTypeEnum.VIEW.getType(), "VIEW");
        return ApiResult.success();
    }

    /**
     * 保存互动操作
     *
     * @param contentId 内容ID
     * @param type      互动类型
     */
    public void saveOperation(Integer contentId, Integer type, String contentType) {
        Interaction interaction = new Interaction();
        interaction.setType(type);
        interaction.setContentId(contentId);
        interaction.setContentType(contentType);
        interaction.setUserId(LocalThreadHolder.getUserId());
        interaction.setCreateTime(LocalDateTime.now());
        // 新增浏览关系
        interactionMapper.save(interaction);
    }

    /**
     * 查询指定操作的数据存量
     *
     * @param contentId 内容ID
     * @param type      操作类型
     * @return Integer 数据存量
     */
    public Integer getOperationCount(Integer contentId, Integer type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setContentId(contentId);
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type);
        return interactionMapper.queryCount(queryDto);
    }

    /**
     * 点赞操作（点赞 --- 取消点赞）
     *
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> upvoteOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.UPVOTE.getType());
        // 取消点赞
        if (operationCount > 0) {
            interactionMapper.delByUserInfo(
                    InteractionTypeEnum.UPVOTE.getType(),
                    contentId,
                    LocalThreadHolder.getUserId());
            // 代表已经删除了点赞的数据
            return ApiResult.success(0);
        }
        saveOperation(contentId, InteractionTypeEnum.UPVOTE.getType(), "UPVOTE");
        return ApiResult.success(1);
    }

    /**
     * 查询点赞状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> upvoteStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.UPVOTE.getType());
        return ApiResult.success(operationCount);
    }


    /**
     * 查询收藏状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> saveStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.COLLECTION.getType());
        return ApiResult.success(operationCount);
    }

    /**
     * 查询评分状态
     *
     * @param contentId 内容ID
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> ratingStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.RATING.getType());
        return ApiResult.success(operationCount);
    }

    /**
     * 评分操作
     *
     * @param contentId 内容ID
     * @param score     评分的分数
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Override
    public Result<List<GourmetVO>> ratingOperation(Integer contentId, Integer score) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.RATING.getType());
        if (operationCount != 0) {
            return ApiResult.error("不能重复评分");
        }
        Interaction interaction = new Interaction();
        interaction.setType(InteractionTypeEnum.RATING.getType());
        interaction.setContentId(contentId);
        interaction.setContentType("RATING");
        interaction.setUserId(LocalThreadHolder.getUserId());
        interaction.setScore(score);
        interaction.setCreateTime(LocalDateTime.now());
        interactionMapper.save(interaction);
        // 把当前最新的美食数据传回去
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setId(contentId);
        gourmetQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        gourmetQueryDto.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        List<GourmetVO> gourmetVOList = gourmetMapper.query(gourmetQueryDto);
        return ApiResult.success(gourmetVOList);
    }

    /**
     * 查询用户的评分数据
     *
     * @return Result<List < RatingVO>>
     */
    @Override
    public Result<List<RatingVO>> queryUserRating(InteractionQueryDto interactionQueryDto) {
        interactionQueryDto.setType(InteractionTypeEnum.RATING.getType());
        List<InteractionVO> interactionVOList
                = interactionMapper.query(interactionQueryDto);
        List<RatingVO> ratingVOS = interactionVOList.stream().map(interactionVO -> new RatingVO(
                interactionVO.getUserAvatar(),
                interactionVO.getUserName(),
                interactionVO.getScore(),
                interactionVO.getCreateTime()
        )).collect(Collectors.toList());
        return ApiResult.success(ratingVOS);
    }

    /**
     * 收藏操作（收藏 --- 取消收藏）
     *
     * @return Result<Void> 响应结果
     */
    @Override
    public Result<Integer> saveOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.COLLECTION.getType());
        // 取消收藏
        if (operationCount > 0) {
            interactionMapper.delByUserInfo(
                    InteractionTypeEnum.COLLECTION.getType(),
                    contentId,
                    LocalThreadHolder.getUserId());
            // 代表已经删除了收藏的数据
            return ApiResult.success(0);
        }
        saveOperation(contentId, InteractionTypeEnum.COLLECTION.getType(), "COLLECTION");
        return ApiResult.success(1);
    }

    /**
     * 查询我的收藏列表
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetListVO>> queryCollectionList(InteractionQueryDto interactionQueryDto) {
        // 1. 查询我收藏了哪些内容
        // 设置用户ID
        interactionQueryDto.setUserId(LocalThreadHolder.getUserId());
        // 设置收藏类型
        interactionQueryDto.setType(InteractionTypeEnum.COLLECTION.getType());
        List<InteractionVO> interactionVOS = interactionMapper.query(interactionQueryDto);
        // 2. 拿到我操作了哪些内容，拿到ID列表
        List<Integer> gourmetIds = interactionVOS.stream()
                .map(InteractionVO::getContentId)
                .collect(Collectors.toList());
        // 3. 通过这些ID，去拿到对应的美食做法列表数据
        List<GourmetVO> gourmetVOS = gourmetMapper.queryByIds(gourmetIds);
        // 转成ListVO出去
        List<GourmetListVO> gourmetListVOS = gourmetVOS.stream()
                .map(gourmetVO -> new GourmetListVO(
                        gourmetVO.getId(),
                        gourmetVO.getTitle(),
                        gourmetVO.getCover(),
                        TextUtil.parseText(gourmetVO.getContent(), 170),
                        gourmetVO.getUserName(),
                        gourmetVO.getUserAvatar(),
                        gourmetVO.getViewCount(),
                        gourmetVO.getUpvoteCount(),
                        gourmetVO.getSaveCount(),
                        gourmetVO.getRating(),
                        gourmetVO.getCreateTime()
                )).collect(Collectors.toList());
        return ApiResult.success(gourmetListVOS);
    }

    /**
     * 统计帖子流量状况
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(InteractionQueryDto interactionQueryDto) {
        // 查询自己的美食做法
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<GourmetVO> gourmetVOS = gourmetMapper.query(gourmetQueryDto);
        // 取出里面全部的ID
        List<Integer> gourmetIds = gourmetVOS.stream()
                .map(GourmetVO::getId)
                .collect(Collectors.toList());
        QueryDto queryDto = DateUtil.startAndEndTime(interactionQueryDto.getDay());
        interactionQueryDto.setStartTime(queryDto.getStartTime());
        interactionQueryDto.setEndTime(queryDto.getEndTime());
        InteractionStaticQueryDto interactionStaticQueryDto = new InteractionStaticQueryDto();
        interactionStaticQueryDto.setInteractionQueryDto(interactionQueryDto);
        interactionStaticQueryDto.setGourmetIds(gourmetIds);
        List<InteractionVO> interactionVOS = interactionMapper.queryDays(interactionStaticQueryDto);
        List<LocalDateTime> localDateTimes = interactionVOS.stream()
                .map(InteractionVO::getCreateTime)
                .collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(
                interactionQueryDto.getDay(),
                localDateTimes
        );
        return ApiResult.success(chartVOS);
    }

    /**
     * 删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        interactionMapper.batchDelete(ids);
        return ApiResult.success();
    }
}
