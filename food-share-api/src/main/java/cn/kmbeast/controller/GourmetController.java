package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.GourmetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 美食做法的控制器
 */
@RestController
@RequestMapping("/gourmet")
public class GourmetController {

    @Resource
    private GourmetService gourmetService;

    /**
     * 美食做法新增
     *
     * @param gourmet 新增实体
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Gourmet gourmet) {
        return gourmetService.save(gourmet);
    }

    /**
     * 美食做法修改
     *
     * @param gourmet 新增实体
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Gourmet gourmet) {
        return gourmetService.update(gourmet);
    }

    /**
     * 美食做法审核
     *
     * @param id 美食做法ID
     * @return Result<String> 响应结果
     */
    @Protector(role = "管理员")
    @PutMapping(value = "/audit/{id}")
    @ResponseBody
    public Result<String> audit(@PathVariable Integer id) {
        return gourmetService.audit(id);
    }

    /**
     * 美食做法删除
     *
     * @param ids 目标ID列表
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return gourmetService.batchDelete(ids);
    }

    /**
     * 通过ID查找美食做法信息
     *
     * @param id 美食做法ID
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Pager
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<List<GourmetVO>> queryById(@PathVariable Integer id) {
        return gourmetService.queryById(id);
    }

    /**
     * 查询用户名下的美食做法
     *
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @GetMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<GourmetListVO>> queryUser() {
        return gourmetService.queryUser();
    }

    /**
     * 美食查询列表
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryList")
    @ResponseBody
    public Result<List<GourmetListVO>> queryList(@RequestBody GourmetQueryDto gourmetQueryDto) {
        return gourmetService.queryList(gourmetQueryDto);
    }

    /**
     * 查询美食做法
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Pager
    @Protector(role = "管理员")
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<GourmetVO>> query(@RequestBody GourmetQueryDto gourmetQueryDto) {
        return gourmetService.query(gourmetQueryDto);
    }

    /**
     * 通过阅读量查询美食做法数据
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Pager
    @Protector(role = "管理员")
    @PostMapping(value = "/queryByView")
    @ResponseBody
    public Result<List<GourmetVO>> queryByView(@RequestBody GourmetQueryDto gourmetQueryDto) {
        return gourmetService.queryByView(gourmetQueryDto);
    }

    /**
     * 按时间统计美食做法存量
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Protector(role = "管理员")
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> daysQuery(@PathVariable Integer day) {
        return gourmetService.daysQuery(day);
    }

}

