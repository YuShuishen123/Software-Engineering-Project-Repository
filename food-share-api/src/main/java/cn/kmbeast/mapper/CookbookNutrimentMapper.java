package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 食谱营养素关联的持久化接口
 */
@Mapper
public interface CookbookNutrimentMapper {

    void save(CookbookNutriment cookbookNutriment);

    void update(CookbookNutriment cookbookNutriment);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<CookbookNutrimentVO> query(CookbookNutrimentQueryDto queryDto);

    Integer queryCount(CookbookNutrimentQueryDto queryDto);

    List<CookbookNutrimentVO> queryByCookbookIds(@Param(value = "ids") List<Integer> ids);

}
