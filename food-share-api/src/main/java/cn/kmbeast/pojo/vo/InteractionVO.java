package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.entity.Interaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 互动信息VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteractionVO extends Interaction {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户的头像
     */
    private String userAvatar;
    /**
     * 标题
     */
    private String contentTitle;

}
