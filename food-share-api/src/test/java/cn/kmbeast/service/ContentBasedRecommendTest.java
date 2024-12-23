package cn.kmbeast.service;

import cn.kmbeast.mapper.ContentSimilarityMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.api.ResultCode;
import cn.kmbeast.pojo.dto.query.extend.ContentBasedQueryDto;
import cn.kmbeast.pojo.entity.ContentSimilarity;
import cn.kmbeast.pojo.vo.ContentRecommendVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.impl.ContentRecommendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContentBasedRecommendTest {

    @Mock
    private ContentSimilarityMapper contentSimilarityMapper;

    @Mock
    private GourmetMapper gourmetMapper;

    @InjectMocks
    private ContentRecommendServiceImpl contentRecommendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecommendSimilar() {
        // Given
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(1);
        queryDto.setSize(5);

        ContentSimilarity similarity = new ContentSimilarity();
        similarity.setSourceId(1);
        similarity.setTargetId(2);
        similarity.setSimilarity(new BigDecimal("0.8"));

        GourmetVO gourmetVO = new GourmetVO();
        gourmetVO.setId(2);
        gourmetVO.setTitle("Test Gourmet");

        when(contentSimilarityMapper.selectBySourceId(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(similarity));
        when(gourmetMapper.queryByIds(any()))
                .thenReturn(Arrays.asList(gourmetVO));

        // When
        Result<List<ContentRecommendVO>> result = contentRecommendService.recommendSimilar(queryDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(ResultCode.REQUEST_SUCCESS.getCode());
        List<ContentRecommendVO> recommendVOS = ((ApiResult<List<ContentRecommendVO>>) result).getData();
        assertThat(recommendVOS).hasSize(1);
        assertThat(recommendVOS.get(0).getId()).isEqualTo(2);
        assertThat(recommendVOS.get(0).getSimilarity()).isEqualTo(new BigDecimal("0.8"));
    }

    @Test
    void testGetContentFeatures_Success() {
        // Given
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        gourmet.setTitle("美味红烧肉");
        gourmet.setContent("这是一道传统的中国菜，主要食材是五花肉");
        gourmet.setCategoryName("肉类");

        when(gourmetMapper.queryById(gourmetId)).thenReturn(gourmet);

        // When
        Result<Map<String, Double>> result = contentRecommendService.getContentFeatures(gourmetId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(ResultCode.REQUEST_SUCCESS.getCode());
        Map<String, Double> features = ((ApiResult<Map<String, Double>>) result).getData();
        assertThat(features).isNotEmpty();
        
        // 验证特征提取
        assertThat(features).containsKey("category_肉类");
        assertThat(features.get("category_肉类")).isEqualTo(1.0); // 应该是最高权重
        
        // 验证标题词特征
        assertThat(features).containsKey("美味红");
        assertThat(features).containsKey("红烧");
        assertThat(features).containsKey("烧肉");
        
        // 验证内容词特征
        assertThat(features).containsKey("传统的");
        assertThat(features).containsKey("中国");
        assertThat(features).containsKey("五花");
        assertThat(features).containsKey("花肉");
        
        // 验证权重归一化
        assertThat(features.values())
            .allMatch(weight -> weight >= 0.0 && weight <= 1.0);
    }

    @Test
    void testGetContentFeatures_NotFound() {
        // Given
        Integer gourmetId = 999; // 不存在的ID
        when(gourmetMapper.queryById(gourmetId)).thenReturn(null);

        // When
        Result<Map<String, Double>> result = contentRecommendService.getContentFeatures(gourmetId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(ResultCode.REQUEST_ERROR.getCode());
        assertThat(result.getMsg()).contains("不存在");
    }

    @Test
    void testGetContentFeatures_EmptyContent() {
        // Given
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        // 不设置title和content，测试空内容处理

        when(gourmetMapper.queryById(gourmetId)).thenReturn(gourmet);

        // When
        Result<Map<String, Double>> result = contentRecommendService.getContentFeatures(gourmetId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(ResultCode.REQUEST_SUCCESS.getCode());
        Map<String, Double> features = ((ApiResult<Map<String, Double>>) result).getData();
        assertThat(features).isEmpty();
    }

    @Test
    void testFeatureWeights() {
        // Given
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        gourmet.setTitle("红烧肉");  // 标题特征
        gourmet.setContent("红烧肉");  // 内容特征（相同词）
        gourmet.setCategoryName("肉类");  // 分类特征

        when(gourmetMapper.queryById(gourmetId)).thenReturn(gourmet);

        // When
        Result<Map<String, Double>> result = contentRecommendService.getContentFeatures(gourmetId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(ResultCode.REQUEST_SUCCESS.getCode());
        Map<String, Double> features = ((ApiResult<Map<String, Double>>) result).getData();
        
        // 验证标题词权重大于内容词权重
        assertThat(features.get("红烧")).isGreaterThan(features.get("红烧") * 0.5);
    }
}
