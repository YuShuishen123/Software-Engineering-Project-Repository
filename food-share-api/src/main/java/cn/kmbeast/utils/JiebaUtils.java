package cn.kmbeast.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Jieba分词工具类
 */
@Slf4j
@Component
public class JiebaUtils {
    private static final JiebaSegmenter segmenter = new JiebaSegmenter();
    
    // 使用TextUtils中的停用词集合
    private static final Set<String> STOP_WORDS = TextUtils.STOP_WORDS;
    
    /**
     * 初始化方法，预热分词器
     */
    @PostConstruct
    public void init() {
        try {
            log.info("Initializing Jieba segmenter...");
            // 预热分词器
            segmenter.process("美食分享测试文本", JiebaSegmenter.SegMode.SEARCH);
            log.info("Jieba segmenter initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Jieba segmenter", e);
        }
    }
    
    /**
     * 清理HTML标签和特殊字符
     */
    private static String cleanText(String text) {
        if (!StringUtils.hasText(text)) {
            return "";
        }
        // 使用Jsoup清理HTML标签
        String cleaned = Jsoup.clean(text, Safelist.none());
        // 移除特殊字符和标点符号
        cleaned = cleaned.replaceAll("[\\p{P}\\p{S}\\s]+", " ").trim();
        return cleaned;
    }
    
    /**
     * 使用jieba分词提取关键词及其频率
     *
     * @param text 输入文本
     * @return 词语及其频率的映射
     */
    public static Map<String, Integer> extractTerms(String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyMap();
        }

        try {
            // 1. 清理文本
            String cleanedText = cleanText(text);
            if (cleanedText.isEmpty()) {
                return Collections.emptyMap();
            }

            // 2. 使用jieba进行分词
            List<SegToken> tokens = segmenter.process(cleanedText, JiebaSegmenter.SegMode.SEARCH);
            
            // 3. 过滤并统计词频
            return tokens.stream()
                .map(token -> token.word)
                .filter(word -> !isStopWord(word))
                // 只保留2-4个字的词，1个字的词通常没有足够的区分性
                .filter(word -> word.length() >= 2 && word.length() <= 4)
                .collect(Collectors.groupingBy(
                    word -> word,
                    Collectors.collectingAndThen(
                        Collectors.counting(),
                        Long::intValue
                    )
                ));
        } catch (Exception e) {
            log.error("Failed to extract terms from text: {}", text, e);
            return Collections.emptyMap();
        }
    }
    
    /**
     * 检查是否为停用词
     *
     * @param term 待检查的词语
     * @return 如果是停用词返回true，否则返回false
     */
    public static boolean isStopWord(String term) {
        return term == null || term.trim().isEmpty() || STOP_WORDS.contains(term);
    }

    /**
     * 加载自定义词典
     *
     * @param dictPath 词典文件路径
     */
    public static void loadUserDict(String dictPath) {
        try {
            log.info("Loading user dictionary from: {}", dictPath);
            Path path = Paths.get(dictPath);
            WordDictionary.getInstance().loadUserDict(path);
            log.info("User dictionary loaded successfully");
        } catch (Exception e) {
            log.error("Failed to load user dictionary from: {}", dictPath, e);
        }
    }
}
