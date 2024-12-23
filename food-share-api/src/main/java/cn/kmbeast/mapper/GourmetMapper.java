package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 美食做法的持久化接口
 */
@Mapper
public interface GourmetMapper {

    void save(Gourmet gourmet);

    void update(Gourmet gourmet);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<GourmetVO> query(GourmetQueryDto gourmetQueryDto);

    List<GourmetVO> queryByView(GourmetQueryDto gourmetQueryDto);

    Integer queryCount(GourmetQueryDto gourmetQueryDto);

    List<GourmetVO> queryByIds(@Param(value = "ids") Collection<Integer> ids);

    /**
     * 根据ID查询美食
     *
     * @param id 美食ID
     * @return GourmetVO
     */
    GourmetVO queryById(@Param("id") Integer id);

    /**
     * 获取所有美食ID
     *
     * @return 美食ID列表
     */
    List<Integer> getAllIds();

    /**
     * 获取除指定ID外的所有美食ID
     *
     * @param id 排除的美食ID
     * @return 美食ID列表
     */
    List<Integer> getAllIdsExcept(@Param("id") Integer id);
}
