package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 饮食记录表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietHistory {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 食谱ID
     */
    private Integer cookbookId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 备注
     */
    private String detail;

    /**
     * 记录的值
     */
    private String value;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
