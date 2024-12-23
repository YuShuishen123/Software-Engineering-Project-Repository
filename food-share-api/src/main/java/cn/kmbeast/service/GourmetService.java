package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;

import java.util.List;

/**
 * 美食做法业务逻辑接口
 */
public interface GourmetService {

    Result<String> save(Gourmet gourmet);

    Result<String> update(Gourmet gourmet);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<GourmetVO>> query(GourmetQueryDto gourmetQueryDto);

    Result<String> audit(Integer id);

    Result<List<GourmetListVO>> queryList(GourmetQueryDto gourmetQueryDto);

    Result<List<GourmetVO>> queryById(Integer id);

    Result<List<GourmetListVO>> queryUser();

    Result<List<ChartVO>> daysQuery(Integer day);

    Result<List<GourmetVO>> queryByView(GourmetQueryDto gourmetQueryDto);

}
