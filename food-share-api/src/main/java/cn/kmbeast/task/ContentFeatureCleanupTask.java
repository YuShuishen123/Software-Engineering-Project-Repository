package cn.kmbeast.task;

import cn.kmbeast.mapper.ContentFeatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定期清理过期的特征向量
 */
@Component
public class ContentFeatureCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(ContentFeatureCleanupTask.class);

    @Autowired
    private ContentFeatureMapper contentFeatureMapper;

    /**
     * 每天凌晨2点执行清理任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredFeatures() {
        log.info("Starting cleanup of expired content features");
        try {
            LocalDateTime expiryTime = LocalDateTime.now().minusDays(7);
            int count = contentFeatureMapper.deleteExpiredFeatures(expiryTime);
            log.info("Cleaned up {} expired content features", count);
        } catch (Exception e) {
            log.error("Failed to cleanup expired content features", e);
        }
    }
}
