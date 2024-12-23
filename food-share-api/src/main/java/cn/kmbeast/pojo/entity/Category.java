package cn.kmbeast.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 美食类别的实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 类别名
     */
    private String name;

}
