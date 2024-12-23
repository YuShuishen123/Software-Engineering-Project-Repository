package cn.kmbeast.pojo.vo;

import lombok.Data;

/**
 * 内容中心返回VO类
 */
@Data
public class CenterVO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头像
     */
    private String userAvatar;
    /**
     * 点赞量
     */
    private Integer upvoteCount;
    /**
     * 收藏量
     */
    private Integer saveCount;
    /**
     * 阅读量
     */
    private Integer viewCount;
}
