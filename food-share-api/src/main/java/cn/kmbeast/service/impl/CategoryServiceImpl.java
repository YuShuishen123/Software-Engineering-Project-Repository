package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.CategoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 美食类别业务逻辑接口实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 新增
     *
     * @param category 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(Category category) {
        categoryMapper.save(category);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param category 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(Category category) {
        categoryMapper.update(category);
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
        categoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param categoryQueryDto 查询参数实体
     * @return Result<List < Category>> 通用的响应类
     */
    @Override
    public Result<List<Category>> query(CategoryQueryDto categoryQueryDto) {
        // 数据
        List<Category> categoryList = categoryMapper.query(categoryQueryDto);
        // 数据量
        Integer totalCount = categoryMapper.queryCount(categoryQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }
}
