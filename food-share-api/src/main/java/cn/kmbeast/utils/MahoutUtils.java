package cn.kmbeast.utils;

import cn.kmbeast.pojo.dto.query.extend.RatingDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mahout工具类，用以实现协同过滤推荐
 */
@Slf4j
public class MahoutUtils {

    /**
     * 为用户生成指定推荐的用户参数
     *
     * @param ratingDTOS   评分实体，描述的是什么用户（用户ID）对什么物品（物品ID）进行了怎样的评分（分值）
     * @param targetUserId 被推荐者用户ID：向谁推荐的，就是本次调用查看者
     * @param targetItems  推荐的物品条数
     * @return List<RecommendedItem> 返回的推荐列表
     */
    public static List<RecommendedItem> recommender(List<RatingDto> ratingDTOS,
                                                    Long targetUserId,
                                                    int targetItems) {
        List<Preference> preferences = new ArrayList<>();
        for (RatingDto ratingDTO : ratingDTOS) {
            long userId = ratingDTO.getUserId().longValue(); // 确保转换为Long类型
            long itemId = ratingDTO.getItemId().longValue(); // 确保转换为Long类型
            float score = ratingDTO.getScore();
            preferences.add(new GenericPreference(userId, itemId, score));
        }
        try {
            DataModel model = buildDataModel(preferences);
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            return recommender.recommend(targetUserId, targetItems);
        } catch (TasteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static DataModel buildDataModel(List<Preference> preferences) throws TasteException {
        Map<Long, List<Preference>> groupedByUserID = preferences.stream()
                .collect(Collectors.groupingBy(Preference::getUserID));

        FastByIDMap<PreferenceArray> userPreferences = new FastByIDMap<>();
        for (Map.Entry<Long, List<Preference>> entry : groupedByUserID.entrySet()) {
            Long userID = entry.getKey();
            List<Preference> userPrefs = entry.getValue();
            PreferenceArray preferenceArray = new GenericUserPreferenceArray(userPrefs);
            userPreferences.put(userID, preferenceArray);
        }

        return new GenericDataModel(userPreferences);
    }

}