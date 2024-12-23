package cn.kmbeast.pojo.dto.query.extend;

import lombok.Data;

/**
 * 基于内容的推荐查询DTO
 */
@Data
public class ContentBasedQueryDto {
    /**
     * 美食ID
     */
    private Integer gourmetId;
    
    /**
     * 每页大小
     */
    private Integer size;
    
    /**
     * 当前页
     */
    private Integer current;
}
