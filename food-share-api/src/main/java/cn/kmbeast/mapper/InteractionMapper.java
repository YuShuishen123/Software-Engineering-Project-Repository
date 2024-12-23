package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionStaticQueryDto;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.InteractionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 内容互动的持久化接口
 */
@Mapper
public interface InteractionMapper {

    void save(Interaction interaction);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    void delByUserInfo(@Param(value = "type") Integer type,
                       @Param(value = "contentId") Integer contentId,
                       @Param(value = "userId") Integer userId);

    List<InteractionVO> query(InteractionQueryDto interactionQueryDto);

    List<InteractionVO> queryDays(InteractionStaticQueryDto interactionStaticQueryDto);

    Integer queryCount(InteractionQueryDto interactionQueryDto);

}
