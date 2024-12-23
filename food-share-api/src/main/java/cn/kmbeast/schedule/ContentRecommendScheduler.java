package cn.kmbeast.schedule;

import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.service.ContentRecommendComputeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 内容推荐定时任务
 */
@Component
@Slf4j
public class ContentRecommendScheduler {
    @Autowired
    private ContentRecommendComputeService computeService;
    
    @Autowired
    private GourmetMapper gourmetMapper;
    
    /**
     * 每天凌晨2点执行推荐更新
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateRecommendations() {
        log.info("Starting daily content recommendation update...");
        try {
            List<Integer> allIds = gourmetMapper.getAllIds();
            log.info("Found {} gourmets to update", allIds.size());
            
            computeService.batchUpdateRecommendations(allIds);
            log.info("Daily content recommendation update completed successfully.");
        } catch (Exception e) {
            log.error("Failed to update content recommendations", e);
        }
    }
}
