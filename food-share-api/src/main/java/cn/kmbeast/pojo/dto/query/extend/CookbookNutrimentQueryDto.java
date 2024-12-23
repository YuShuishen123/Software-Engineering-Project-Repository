package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食谱营养素关联查询条件类Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookNutrimentQueryDto extends QueryDto {
    /**
     * 食谱ID
     */
    private Integer cookbookId;

    /**
     * 营养素ID
     */
    private Integer nutrimentId;
}
