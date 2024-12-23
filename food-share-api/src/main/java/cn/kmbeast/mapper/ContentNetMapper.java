package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ContentNetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 内容分享的持久化接口
 */
@Mapper
public interface ContentNetMapper {

    void save(ContentNet contentNet);

    void update(ContentNet contentNet);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<ContentNetVO> query(ContentNetQueryDto contentNetQueryDto);

    Integer queryCount(ContentNetQueryDto contentNetQueryDto);

}
