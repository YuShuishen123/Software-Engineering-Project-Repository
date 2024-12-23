package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.CookbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 食谱的控制器
 */
@RestController
@RequestMapping("/cookbook")
public class CookbookController {

    @Resource
    private CookbookService cookbookService;

    /**
     * 用户新增
     *
     * @param cookbook 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/userSave")
    @ResponseBody
    public Result<String> userSave(@RequestBody Cookbook cookbook) {
        cookbook.setIsPublish(PublishEnum.NO_AUDIT.getFlag());
        return cookbookService.save(cookbook);
    }

    /**
     * 管理员新增食谱
     *
     * @param cookbook 新增实体
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/backSave")
    @ResponseBody
    public Result<String> backSave(@RequestBody Cookbook cookbook) {
        cookbook.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        return cookbookService.save(cookbook);
    }

    /**
     * 食谱新增
     *
     * @param cookbook 新增实体
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Cookbook cookbook) {
        return cookbookService.save(cookbook);
    }

    /**
     * 食谱修改
     *
     * @param cookbook 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Cookbook cookbook) {
        return cookbookService.update(cookbook);
    }

    /**
     * 食谱删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return cookbookService.batchDelete(ids);
    }

    /**
     * 查询食谱下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/querySelectedItems")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedItems() {
        return cookbookService.querySelectedItems();
    }

    /**
     * 查询用户自己食谱的下拉选择器列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/querySelectedItemsUser")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedItemsUser() {
        return cookbookService.querySelectedItemsUser();
    }

    /**
     * 查询用户自己食谱的下拉选择器列表，包括管理员公开出来的
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/querySelectedItemsAll")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedItemsAll() {
        return cookbookService.querySelectedItemsAll();
    }

    /**
     * 查询用户的食谱
     *
     * @param cookbookQueryDto 查询参数
     * @return Result<List < Category>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<CookbookVO>> queryUser(@RequestBody CookbookQueryDto cookbookQueryDto) {
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        return cookbookService.query(cookbookQueryDto);
    }

    /**
     * 查询公开的食谱
     *
     * @param cookbookQueryDto 查询参数
     * @return Result<List < Category>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryPublish")
    @ResponseBody
    public Result<List<CookbookVO>> queryPublish(@RequestBody CookbookQueryDto cookbookQueryDto) {
        cookbookQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        return cookbookService.query(cookbookQueryDto);
    }

    /**
     * 查询食谱
     *
     * @param cookbookQueryDto 查询参数
     * @return Result<List < Category>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<CookbookVO>> query(@RequestBody CookbookQueryDto cookbookQueryDto) {
        return cookbookService.query(cookbookQueryDto);
    }

}

