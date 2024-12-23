package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Nutriment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 营养素VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutrimentVO extends Nutriment {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户的头像
     */
    private String userAvatar;

}
