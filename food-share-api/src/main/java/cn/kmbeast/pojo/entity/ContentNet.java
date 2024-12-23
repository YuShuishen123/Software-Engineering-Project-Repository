package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 内容分享信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentNet {

    /**
     * 主键ID
     */
    private Integer id;

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
     * 有效天数
     */
    private Integer validDay;

    /**
     * 访问密码
     */
    private String accessPassword;

    /**
     * 是否是密码认证
     * true ---> 1;false ---> 0; while(1){  } = while(true){  }
     */
    private Boolean passwordAuth;

    /**
     * 分享时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
