package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.*;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.*;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.service.ViewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页可视化
 */
@Service
public class ViewsServiceImpl implements ViewsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private GourmetMapper gourmetMapper;
    @Resource
    private CookbookMapper cookbookMapper;
    @Resource
    private NutrimentMapper nutrimentMapper;
    @Resource
    private ContentNetMapper contentNetMapper;
    @Resource
    private InteractionMapper interactionMapper;

    /**
     * 统计一些系统的基础数据
     *
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> staticControls() {
        List<ChartVO> chartVOS = new ArrayList<>();
        // 用户数
        int userCount = userMapper.queryCount(new UserQueryDto());
        change(userCount, "存量用户", chartVOS);
        // 收录美食做法
        int gourmetCount = gourmetMapper.queryCount(new GourmetQueryDto());
        change(gourmetCount, "美食做法", chartVOS);
        // 收录食谱
        int cookbookCount = cookbookMapper.queryCount(new CookbookQueryDto());
        change(cookbookCount, "收录食谱", chartVOS);
        // 营养模型
        int nutrimentCount = nutrimentMapper.queryCount(new NutrimentQueryDto());
        change(nutrimentCount, "营养模型", chartVOS);
        // 内容分享
        int contentNetCount = contentNetMapper.queryCount(new ContentNetQueryDto());
        change(contentNetCount, "内容分享", chartVOS);
        // 互动量
        int interactionCount = interactionMapper.queryCount(new InteractionQueryDto());
        change(interactionCount, "互动量", chartVOS);
        return ApiResult.success(chartVOS);
    }

    /**
     * 参数处理
     *
     * @param count    总数目
     * @param name     统计项
     * @param chartVOS 装它们的集合
     */
    private void change(Integer count, String name, List<ChartVO> chartVOS) {
        ChartVO chartVO = new ChartVO(name, count);
        chartVOS.add(chartVO);
    }


}
