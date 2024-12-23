package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.vo.*;

import java.util.List;

/**
 * 内容互动业务逻辑接口
 */
public interface InteractionService {

    Result<List<InteractionVO>> query(InteractionQueryDto interactionQueryDto);

    Result<Void> viewOperation(Integer contentId);

    Result<Integer> upvoteOperation(Integer contentId);

    Result<Integer> upvoteStatus(Integer contentId);

    Result<Integer> saveOperation(Integer contentId);

    Result<Integer> saveStatus(Integer contentId);

    Result<List<GourmetListVO>> queryCollectionList(InteractionQueryDto interactionQueryDto);

    Result<Integer> ratingStatus(Integer contentId);

    Result<List<GourmetVO>> ratingOperation(Integer contentId, Integer score);

    Result<List<RatingVO>> queryUserRating(InteractionQueryDto interactionQueryDto);

    Result<List<ChartVO>> daysQuery(InteractionQueryDto interactionQueryDto);

    Result<String> batchDelete(List<Integer> ids);

}
