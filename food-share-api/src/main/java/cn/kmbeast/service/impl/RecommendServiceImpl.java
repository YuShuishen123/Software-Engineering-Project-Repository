package cn.kmbeast.service.impl;


import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.RatingDto;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.RecommendService;
import cn.kmbeast.utils.MahoutUtils;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐业务逻辑实现
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private InteractionMapper interactionMapper;
    @Resource
    private GourmetMapper gourmetMapper;


    /**
     * 查询需要推荐给用户的美食做法帖子数据
     *
     * @param item 需要的条数
     * @return Result<List < GourmetVO>>
     */
    @Override
    public Result<List<GourmetVO>> recommendGourmet(Integer item) {
        // 1. 需要数据集(用户关于物品的评分数据)
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setType(InteractionTypeEnum.RATING.getType());
        List<InteractionVO> interactionVOS = interactionMapper.query(queryDto);
        List<RatingDto> ratingDtoList = interactionVOS.stream().map(interactionVO -> new RatingDto(
                interactionVO.getUserId(),
                interactionVO.getContentId(),
                interactionVO.getScore()
        )).collect(Collectors.toList());
        List<RecommendedItem> recommenderList
                = MahoutUtils.recommender(
                ratingDtoList,
                (long) LocalThreadHolder.getUserId(),
                item
        );
        List<Long> gourmetIds = recommenderList.stream()
                .map(RecommendedItem::getItemID)
                .collect(Collectors.toList());
        // 有三种情况： 1. 直接没有数据，算不出来；2. 算出来了
        if (CollectionUtils.isEmpty(gourmetIds)) {
            // 直接返回当前阅读量最高的
            GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
            gourmetQueryDto.setCurrent(0);
            gourmetQueryDto.setSize(item);
            List<GourmetVO> gourmetVOS = gourmetMapper.queryByView(gourmetQueryDto);
            return ApiResult.success(gourmetVOS);
        }
        // TODO 还需要进一步优化
        List<Integer> ids = new ArrayList<>();
        for (Long gourmetId : gourmetIds) {
            ids.add(Integer.parseInt(String.valueOf(gourmetId)));
        }
        // 最终推荐的美食做法
        List<GourmetVO> gourmetVOS = gourmetMapper.queryByIds(ids);
        return ApiResult.success(gourmetVOS);
    }

}
