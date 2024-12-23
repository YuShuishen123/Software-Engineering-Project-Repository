package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.ContentNetMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionStaticQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.ContentNetService;
import cn.kmbeast.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 内容分享业务逻辑接口实现类
 */
@Service
public class ContentNetServiceImpl implements ContentNetService {

    @Resource
    private ContentNetMapper contentNetMapper;
    @Resource
    private GourmetMapper gourmetMapper;

    /**
     * 新增
     *
     * @param contentNet 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> save(ContentNet contentNet) {
        // TODO 添加适当的检验
        // 操作人信息
        contentNet.setUserId(LocalThreadHolder.getUserId());
        // 兼容不需要认证的场景
        if (contentNet.getPasswordAuth() == null){
            contentNet.setPasswordAuth(false);
        }
        // 设置访问码 --- 跟ID一样，唯一
        String accessCode = UUID.randomUUID().toString().toUpperCase(Locale.ROOT);
        contentNet.setAccessCode(accessCode);
        // 设置时间
        contentNet.setCreateTime(LocalDateTime.now());
        contentNetMapper.save(contentNet);
        // 返回访问的链接
        // 部署上云服务器，需要替换这里
        String url = "http://localhost:21091/shareDetail?accessCode=" + accessCode;
        return ApiResult.success(url);
    }

    /**
     * 修改
     *
     * @param contentNet 实体
     * @return Result<String> 通用的响应类
     */
    @Override
    public Result<String> update(ContentNet contentNet) {
        contentNetMapper.update(contentNet);
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
        contentNetMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 查询
     *
     * @param contentNetQueryDto 查询参数实体
     * @return Result<List < ContentNetVO>> 通用的响应类
     */
    @Override
    public Result<List<ContentNetVO>> query(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        Integer totalCount = contentNetMapper.queryCount(contentNetQueryDto);
        return ApiResult.success(contentNetVOS, totalCount);
    }

    /**
     * 通过访问码查询数据
     *
     * @param contentNetQueryDto 新增实体
     * @return Result<Object> 响应结果
     */
    @Override
    public Result<Object> contentNetQueryDto(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        if (CollectionUtils.isEmpty(contentNetVOS)) {
            return ApiResult.success("数据异常");
        }
        ContentNetVO contentNetVO = contentNetVOS.get(0);
        // 是否有效
        LocalDateTime createTime = contentNetVO.getCreateTime();
        LocalDateTime nowTime = LocalDateTime.now();
        long between = ChronoUnit.DAYS.between(nowTime, createTime);
        // 如果不是长期有效的，需要判断有效期
        if (!Objects.equals(-1, contentNetVO.getValidDay())) {
            // 失效了
            if (between > contentNetVO.getValidDay()) {
                return ApiResult.error("链接已经失效了");
            }
        }
        GourmetQueryDto queryDto = new GourmetQueryDto();
        queryDto.setId(contentNetVO.getGourmetId());
        List<GourmetVO> gourmetVOS = gourmetMapper.query(queryDto);
        // 不需要认证
        if (!contentNetVO.getPasswordAuth()) {
            return ApiResult.success(gourmetVOS);
        }
        // 认证密码
        if (!Objects.equals(contentNetVO.getAccessPassword(),
                contentNetQueryDto.getAccessPassword())) {
            return ApiResult.error("密码错误");
        }
        return ApiResult.success(gourmetVOS);
    }

    /**
     * 通过访问码查询是否需要认证
     *
     * @param contentNetQueryDto 新增实体
     * @return Result<Boolean> 响应结果 --- 认证与否
     */
    @Override
    public Result<Boolean> authStatus(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        if (CollectionUtils.isEmpty(contentNetVOS)) {
            return ApiResult.success("数据异常");
        }
        return ApiResult.success(contentNetVOS.get(0).getPasswordAuth());
    }

    /**
     * 按时间统计内容分享量
     *
     * @param contentNetQueryDto 查询参数
     * @return Result<List<ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> daysQuery(ContentNetQueryDto contentNetQueryDto) {
        QueryDto queryDto = DateUtil.startAndEndTime(contentNetQueryDto.getDay());
        contentNetQueryDto.setStartTime(queryDto.getStartTime());
        contentNetQueryDto.setEndTime(queryDto.getEndTime());

        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        List<LocalDateTime> localDateTimes = contentNetVOS.stream()
                .map(ContentNetVO::getCreateTime)
                .collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(
                contentNetQueryDto.getDay(),
                localDateTimes
        );
        return ApiResult.success(chartVOS);
    }
}
