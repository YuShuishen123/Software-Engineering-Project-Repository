package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 营养素信息表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nutriment {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String detail;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否公开
     */
    private Boolean isPublish;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
