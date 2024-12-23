package cn.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评分的VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingVO {
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户的名称
     */
    private String userName;
    /**
     * 它评了多少分
     */
    private Integer score;
    /**
     * 什么时候评分的---时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
