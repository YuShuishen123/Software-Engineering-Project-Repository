package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食谱查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 所属分类ID
     */
    private Integer categoryId;

    /**
     * 是否公开
     */
    private Boolean isPublish;
}
