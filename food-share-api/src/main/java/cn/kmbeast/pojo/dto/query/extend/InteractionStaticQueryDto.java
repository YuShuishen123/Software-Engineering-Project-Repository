package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 帖子流量查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionStaticQueryDto extends QueryDto {
    /**
     * 查询条件Dto类
     */
    private InteractionQueryDto interactionQueryDto;
    /**
     * 美食做法的ID列表
     */
    private List<Integer> gourmetIds;
}
