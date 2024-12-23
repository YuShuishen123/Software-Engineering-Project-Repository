package cn.kmbeast.event;

import cn.kmbeast.service.ContentRecommendComputeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 美食变更事件监听器
 */
@Component
@Slf4j
public class GourmetChangeListener {
    @Autowired
    private ContentRecommendComputeService computeService;
    
    /**
     * 异步处理美食变更事件
     */
    @Async
    @EventListener
    public void handleGourmetChange(GourmetChangeEvent event) {
        Integer gourmetId = event.getGourmetId();
        log.info("Handling gourmet change event for id: {}", gourmetId);
        
        try {
            computeService.updateRecommendations(gourmetId);
            log.info("Successfully updated recommendations for gourmet: {}", gourmetId);
        } catch (Exception e) {
            log.error("Failed to handle gourmet change for id: {}", gourmetId, e);
        }
    }
}
