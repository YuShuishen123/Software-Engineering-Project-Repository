package cn.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉选择器的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedVO {
    private Integer id;
    private String name;
}
