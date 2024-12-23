package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户饮食查询条件类Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DietHistoryQueryDto extends QueryDto {

    /**
     * 食谱ID
     */
    private Integer cookbookId;

    /**
     * 用户ID
     */
    private Integer userId;

}
