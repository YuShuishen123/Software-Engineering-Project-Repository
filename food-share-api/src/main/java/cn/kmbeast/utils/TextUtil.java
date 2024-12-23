package cn.kmbeast.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 文本工具类
 */
public class TextUtil {

    /**
     * 提取富文本里边指定字数
     *
     * @param targetStr  目标文本
     * @param fontNumber 指定字数
     * @return String
     */
    public static String parseText(String targetStr, Integer fontNumber) {
        if (targetStr == null) {
            return null;
        }
        Document document = Jsoup.parse(targetStr);
        String text = document.text();
        if (text.length() < fontNumber) {
            return text;
        }
        return text.substring(0, fontNumber) + "...";
    }

}
