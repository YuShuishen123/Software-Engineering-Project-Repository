package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 行为互动表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Interaction {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 互动类型
     */
    private Integer type;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 内容ID
     */
    private Integer contentId;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 互动时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
