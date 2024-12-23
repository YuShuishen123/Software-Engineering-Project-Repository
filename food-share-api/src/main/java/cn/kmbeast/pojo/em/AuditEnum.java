package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum AuditEnum {

    NO_AUDIT(false, "未审核"),
    OK_AUDIT(true, "已审核");

    /**
     * 编码
     */
    private final Boolean flag;
    /**
     * 名称
     */
    private final String name;

}
