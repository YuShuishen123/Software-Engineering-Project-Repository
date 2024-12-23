package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.CookbookMapper;
import cn.kmbeast.mapper.CookbookNutrimentMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.service.CookbookNutrimentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食谱营养素关联业务逻辑接口实现类
 */
@Service
public class CookbookNutrimentServiceImpl implements CookbookNutrimentService {

    @Resource
    private CookbookNutrimentMapper cookbookNutrimentMapper;
    @Resource
    private CookbookMapper cookbookMapper;

    /**
     * 新增
     *
     * @param cookbookNutriment 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(CookbookNutriment cookbookNutriment) {
        // 只能是数字
        if (!StringUtils.hasText(cookbookNutriment.getG100Value())){
            return ApiResult.error("值含量不能为空");
        }
        CookbookNutrimentQueryDto queryDto = new CookbookNutrimentQueryDto();
        BeanUtils.copyProperties(cookbookNutriment, queryDto);
        Integer queryCount = cookbookNutrimentMapper.queryCount(queryDto);
        if (queryCount != 0) {
            return ApiResult.error("配置已存在");
        }
        cookbookNutrimentMapper.save(cookbookNutriment);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param cookbookNutriment 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(CookbookNutriment cookbookNutriment) {
        cookbookNutrimentMapper.update(cookbookNutriment);
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
        cookbookNutrimentMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param queryDto 查询参数实体
     * @return Result<List < CookbookNutrimentVO>> 通用的响应类
     */
    @Override
    public Result<List<CookbookNutrimentVO>> query(CookbookNutrimentQueryDto queryDto) {
        // 数据
        List<CookbookNutrimentVO> categoryList = cookbookNutrimentMapper.query(queryDto);
        // 数据量
        Integer totalCount = cookbookNutrimentMapper.queryCount(queryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 查询用户自己食谱的配置信息
     *
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Override
    public Result<List<CookbookNutrimentVO>> queryUser() {
        // 通过自己的ID，查询自己名下的食谱
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        // 我自己的食谱
        List<CookbookVO> cookbookVOS = cookbookMapper.query(cookbookQueryDto);
        // 拿到ID的列表
        List<Integer> cookbookIds = cookbookVOS.stream()
                .map(CookbookVO::getId).collect(Collectors.toList());
        List<CookbookNutrimentVO> cookbookNutrimentVOS
                = cookbookNutrimentMapper.queryByCookbookIds(cookbookIds);
        return ApiResult.success(cookbookNutrimentVOS);
    }
}
