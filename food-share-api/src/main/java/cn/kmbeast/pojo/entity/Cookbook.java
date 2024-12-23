package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 食谱表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cookbook {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 标题
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 所属分类ID
     */
    private Integer categoryId;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否是公开食谱
     */
    private Boolean isPublish;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
