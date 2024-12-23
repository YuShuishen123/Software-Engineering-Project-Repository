package cn.kmbeast.pojo.dto.query.extend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分的Dto类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 物品ID
     */
    private Integer itemId;
    /**
     * 评分
     */
    private Integer score;
}
