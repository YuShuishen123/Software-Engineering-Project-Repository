package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 美食类别的控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 类别新增
     *
     * @param category 新增实体
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * 类别修改
     *
     * @param category 新增实体
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    /**
     * 类别删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return categoryService.batchDelete(ids);
    }

    /**
     * 查询美食类别
     *
     * @param categoryQueryDto 查询参数
     * @return Result<List < Category>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<Category>> query(@RequestBody CategoryQueryDto categoryQueryDto) {
        return categoryService.query(categoryQueryDto);
    }

}

