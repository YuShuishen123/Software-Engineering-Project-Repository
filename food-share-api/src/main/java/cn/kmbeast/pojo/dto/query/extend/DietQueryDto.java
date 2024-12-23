package cn.kmbeast.pojo.dto.query.extend;

import lombok.Data;

/**
 * 用户查询营养摄入情况查询条件类
 */
@Data
public class DietQueryDto {
    /**
     * 查询天数
     */
    private Integer day;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 营养素ID
     */
    private Integer nutrimentId;
}
