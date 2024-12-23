package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import cn.kmbeast.pojo.vo.SelectedVO;

import java.util.List;

/**
 * 营养素业务逻辑接口
 */
public interface NutrimentService {

    Result<String> save(Nutriment nutriment);

    Result<String> update(Nutriment nutriment);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<NutrimentVO>> query(NutrimentQueryDto nutrimentQueryDto);

    Result<List<SelectedVO>> querySelectedItems();

    Result<List<SelectedVO>> querySelectedItemsUser();


}
