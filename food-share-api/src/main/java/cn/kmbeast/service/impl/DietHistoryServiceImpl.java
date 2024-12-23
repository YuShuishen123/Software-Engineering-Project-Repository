package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.DietHistoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.pojo.vo.DietNutrimentVO;
import cn.kmbeast.service.DietHistoryService;
import cn.kmbeast.utils.DateUtil;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户饮食业务逻辑接口实现类
 */
@Service
public class DietHistoryServiceImpl implements DietHistoryService {

    @Resource
    private DietHistoryMapper dietHistoryMapper;

    /**
     * 新增
     *
     * @param dietHistories 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(List<DietHistory> dietHistories) {
        LocalDateTime dateTime = LocalDateTime.now();
        Integer userId = LocalThreadHolder.getUserId();
        for (DietHistory dietHistory : dietHistories) {
            dietHistory.setUserId(userId);
            dietHistory.setCreateTime(dateTime);
        }
        dietHistoryMapper.save(dietHistories);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param dietHistory 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(DietHistory dietHistory) {
        dietHistoryMapper.update(dietHistory);
        return ApiResult.success();
    }

    /**
     * 删除
     *
     * @param ids ID列表
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        dietHistoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param dietHistoryQueryDto 查询参数实体
     * @return Result<List < DietHistoryVO>> 通用的响应类
     */
    @Override
    public Result<List<DietHistoryVO>> query(DietHistoryQueryDto dietHistoryQueryDto) {
        List<DietHistoryVO> categoryList = dietHistoryMapper.query(dietHistoryQueryDto);
        Integer totalCount = dietHistoryMapper.queryCount(dietHistoryQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 查询用户的饮食历史
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Override
    public Result<List<DietHistoryVO>> queryUser(DietHistoryQueryDto dietHistoryQueryDto) {
        dietHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<DietHistoryVO> dietHistoryVOS = dietHistoryMapper.query(dietHistoryQueryDto);
        Integer totalCount = dietHistoryMapper.queryCount(dietHistoryQueryDto);
        return ApiResult.success(dietHistoryVOS, totalCount);
    }

    /**
     * 查询营养摄入情况
     *
     * @param dietQueryDto 查询参数
     * @return Result<List < DietNutrimentVO> > 响应结果
     */
    @Override
    public Result<List<DietNutrimentVO>> queryDietNutrimentInfo(DietQueryDto dietQueryDto) {
        DietHistoryQueryDto dietHistoryQueryDto = createDietHistoryQueryDto(dietQueryDto);
        List<DietHistoryVO> historyVOS = dietHistoryMapper.query(dietHistoryQueryDto);
        List<DietNutrimentVO> dietNutrimentVOS = parseNutriment(historyVOS, dietQueryDto);
        Collections.reverse(dietNutrimentVOS);
        return ApiResult.success(dietNutrimentVOS);
    }

    /**
     * 由传入的查询条件，组装成数据库查询需要的参数对象信息
     *
     * @param dietQueryDto 原始查询参数对象
     * @return DietHistoryQueryDto
     */
    private DietHistoryQueryDto createDietHistoryQueryDto(DietQueryDto dietQueryDto) {
        Integer day = dietQueryDto.getDay();
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        LocalDateTime startTime = queryDto.getStartTime();
        LocalDateTime endTime = queryDto.getEndTime();
        DietHistoryQueryDto dietHistoryQueryDto = new DietHistoryQueryDto();
        dietHistoryQueryDto.setStartTime(startTime);
        dietHistoryQueryDto.setEndTime(endTime);
        BeanUtils.copyProperties(dietQueryDto, dietHistoryQueryDto);
        return dietHistoryQueryDto;
    }

    /**
     * 转换成折线图需要的数据
     *
     * @param historyVOS 数据库原始数据列表
     * @return List<DietNutrimentVO>
     */
    private List<DietNutrimentVO> parseNutriment(List<DietHistoryVO> historyVOS,
                                                 DietQueryDto dietQueryDto) {
        List<DietNutrimentVO> dietNutrimentVOS = new ArrayList<>();
        for (DietHistoryVO historyVO : historyVOS) {
            if (!StringUtils.hasText(historyVO.getNutrimentName())) {
                continue;
            }
            DietNutrimentVO dietNutrimentVO = createDietNutrimentVO(historyVO);
            dietNutrimentVOS.add(dietNutrimentVO);
        }
        selectedNutrimentList(dietNutrimentVOS, dietQueryDto);
        return diffSum(dietNutrimentVOS);
    }

    /**
     * 通过营养素ID以及时间去重，去重之前累加营养摄入值
     *
     * @param dietNutrimentVOS 原始待处理营养素摄入信息列表
     * @return List<DietNutrimentVO>
     */
    public List<DietNutrimentVO> diffSum(List<DietNutrimentVO> dietNutrimentVOS) {
        Map<DietNutrimentVO, Integer> intermediateMap = dietNutrimentVOS.stream()
                .collect(Collectors.toMap(
                        Function.identity(), // 使用DietNutrimentVO对象自身作为键
                        DietNutrimentVO::getValue, // 获取value作为初始值
                        Integer::sum // 累加value
                ));
        // 由于DietNutrimentVO是不可变的，并且我们重写了equals和hashCode，
        // 中间Map的键实际上是唯一的DietNutrimentVO对象，其value字段已经被累加。
        // 由Map转回List
        return intermediateMap.entrySet().stream()
                .map(entry -> new DietNutrimentVO(entry.getKey().getNutrimentId(), entry.getKey().getNutrimentName(), entry.getValue(), entry.getKey().getTime()))
                .collect(Collectors.toList());
    }

    /**
     * 组装成新的营养素摄入出参对象
     *
     * @param dietHistoryVO 原始对象
     * @return DietNutrimentVO
     */
    private DietNutrimentVO createDietNutrimentVO(DietHistoryVO dietHistoryVO) {
        DietNutrimentVO dietNutrimentVO = new DietNutrimentVO();
        String g100Value = dietHistoryVO.getG100Value();
        String value = dietHistoryVO.getValue();
        double inputValue = Double.parseDouble(value);
        double sumInput = (inputValue / 100) * Double.parseDouble(g100Value);
        String recordTime = dietHistoryVO.getCreateTime().toString().substring(5, 10);
        dietNutrimentVO.setNutrimentId(dietHistoryVO.getNutrimentId());
        dietNutrimentVO.setValue((int) sumInput);
        dietNutrimentVO.setNutrimentName(dietHistoryVO.getNutrimentName());
        dietNutrimentVO.setTime(recordTime);
        return dietNutrimentVO;
    }

    /**
     * 通过营养素ID，选择需要的饮食摄入数据
     *
     * @param dietNutrimentVOS 原始数据
     * @param dietQueryDto     接收查询条件
     */
    private void selectedNutrimentList(List<DietNutrimentVO> dietNutrimentVOS,
                                       DietQueryDto dietQueryDto) {
        if (dietQueryDto.getNutrimentId() != null) {
            List<DietNutrimentVO> nutrimentVOS = dietNutrimentVOS.stream()
                    .filter(dietNutrimentVO -> Objects.equals(
                            dietNutrimentVO.getNutrimentId(),
                            dietQueryDto.getNutrimentId()
                    ))
                    .collect(Collectors.toList());
            dietNutrimentVOS.clear();
            dietNutrimentVOS.addAll(nutrimentVOS);
        }
    }

}
