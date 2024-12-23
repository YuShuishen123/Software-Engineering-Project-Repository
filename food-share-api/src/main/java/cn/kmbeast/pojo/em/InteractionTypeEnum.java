package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 互动类型的枚举
 */
@Getter
@AllArgsConstructor
public enum InteractionTypeEnum {

    UPVOTE(1, "点赞"),
    COLLECTION(2, "收藏"),
    VIEW(3, "浏览"),
    RATING(4, "评分");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String detail;

}
