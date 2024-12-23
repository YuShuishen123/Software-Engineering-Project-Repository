package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 美食做法查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GourmetQueryDto extends QueryDto {

    /**
     * 美食分类ID
     */
    private Integer categoryId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否已经审核
     */
    private Boolean isAudit;

    /**
     * 是否公开
     */
    private Boolean isPublish;
}
