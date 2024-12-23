package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 美食分类查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQueryDto extends QueryDto {
    /**
     * 分类名
     */
    private String name;
}
