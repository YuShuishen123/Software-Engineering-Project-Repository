package cn.kmbeast.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 内容相似度实体类
 */
@Data
public class ContentSimilarity {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 源美食ID
     */
    private Integer sourceId;
    
    /**
     * 目标美食ID
     */
    private Integer targetId;
    
    /**
     * 相似度得分
     */
    private BigDecimal similarity;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
