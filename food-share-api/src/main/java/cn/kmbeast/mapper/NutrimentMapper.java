package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营养素的持久化接口
 */
@Mapper
public interface NutrimentMapper {

    void save(Nutriment nutriment);

    void update(Nutriment nutriment);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<NutrimentVO> query(NutrimentQueryDto nutrimentQueryDto);

    Integer queryCount(NutrimentQueryDto nutrimentQueryDto);

}
