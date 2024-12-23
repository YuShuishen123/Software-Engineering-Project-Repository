package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 食谱的持久化接口
 */
@Mapper
public interface CookbookMapper {

    void save(Cookbook cookbook);

    void update(Cookbook cookbook);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<CookbookVO> query(CookbookQueryDto cookbookQueryDto);

    Integer queryCount(CookbookQueryDto cookbookQueryDto);

}
