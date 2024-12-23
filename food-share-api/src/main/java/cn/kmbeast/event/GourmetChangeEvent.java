package cn.kmbeast.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 美食变更事件
 */
@Getter
public class GourmetChangeEvent extends ApplicationEvent {
    private final Integer gourmetId;
    
    public GourmetChangeEvent(Object source, Integer gourmetId) {
        super(source);
        this.gourmetId = gourmetId;
    }
}
