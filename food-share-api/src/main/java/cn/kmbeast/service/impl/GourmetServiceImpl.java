package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.GourmetService;
import cn.kmbeast.utils.DateUtil;
import cn.kmbeast.utils.TextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 美食做法业务逻辑接口实现类
 */
@Service
public class GourmetServiceImpl implements GourmetService {

    @Resource
    private GourmetMapper gourmetMapper;

    /**
     * 新增
     *
     * @param gourmet 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(Gourmet gourmet) {
        // 设置美食做法创建时的时间
        gourmet.setCreateTime(LocalDateTime.now());
        // 初始时，未审核
        gourmet.setIsAudit(false);
        // 设置发布者
        gourmet.setUserId(LocalThreadHolder.getUserId());
        gourmetMapper.save(gourmet);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param gourmet 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(Gourmet gourmet) {
        gourmetMapper.update(gourmet);
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
        gourmetMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param gourmetQueryDto 查询参数实体
     * @return Result<List < GourmetVO>> 通用的响应类
     */
    @Override
    public Result<List<GourmetVO>> query(GourmetQueryDto gourmetQueryDto) {
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        Integer totalCount = gourmetMapper.queryCount(gourmetQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * 美食做法审核
     *
     * @param id 美食做法ID
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> audit(Integer id) {
        Gourmet gourmet = new Gourmet();
        gourmet.setId(id);
        // 设置审核状态
        gourmet.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        gourmetMapper.update(gourmet);
        return ApiResult.success("美食做法审核成功");
    }

    /**
     * 美食查询列表
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetListVO>> queryList(GourmetQueryDto gourmetQueryDto) {
        gourmetQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        gourmetQueryDto.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        Integer totalCount = gourmetMapper.queryCount(gourmetQueryDto);
        List<GourmetListVO> gourmetListVOS = categoryList.stream()
                .map(gourmetVO -> new GourmetListVO(
                        gourmetVO.getId(),
                        gourmetVO.getTitle(),
                        gourmetVO.getCover(),
                        TextUtil.parseText(gourmetVO.getContent(), 180),
                        gourmetVO.getUserName(),
                        gourmetVO.getUserAvatar(),
                        gourmetVO.getViewCount(),
                        gourmetVO.getUpvoteCount(),
                        gourmetVO.getSaveCount(),
                        gourmetVO.getRating(),
                        gourmetVO.getCreateTime()
                )).collect(Collectors.toList());
        return ApiResult.success(gourmetListVOS, totalCount);
    }

    /**
     * 通过ID查找美食做法信息
     *
     * @param id 美食做法ID
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetVO>> queryById(Integer id) {
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setId(id);
        gourmetQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        gourmetQueryDto.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        return ApiResult.success(categoryList);
    }

    /**
     * 查询用户名下的美食做法
     *
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetListVO>> queryUser() {
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        List<GourmetListVO> gourmetListVOS = categoryList.stream()
                .map(gourmetVO -> new GourmetListVO(
                        gourmetVO.getId(),
                        gourmetVO.getTitle(),
                        gourmetVO.getCover(),
                        TextUtil.parseText(gourmetVO.getContent(), 180),
                        gourmetVO.getUserName(),
                        gourmetVO.getUserAvatar(),
                        gourmetVO.getViewCount(),
                        gourmetVO.getUpvoteCount(),
                        gourmetVO.getSaveCount(),
                        gourmetVO.getRating(),
                        gourmetVO.getCreateTime()
                )).collect(Collectors.toList());
        return ApiResult.success(gourmetListVOS);
    }

    /**
     * 通过阅读量查询美食做法数据
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Override
    public Result<List<GourmetVO>> queryByView(GourmetQueryDto gourmetQueryDto) {
        List<GourmetVO> gourmetVOS = gourmetMapper.queryByView(gourmetQueryDto);
        return ApiResult.success(gourmetVOS);
    }

    /**
     * 按时间统计美食做法存量
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setStartTime(queryDto.getStartTime());
        gourmetQueryDto.setEndTime(queryDto.getEndTime());
        List<GourmetVO> contentNetVOS = gourmetMapper.query(gourmetQueryDto);
        List<LocalDateTime> localDateTimes = contentNetVOS.stream()
                .map(GourmetVO::getCreateTime)
                .collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(
                day,
                localDateTimes
        );
        return ApiResult.success(chartVOS);
    }
}
