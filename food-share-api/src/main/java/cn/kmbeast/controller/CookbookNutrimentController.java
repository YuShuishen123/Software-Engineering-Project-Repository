package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import cn.kmbeast.service.CookbookNutrimentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 食谱营养素关联的控制器
 */
@RestController
@RequestMapping("/cookbookNutriment")
public class CookbookNutrimentController {

    @Resource
    private CookbookNutrimentService cookbookNutrimentService;


    /**
     * 食谱营养素关联新增
     *
     * @param cookbookNutriment 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody CookbookNutriment cookbookNutriment) {
        return cookbookNutrimentService.save(cookbookNutriment);
    }

    /**
     * 食谱营养素关联修改
     *
     * @param cookbookNutriment 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody CookbookNutriment cookbookNutriment) {
        return cookbookNutrimentService.update(cookbookNutriment);
    }

    /**
     * 食谱营养素关联删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return cookbookNutrimentService.batchDelete(ids);
    }

    /**
     * 查询食谱营养素关联
     *
     * @param queryDto 查询参数
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<CookbookNutrimentVO>> query(@RequestBody CookbookNutrimentQueryDto queryDto) {
        return cookbookNutrimentService.query(queryDto);
    }

    /**
     * 查询用户自己食谱的配置信息
     *
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<CookbookNutrimentVO>> queryUser() {
        return cookbookNutrimentService.queryUser();
    }

}

