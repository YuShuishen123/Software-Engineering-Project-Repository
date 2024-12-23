package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Gourmet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 美食做法VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GourmetVO extends Gourmet {

    /**
     * 美食类别名
     */
    private String categoryName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户的头像
     */
    private String userAvatar;
    /**
     * 浏览量
     */
    private Integer viewCount;
    /**
     * 点赞量
     */
    private Integer upvoteCount;
    /**
     * 收藏量
     */
    private Integer saveCount;
    /**
     * 评分
     */
    private Double rating;

}
