package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.CookbookMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.CookbookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食谱业务逻辑接口实现类
 */
@Service
public class CookbookServiceImpl implements CookbookService {

    @Resource
    private CookbookMapper cookbookMapper;

    /**
     * 新增
     *
     * @param cookbook 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(Cookbook cookbook) {
        // 设置发布者
        cookbook.setUserId(LocalThreadHolder.getUserId());
        // 设置发布时间
        cookbook.setCreateTime(LocalDateTime.now());
        cookbookMapper.save(cookbook);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param cookbook 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(Cookbook cookbook) {
        cookbookMapper.update(cookbook);
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
        cookbookMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param cookbookQueryDto 查询参数实体
     * @return Result<List < CookbookVO>> 通用的响应类
     */
    @Override
    public Result<List<CookbookVO>> query(CookbookQueryDto cookbookQueryDto) {
        // 数据
        List<CookbookVO> categoryList = cookbookMapper.query(cookbookQueryDto);
        // 数据量
        Integer totalCount = cookbookMapper.queryCount(cookbookQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 查询食谱下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItems() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        List<CookbookVO> categoryList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> selectedVOS = categoryList.stream().map(
                cookbookVO -> new SelectedVO(
                        cookbookVO.getId(),
                        cookbookVO.getTitle()
                )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }

    /**
     * 查询用户名下食谱下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItemsUser() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<CookbookVO> categoryList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> selectedVOS = categoryList.stream().map(
                cookbookVO -> new SelectedVO(
                        cookbookVO.getId(),
                        cookbookVO.getTitle()
                )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }

    /**
     * 查询用户自己食谱的下拉选择器列表，包括管理员公开出来的
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItemsAll() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<CookbookVO> categoryList = cookbookMapper.query(cookbookQueryDto);
        // 我自己的
        List<SelectedVO> selectedVOS = categoryList.stream().map(
                cookbookVO -> new SelectedVO(
                        cookbookVO.getId(),
                        "私人食谱-" + cookbookVO.getTitle()
                )).collect(Collectors.toList());
        // 需要管理员公开的
        cookbookQueryDto.setUserId(null);
        cookbookQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        List<CookbookVO> publishCookbookList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> publishAll = publishCookbookList.stream().map(
                cookbookVO -> new SelectedVO(
                        cookbookVO.getId(),
                        "公开食谱-" + cookbookVO.getTitle()
                )).collect(Collectors.toList());
        List<SelectedVO> selectedVOList = new ArrayList<>();
        selectedVOList.addAll(selectedVOS);
        selectedVOList.addAll(publishAll);
        return ApiResult.success(selectedVOList);
    }
}
