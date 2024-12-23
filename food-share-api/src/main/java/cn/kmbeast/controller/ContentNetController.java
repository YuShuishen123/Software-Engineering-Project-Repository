package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.service.ContentNetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容分享的控制器
 */
@RestController
@RequestMapping("/contentNet")
public class ContentNetController {

    @Resource
    private ContentNetService contentNetService;

    /**
     * 通过访问码查询数据
     *
     * @param contentNetQueryDto 新增实体
     * @return Result<Object> 响应结果
     */
    @PostMapping(value = "/findContent")
    @ResponseBody
    public Result<Object> findContent(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.contentNetQueryDto(contentNetQueryDto);
    }

    /**
     * 通过访问码查询是否需要认证
     *
     * @param contentNetQueryDto 新增实体
     * @return Result<Boolean> 响应结果 --- 认证与否
     */
    @PostMapping(value = "/authStatus")
    @ResponseBody
    public Result<Boolean> authStatus(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.authStatus(contentNetQueryDto);
    }


    /**
     * 用户分享内容
     *
     * @param contentNet 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody ContentNet contentNet) {
        return contentNetService.save(contentNet);
    }

    /**
     * 内容分享修改
     *
     * @param contentNet 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody ContentNet contentNet) {
        return contentNetService.update(contentNet);
    }

    /**
     * 内容分享删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return contentNetService.batchDelete(ids);
    }

    /**
     * 查询用户分享出去的美食内容
     *
     * @param contentNetQueryDto 查询参数
     * @return Result<List < ContentNetVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ContentNetVO>> queryUser(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        contentNetQueryDto.setUserId(LocalThreadHolder.getUserId());
        return contentNetService.query(contentNetQueryDto);
    }

    /**
     * 查询美食内容分享
     *
     * @param contentNetQueryDto 查询参数
     * @return Result<List < ContentNetVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<ContentNetVO>> query(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.query(contentNetQueryDto);
    }

    /**
     * 按时间统计内容分享量
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @PostMapping(value = "/daysQuery")
    @ResponseBody
    public Result<List<ChartVO>> daysQuery(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.daysQuery(contentNetQueryDto);
    }

}

