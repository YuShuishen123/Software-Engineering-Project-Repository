package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.pojo.vo.DietNutrimentVO;
import cn.kmbeast.service.DietHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 饮食历史的控制器
 */
@RestController
@RequestMapping("/dietHistory")
public class DietHistoryController {

    @Resource
    private DietHistoryService dietHistoryService;

    /**
     * 用户饮食新增
     *
     * @param dietHistories 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody List<DietHistory> dietHistories) {
        return dietHistoryService.save(dietHistories);
    }

    /**
     * 用户饮食修改
     *
     * @param dietHistory 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody DietHistory dietHistory) {
        return dietHistoryService.update(dietHistory);
    }

    /**
     * 用户饮食删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return dietHistoryService.batchDelete(ids);
    }

    /**
     * 查询用户的饮食历史
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<DietHistoryVO>> queryUser(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.queryUser(dietHistoryQueryDto);
    }

    /**
     * 查询饮食历史
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<DietHistoryVO>> query(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.query(dietHistoryQueryDto);
    }

    /**
     * 查询营养摄入情况
     *
     * @param dietQueryDto 查询参数
     * @return Result<List < DietNutrimentVO> > 响应结果
     */
    @PostMapping(value = "/queryDietNutrimentInfo")
    @ResponseBody
    public Result<List<DietNutrimentVO>> queryDietNutrimentInfo(@RequestBody DietQueryDto dietQueryDto) {
        return dietHistoryService.queryDietNutrimentInfo(dietQueryDto);
    }

}

