package cn.kmbeast.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 食谱营养素关联信息表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CookbookNutriment {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 食谱ID
     */
    private Integer cookbookId;

    /**
     * 营养素ID
     */
    private Integer nutrimentId;

    /**
     * 每100g当中营养素的含量
     */
    private String g100Value;

}
