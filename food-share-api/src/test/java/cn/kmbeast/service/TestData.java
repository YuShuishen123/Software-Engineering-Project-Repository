package cn.kmbeast.service;

import cn.kmbeast.pojo.vo.GourmetVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试数据类
 */
public class TestData {
    
    /**
     * 获取测试美食数据
     */
    public static List<GourmetVO> getTestGourmetList() {
        List<GourmetVO> gourmetList = new ArrayList<>();
        
        // 测试用例1：麻婆豆腐
        GourmetVO gourmet1 = new GourmetVO();
        gourmet1.setId(1);
        gourmet1.setTitle("麻婆豆腐");
        gourmet1.setCover("cover_url_1");
        gourmet1.setContent("经典川菜，麻辣鲜香。主料：豆腐、猪肉末。调料：豆瓣酱、花椒、辣椒。");
        gourmet1.setUserName("test_user_1");
        gourmet1.setUserAvatar("avatar_url_1");
        gourmet1.setViewCount(100);
        gourmet1.setUpvoteCount(50);
        gourmet1.setSaveCount(30);
        gourmet1.setRating(4.5);
        gourmet1.setCreateTime(LocalDateTime.now());
        gourmetList.add(gourmet1);
        
        // 测试用例2：宫保鸡丁
        GourmetVO gourmet2 = new GourmetVO();
        gourmet2.setId(2);
        gourmet2.setTitle("宫保鸡丁");
        gourmet2.setCover("cover_url_2");
        gourmet2.setContent("经典川菜，麻辣鲜香。主料：鸡肉、花生。调料：花椒、干辣椒。");
        gourmet2.setUserName("test_user_2");
        gourmet2.setUserAvatar("avatar_url_2");
        gourmet2.setViewCount(150);
        gourmet2.setUpvoteCount(70);
        gourmet2.setSaveCount(40);
        gourmet2.setRating(4.7);
        gourmet2.setCreateTime(LocalDateTime.now());
        gourmetList.add(gourmet2);
        
        // 测试用例3：清炒小白菜
        GourmetVO gourmet3 = new GourmetVO();
        gourmet3.setId(3);
        gourmet3.setTitle("清炒小白菜");
        gourmet3.setCover("cover_url_3");
        gourmet3.setContent("清淡素菜。主料：小白菜。调料：蒜末、盐。");
        gourmet3.setUserName("test_user_3");
        gourmet3.setUserAvatar("avatar_url_3");
        gourmet3.setViewCount(80);
        gourmet3.setUpvoteCount(30);
        gourmet3.setSaveCount(20);
        gourmet3.setRating(4.0);
        gourmet3.setCreateTime(LocalDateTime.now());
        gourmetList.add(gourmet3);
        
        return gourmetList;
    }
    
    /**
     * 获取测试特征向量
     */
    public static String getTestFeatureVector(int type) {
        switch (type) {
            case 1: // 麻婆豆腐的特征向量
                return "{\"川菜\":0.8,\"麻辣\":0.9,\"豆腐\":0.7,\"肉末\":0.6,\"炒菜\":0.5}";
            case 2: // 宫保鸡丁的特征向量
                return "{\"川菜\":0.8,\"麻辣\":0.8,\"鸡肉\":0.7,\"花生\":0.6,\"炒菜\":0.5}";
            case 3: // 清炒小白菜的特征向量
                return "{\"素菜\":0.9,\"清淡\":0.8,\"蔬菜\":0.7,\"炒菜\":0.6}";
            default:
                return "{}";
        }
    }
    
    /**
     * 获取预期的相似度结果
     */
    public static double getExpectedSimilarity(int source, int target) {
        if (source == 1 && target == 2) { // 麻婆豆腐 vs 宫保鸡丁
            return 0.85; // 都是川菜，麻辣，相似度高
        } else if (source == 1 && target == 3) { // 麻婆豆腐 vs 清炒小白菜
            return 0.3; // 一个麻辣一个清淡，相似度低
        } else if (source == 2 && target == 3) { // 宫保鸡丁 vs 清炒小白菜
            return 0.35; // 一个麻辣一个清淡，相似度低
        }
        return 0.0;
    }
}
