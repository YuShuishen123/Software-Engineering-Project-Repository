package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.DietHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户饮食出参类VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DietHistoryVO extends DietHistory {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 食谱封面
     */
    private String cookbookCover;
    /**
     * 食谱名
     */
    private String cookbookTitle;
    /**
     * 营养素ID
     */
    private Integer nutrimentId;
    /**
     * 营养素单位
     */
    private String nutrimentUnit;
    /**
     * 营养素名称
     */
    private String nutrimentName;
    /**
     * 每100g里面，营养素含量
     */
    private String g100Value;
}
