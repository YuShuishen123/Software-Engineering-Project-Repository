package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;

import java.util.List;

/**
 * 食谱营养素关联业务逻辑接口
 */
public interface CookbookNutrimentService {

    Result<String> save(CookbookNutriment cookbookNutriment);

    Result<String> update(CookbookNutriment cookbookNutriment);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<CookbookNutrimentVO>> query(CookbookNutrimentQueryDto queryDto);

    Result<List<CookbookNutrimentVO>> queryUser();


}
