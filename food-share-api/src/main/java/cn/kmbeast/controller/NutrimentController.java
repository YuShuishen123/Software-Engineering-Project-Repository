package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.NutrimentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 营养素的控制器
 */
@RestController
@RequestMapping("/nutriment")
public class NutrimentController {

    @Resource
    private NutrimentService nutrimentService;

    /**
     * 管理员新增营养素
     *
     * @param nutriment 新增实体
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/backSave")
    @ResponseBody
    public Result<String> backSave(@RequestBody Nutriment nutriment) {
        nutriment.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        return nutrimentService.save(nutriment);
    }

    /**
     * 用户新增营养素
     *
     * @param nutriment 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Nutriment nutriment) {
        nutriment.setIsPublish(PublishEnum.NO_AUDIT.getFlag());
        return nutrimentService.save(nutriment);
    }

    /**
     * 营养素修改
     *
     * @param nutriment 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Nutriment nutriment) {
        return nutrimentService.update(nutriment);
    }

    /**
     * 营养素删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return nutrimentService.batchDelete(ids);
    }

    /**
     * 查询营养素下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/querySelectedItems")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedItems() {
        return nutrimentService.querySelectedItems();
    }


    /**
     * 查询用户自己配置营养素下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/querySelectedItemsUser")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedItemsUser() {
        return nutrimentService.querySelectedItemsUser();
    }


    /**
     * 查询营养素
     *
     * @param nutrimentQueryDto 查询参数
     * @return Result<List < NutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<NutrimentVO>> query(@RequestBody NutrimentQueryDto nutrimentQueryDto) {
        return nutrimentService.query(nutrimentQueryDto);
    }

    /**
     * 查询用户自己配置的营养素
     *
     * @param nutrimentQueryDto 查询参数
     * @return Result<List < NutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<NutrimentVO>> queryUser(@RequestBody NutrimentQueryDto nutrimentQueryDto) {
        nutrimentQueryDto.setUserId(LocalThreadHolder.getUserId());
        return nutrimentService.query(nutrimentQueryDto);
    }

}

