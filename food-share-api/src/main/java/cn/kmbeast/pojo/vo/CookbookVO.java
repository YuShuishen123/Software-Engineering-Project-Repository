package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.entity.Cookbook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 食谱VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CookbookVO extends Cookbook {

    /**
     * 所属美食分类的名称
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

}
