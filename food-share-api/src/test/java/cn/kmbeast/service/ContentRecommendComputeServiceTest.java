package cn.kmbeast.service;

import cn.kmbeast.mapper.ContentFeatureMapper;
import cn.kmbeast.mapper.ContentSimilarityMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.entity.ContentFeature;
import cn.kmbeast.pojo.entity.ContentSimilarity;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.impl.ContentRecommendComputeServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ContentRecommendComputeServiceTest {

    @Mock
    private GourmetMapper gourmetMapper;

    @Mock
    private ContentFeatureMapper contentFeatureMapper;

    @Mock
    private ContentSimilarityMapper contentSimilarityMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ContentRecommendComputeServiceImpl computeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void computeAndSaveFeatures_Success() throws Exception {
        // 准备测试数据
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        gourmet.setTitle("美味红烧肉");
        gourmet.setContent("这是一道经典的红烧肉做法");
        gourmet.setCategoryName("肉类");

        // Mock 依赖
        when(gourmetMapper.queryById(eq(gourmetId))).thenReturn(gourmet);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"test\":1.0}");
        when(contentFeatureMapper.insertOrUpdate(any(ContentFeature.class))).thenReturn(1);

        // 执行测试
        String result = computeService.computeAndSaveFeatures(gourmetId);

        // 验证结果
        assertNotNull(result);
        assertEquals("{\"test\":1.0}", result);
        verify(contentFeatureMapper).insertOrUpdate(any(ContentFeature.class));
    }

    @Test
    void computeAndSaveSimilarity_Success() throws Exception {
        // 准备测试数据
        Integer sourceId = 1;
        Integer targetId = 2;
        
        ContentFeature sourceFeature = new ContentFeature();
        sourceFeature.setGourmetId(sourceId);
        sourceFeature.setFeatureVector("{\"feature1\":1.0}");
        sourceFeature.setCreateTime(LocalDateTime.now());
        
        ContentFeature targetFeature = new ContentFeature();
        targetFeature.setGourmetId(targetId);
        targetFeature.setFeatureVector("{\"feature1\":0.8}");
        targetFeature.setCreateTime(LocalDateTime.now());

        Map<String, Double> sourceFeatures = new HashMap<>();
        sourceFeatures.put("feature1", 1.0);
        
        Map<String, Double> targetFeatures = new HashMap<>();
        targetFeatures.put("feature1", 0.8);

        // Mock 依赖
        when(contentFeatureMapper.selectByGourmetId(eq(sourceId))).thenReturn(sourceFeature);
        when(contentFeatureMapper.selectByGourmetId(eq(targetId))).thenReturn(targetFeature);
        when(objectMapper.readValue(eq(sourceFeature.getFeatureVector()), any(TypeReference.class))).thenReturn(sourceFeatures);
        when(objectMapper.readValue(eq(targetFeature.getFeatureVector()), any(TypeReference.class))).thenReturn(targetFeatures);
        when(contentSimilarityMapper.insertOrUpdate(any(ContentSimilarity.class))).thenReturn(1);

        // 执行测试
        BigDecimal similarity = computeService.computeAndSaveSimilarity(sourceId, targetId);

        // 验证结果
        assertNotNull(similarity);
        assertTrue(similarity.compareTo(BigDecimal.ZERO) > 0);
        verify(contentSimilarityMapper).insertOrUpdate(any(ContentSimilarity.class));
    }

    @Test
    void computeAndSaveFeatures_EmptyContent() throws Exception {
        // 准备测试数据
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        // 不设置标题和内容，模拟空内容

        // Mock 依赖
        when(gourmetMapper.queryById(eq(gourmetId))).thenReturn(gourmet);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"default\":1.0}");
        when(contentFeatureMapper.insertOrUpdate(any(ContentFeature.class))).thenReturn(1);

        // 执行测试
        String result = computeService.computeAndSaveFeatures(gourmetId);

        // 验证结果
        assertNotNull(result);
        assertEquals("{\"default\":1.0}", result);
        verify(contentFeatureMapper).insertOrUpdate(any(ContentFeature.class));
    }

    @Test
    void computeAndSaveSimilarity_NoFeatures() {
        // 准备测试数据
        Integer sourceId = 1;
        Integer targetId = 2;

        // Mock 依赖：模拟找不到特征数据
        when(contentFeatureMapper.selectByGourmetId(eq(sourceId))).thenReturn(null);
        when(contentFeatureMapper.selectByGourmetId(eq(targetId))).thenReturn(null);

        // 执行测试
        BigDecimal similarity = computeService.computeAndSaveSimilarity(sourceId, targetId);

        // 验证结果
        assertEquals(BigDecimal.ZERO, similarity);
        verify(contentSimilarityMapper, never()).insertOrUpdate(any(ContentSimilarity.class));
    }
}
