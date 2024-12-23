package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 互动信息查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 内容ID
     */
    private Integer contentId;
    /**
     * 指定查询的天数
     */
    private Integer day;
    /**
     * 互动类型
     */
    private Integer type;
}
