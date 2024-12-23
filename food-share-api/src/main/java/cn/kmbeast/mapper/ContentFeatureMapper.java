package cn.kmbeast.mapper;

import cn.kmbeast.pojo.entity.ContentFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 内容特征Mapper接口
 */
@Mapper
public interface ContentFeatureMapper {
    /**
     * 插入特征
     */
    void insert(ContentFeature contentFeature);
    
    /**
     * 根据美食ID查询特征
     */
    ContentFeature selectByGourmetId(@Param("gourmetId") Integer gourmetId);
    
    /**
     * 批量查询特征
     */
    List<ContentFeature> selectBatch(@Param("gourmetIds") List<Integer> gourmetIds);

    /**
     * 插入或更新特征
     * 如果记录存在则更新，不存在则插入
     */
    void insertOrUpdate(ContentFeature feature);

    /**
     * 删除过期的特征记录
     * @param expiryTime 过期时间
     * @return 删除的记录数
     */
    int deleteExpiredFeatures(@Param("expiryTime") LocalDateTime expiryTime);
}
