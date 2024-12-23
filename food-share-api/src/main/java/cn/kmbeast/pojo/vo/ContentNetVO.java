package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.entity.Gourmet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 内容分享VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentNetVO extends ContentNet {

    /**
     * 美食做法标题
     */
    private String gourmetTitle;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户的头像
     */
    private String userAvatar;

}
