package cn.kmbeast.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 内容特征实体类
 */
@Data
public class ContentFeature {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 美食ID
     */
    private Integer gourmetId;
    
    /**
     * 特征向量(JSON格式存储)
     */
    private String featureVector;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
