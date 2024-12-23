package cn.kmbeast.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文本处理工具类
 */
@Slf4j
public class TextUtils {
    // 停用词集合
    public static final Set<String> STOP_WORDS = new HashSet<>();
    
    // 标点符号正则表达式
    private static final Pattern PUNCTUATION = Pattern.compile("[\\p{P}\\p{S}\\s]+");
    
    /**
     * 初始化方法，加载停用词
     */
    @PostConstruct
    public static void init() {
        try {
            log.info("Loading stop words...");
            ClassPathResource resource = new ClassPathResource("dict/stopwords.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 跳过空行和注释
                    if (line.trim().isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    STOP_WORDS.add(line.trim());
                }
            }
            log.info("Loaded {} stop words successfully", STOP_WORDS.size());
        } catch (Exception e) {
            log.error("Failed to load stop words", e);
            // 加载默认的基础停用词
            STOP_WORDS.addAll(Arrays.asList(
                "的", "了", "和", "是", "就", "都", "而", "及", "与", "着",
                "或", "一个", "没有", "我们", "你们", "他们", "它们", "这个",
                "那个", "这些", "那些", "这样", "那样", "之", "的话", "说",
                "在", "有", "也", "要", "这", "那", "去", "来", "到", "想",
                "看", "听", "让", "但", "但是", "然后", "因为", "所以", "如果",
                "为", "向", "从", "给", "把", "对", "和", "跟", "比", "还",
                
                // 美食相关常用词（不具备区分性的词）
                "食材", "配料", "准备", "材料", "步骤", "制作", "做法", "烹饪",
                "美食", "菜品", "味道", "口感", "特色", "传统", "独特", "美味",
                "可以", "需要", "一些", "一点", "适量", "少许", "适当", "根据",
                "时间", "分钟", "小时", "约", "大概", "差不多", "左右",
                
                // 烹饪动作（作为独立词时）
                "切", "炒", "煮", "炸", "蒸", "煎", "烤", "拌", "放", "加",
                "倒", "调", "搅", "盖", "煲", "焖", "烫", "泡", "腌", "炖",
                
                // 程度词
                "很", "非常", "特别", "十分", "格外", "略微", "稍微", "略",
                
                // 时间词
                "现在", "之后", "然后", "接着", "最后",
                
                // 数量词
                "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
                "个", "些", "多", "少", "半", "块", "片", "条", "只", "克",
                "斤", "两", "公斤", "毫升", "升",
                
                // 其他美食相关常用词
                "如何", "怎么", "怎样", "教程", "方法", "技巧", "诀窍", "步骤",
                "开始", "首先", "先", "再", "又", "最终", "完成"
            ));
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
     * 提取文本中的词语及其频率
     *
     * @param text 输入文本
     * @return 词语及其频率的映射
     */
    public static Map<String, Integer> extractTerms(String text) {
        // 使用jieba分词实现
        return JiebaUtils.extractTerms(text);
    }

    /**
     * 计算两个文本的相似度
     *
     * @param text1 文本1
     * @param text2 文本2
     * @return 相似度分数 (0-1)
     */
    public static double calculateSimilarity(String text1, String text2) {
        if (!StringUtils.hasText(text1) || !StringUtils.hasText(text2)) {
            return 0.0;
        }

        // 1. 提取两个文本的词频
        Map<String, Integer> terms1 = extractTerms(text1);
        Map<String, Integer> terms2 = extractTerms(text2);

        // 2. 获取所有唯一词语
        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(terms1.keySet());
        allTerms.addAll(terms2.keySet());

        // 3. 计算余弦相似度
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String term : allTerms) {
            int freq1 = terms1.getOrDefault(term, 0);
            int freq2 = terms2.getOrDefault(term, 0);

            dotProduct += freq1 * freq2;
            norm1 += freq1 * freq1;
            norm2 += freq2 * freq2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 提取文本的关键词（按词频排序）
     *
     * @param text 输入文本
     * @param topK 返回前K个关键词
     * @return 关键词列表
     */
    public static List<String> extractKeywords(String text, int topK) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }

        // 1. 提取词频
        Map<String, Integer> termFrequency = extractTerms(text);

        // 2. 按词频排序
        return termFrequency.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(topK)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
