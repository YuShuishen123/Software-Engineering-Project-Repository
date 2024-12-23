package cn.kmbeast.mapper;

import cn.kmbeast.pojo.entity.ContentSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 内容相似度Mapper接口
 */
@Mapper
public interface ContentSimilarityMapper {
    /**
     * 插入相似度
     */
    void insert(ContentSimilarity contentSimilarity);
    
    /**
     * 批量插入相似度
     */
    void batchInsert(@Param("similarities") List<ContentSimilarity> similarities);
    
    /**
     * 根据源ID查询相似内容
     */
    List<ContentSimilarity> selectBySourceId(@Param("sourceId") Integer sourceId, @Param("limit") Integer limit);
    
    /**
     * 插入或更新相似度
     * 如果记录存在则更新，不存在则插入
     */
    void insertOrUpdate(ContentSimilarity similarity);
    
    /**
     * 根据源ID和目标ID查询相似度
     */
    ContentSimilarity selectBySourceAndTargetId(@Param("sourceId") Integer sourceId, @Param("targetId") Integer targetId);
    
    /**
     * 删除指定美食的所有相似度记录
     */
    void deleteBySourceId(@Param("sourceId") Integer sourceId);
}
