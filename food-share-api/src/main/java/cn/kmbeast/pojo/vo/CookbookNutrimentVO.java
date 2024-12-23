package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.CookbookNutriment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食谱营养素关联VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookNutrimentVO extends CookbookNutriment {
    /**
     * 食谱名
     */
    private String cookbookName;
    /**
     * 营养素名称
     */
    private String nutrimentName;
    /**
     * 营养素单位
     */
    private String nutrimentUnit;
}
