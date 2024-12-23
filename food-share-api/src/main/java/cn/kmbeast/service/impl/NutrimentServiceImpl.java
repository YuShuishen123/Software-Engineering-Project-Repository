package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.NutrimentMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.NutrimentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营养素业务逻辑接口实现类
 */
@Service
public class NutrimentServiceImpl implements NutrimentService {

    @Resource
    private NutrimentMapper nutrimentMapper;

    /**
     * 新增
     *
     * @param nutriment 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(Nutriment nutriment) {
        // 设置发布时间
        nutriment.setCreateTime(LocalDateTime.now());
        // 设置发布者ID
        nutriment.setUserId(LocalThreadHolder.getUserId());
        nutrimentMapper.save(nutriment);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param nutriment 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(Nutriment nutriment) {
        nutrimentMapper.update(nutriment);
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
        nutrimentMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param nutrimentQueryDto 查询参数实体
     * @return Result<List < NutrimentVO>> 通用的响应类
     */
    @Override
    public Result<List<NutrimentVO>> query(NutrimentQueryDto nutrimentQueryDto) {
        // 数据
        List<NutrimentVO> categoryList = nutrimentMapper.query(nutrimentQueryDto);
        // 数据量
        Integer totalCount = nutrimentMapper.queryCount(nutrimentQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 查询营养素下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItems() {
        NutrimentQueryDto nutrimentQueryDto = new NutrimentQueryDto();
        List<NutrimentVO> nutrimentVOList = nutrimentMapper.query(nutrimentQueryDto);
        List<SelectedVO> selectedVOS = nutrimentVOList.stream().map(
                nutrimentVO -> new SelectedVO(
                        nutrimentVO.getId(),
                        nutrimentVO.getName()
                )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }

    /**
     * 查询用户配置的营养素下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItemsUser() {
        NutrimentQueryDto nutrimentQueryDto = new NutrimentQueryDto();
        nutrimentQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<NutrimentVO> nutrimentVOList = nutrimentMapper.query(nutrimentQueryDto);
        List<SelectedVO> selectedVOS = nutrimentVOList.stream().map(
                nutrimentVO -> new SelectedVO(
                        nutrimentVO.getId(),
                        "私人营养素 - " + nutrimentVO.getName()
                )).collect(Collectors.toList());
        nutrimentQueryDto.setUserId(null);
        nutrimentQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        List<NutrimentVO> nutrimentVOS = nutrimentMapper.query(nutrimentQueryDto);
        List<SelectedVO> selectedVOList = nutrimentVOS.stream().map(
                nutrimentVO -> new SelectedVO(
                        nutrimentVO.getId(),
                        "公开营养素 - " + nutrimentVO.getName()
                )).collect(Collectors.toList());
        List<SelectedVO> voList = new ArrayList<>();
        voList.addAll(selectedVOS);
        voList.addAll(selectedVOList);
        return ApiResult.success(voList);
    }

}
