package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公开状态枚举
 */
@Getter
@AllArgsConstructor
public enum PublishEnum {

    NO_AUDIT(false, "私人"),
    OK_AUDIT(true, "公开");

    /**
     * 编码
     */
    private final Boolean flag;
    /**
     * 名称
     */
    private final String name;

}
