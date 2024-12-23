package cn.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 饮食营养素摄入情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietNutrimentVO {
    /**
     * 营养素ID
     */
    private Integer nutrimentId;
    /**
     * 营养素名称
     */
    private String nutrimentName;
    /**
     * 总的含量值
     */
    private Integer value;
    /**
     * 时间
     */
    private String time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DietNutrimentVO that = (DietNutrimentVO) o;
        return nutrimentId == that.nutrimentId && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nutrimentId, time);
    }
}
