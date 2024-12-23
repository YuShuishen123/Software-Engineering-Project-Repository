package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 内容分享查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentNetQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 美食做法ID
     */
    private Integer gourmetId;
    /**
     * 访问码
     */
    private String accessCode;
    /**
     * 查询天数
     */
    private Integer day;
    /**
     * 访问密码
     */
    private String accessPassword;
}
