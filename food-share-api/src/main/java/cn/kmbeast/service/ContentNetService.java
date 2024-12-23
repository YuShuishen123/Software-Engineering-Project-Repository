package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.pojo.vo.GourmetVO;

import java.util.List;

/**
 * 内容分享业务逻辑接口
 */
public interface ContentNetService {

    Result<String> save(ContentNet contentNet);

    Result<String> update(ContentNet contentNet);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<ContentNetVO>> query(ContentNetQueryDto contentNetQueryDto);

    Result<Object> contentNetQueryDto(ContentNetQueryDto contentNetQueryDto);

    Result<Boolean> authStatus(ContentNetQueryDto contentNetQueryDto);

    Result<List<ChartVO>> daysQuery(ContentNetQueryDto contentNetQueryDto);

}
