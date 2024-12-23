package cn.kmbeast.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基于内容的推荐展示VO
 */
@Data
public class ContentRecommendVO {
    /**
     * ID
     */
    private Integer id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 封面
     */
    private String cover;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 用户头像
     */
    private String userAvatar;
    
    /**
     * 浏览数
     */
    private Integer viewCount;
    
    /**
     * 点赞数
     */
    private Integer upvoteCount;
    
    /**
     * 收藏数
     */
    private Integer saveCount;
    
    /**
     * 评分
     */
    private BigDecimal rating;
    
    /**
     * 相似度分数
     */
    private BigDecimal similarity;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
