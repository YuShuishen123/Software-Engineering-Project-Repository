package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营养素查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NutrimentQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否公开
     */
    private Boolean isPublish;
}
