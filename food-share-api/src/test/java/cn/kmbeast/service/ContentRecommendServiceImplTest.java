package cn.kmbeast.service;

import cn.kmbeast.mapper.ContentSimilarityMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.Result;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ContentRecommendServiceImplTest {

    @InjectMocks
    private ContentRecommendServiceImpl recommendService;

    @Mock
    private ContentSimilarityMapper contentSimilarityMapper;

    @Mock
    private GourmetMapper gourmetMapper;

    @Mock
    private ContentRecommendComputeService computeService;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void recommendSimilar_Success() {
        // 准备测试数据
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(1);
        queryDto.setSize(5);

        ContentSimilarity similarity1 = new ContentSimilarity();
        similarity1.setSourceId(1);
        similarity1.setTargetId(2);
        similarity1.setSimilarity(BigDecimal.valueOf(0.8));
        
        ContentSimilarity similarity2 = new ContentSimilarity();
        similarity2.setSourceId(1);
        similarity2.setTargetId(3);
        similarity2.setSimilarity(BigDecimal.valueOf(0.6));

        List<ContentSimilarity> similarities = Arrays.asList(similarity1, similarity2);

        GourmetVO gourmet1 = new GourmetVO();
        gourmet1.setId(2);
        gourmet1.setTitle("红烧肉");
        gourmet1.setCategoryName("中餐");

        GourmetVO gourmet2 = new GourmetVO();
        gourmet2.setId(3);
        gourmet2.setTitle("糖醋排骨");
        gourmet2.setCategoryName("中餐");

        // Mock依赖
        when(contentSimilarityMapper.selectBySourceId(eq(1), eq(5)))
            .thenReturn(similarities);
        when(gourmetMapper.queryByIds(anySet()))
            .thenReturn(Arrays.asList(gourmet1, gourmet2));

        // 执行测试
        Result<List<ContentRecommendVO>> result = recommendService.recommendSimilar(queryDto);

        // 验证结果
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        
        ContentRecommendVO vo1 = result.getData().get(0);
        assertEquals(2, vo1.getId());
        assertEquals("红烧肉", vo1.getTitle());
        assertEquals("中餐", vo1.getCategoryName());
        assertEquals(BigDecimal.valueOf(0.8), vo1.getSimilarity());
    }

    @Test
    void recommendSimilar_EmptySimilarities_TriggerCompute() {
        // 准备测试数据
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(1);
        queryDto.setSize(5);

        ContentSimilarity similarity = new ContentSimilarity();
        similarity.setSourceId(1);
        similarity.setTargetId(2);
        similarity.setSimilarity(BigDecimal.valueOf(0.8));

        // Mock依赖 - 第一次返回空，计算后返回结果
        when(contentSimilarityMapper.selectBySourceId(eq(1), eq(5)))
            .thenReturn(Collections.emptyList())
            .thenReturn(Collections.singletonList(similarity));

        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(2);
        gourmet.setTitle("红烧肉");
        when(gourmetMapper.queryByIds(anySet()))
            .thenReturn(Collections.singletonList(gourmet));

        // 执行测试
        Result<List<ContentRecommendVO>> result = recommendService.recommendSimilar(queryDto);

        // 验证结果
        assertEquals(200, result.getCode());
        verify(computeService).updateRecommendations(eq(1));
        verify(contentSimilarityMapper, times(2)).selectBySourceId(eq(1), eq(5));
    }

    @Test
    void recommendSimilar_InvalidInput() {
        // 准备测试数据 - 无效的ID
        ContentBasedQueryDto queryDto = new ContentBasedQueryDto();
        queryDto.setGourmetId(-1);

        // 执行测试
        Result<List<ContentRecommendVO>> result = recommendService.recommendSimilar(queryDto);

        // 验证结果
        assertNotEquals(200, result.getCode());
        verify(contentSimilarityMapper, never()).selectBySourceId(anyInt(), anyInt());
    }

    @Test
    void getContentFeatures_Success() {
        // 准备测试数据
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);
        gourmet.setTitle("红烧肉");

        String featureJson = "{\"category_中餐\":1.0,\"title_红烧肉\":0.75}";

        // Mock依赖
        when(gourmetMapper.queryById(gourmetId)).thenReturn(gourmet);
        when(computeService.computeAndSaveFeatures(gourmetId)).thenReturn(featureJson);

        // 执行测试
        Result<Map<String, Double>> result = recommendService.getContentFeatures(gourmetId);

        // 验证结果
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        verify(computeService).computeAndSaveFeatures(eq(gourmetId));
    }

    @Test
    void getContentFeatures_GourmetNotFound() {
        // 准备测试数据
        Integer gourmetId = 1;

        // Mock依赖
        when(gourmetMapper.queryById(gourmetId)).thenReturn(null);

        // 执行测试
        Result<Map<String, Double>> result = recommendService.getContentFeatures(gourmetId);

        // 验证结果
        assertNotEquals(200, result.getCode());
        verify(computeService, never()).computeAndSaveFeatures(anyInt());
    }

    @Test
    void getContentFeatures_ComputeError() {
        // 准备测试数据
        Integer gourmetId = 1;
        GourmetVO gourmet = new GourmetVO();
        gourmet.setId(gourmetId);

        // Mock依赖
        when(gourmetMapper.queryById(gourmetId)).thenReturn(gourmet);
        when(computeService.computeAndSaveFeatures(gourmetId)).thenReturn(null);

        // 执行测试
        Result<Map<String, Double>> result = recommendService.getContentFeatures(gourmetId);

        // 验证结果
        assertNotEquals(200, result.getCode());
    }
}
