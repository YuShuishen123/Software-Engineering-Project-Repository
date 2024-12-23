package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户饮食的持久化接口
 */
@Mapper
public interface DietHistoryMapper {

    void save(@Param(value = "dietHistories") List<DietHistory> dietHistories);

    void update(DietHistory dietHistory);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<DietHistoryVO> query(DietHistoryQueryDto dietHistoryQueryDto);

    Integer queryCount(DietHistoryQueryDto dietHistoryQueryDto);

}
