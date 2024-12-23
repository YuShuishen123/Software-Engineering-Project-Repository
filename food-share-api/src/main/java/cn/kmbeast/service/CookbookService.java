package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.pojo.vo.SelectedVO;

import java.util.List;

/**
 * 食谱业务逻辑接口
 */
public interface CookbookService {

    Result<String> save(Cookbook cookbook);

    Result<String> update(Cookbook cookbook);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<CookbookVO>> query(CookbookQueryDto cookbookQueryDto);

    Result<List<SelectedVO>> querySelectedItems();

    Result<List<SelectedVO>> querySelectedItemsUser();

    Result<List<SelectedVO>> querySelectedItemsAll();



}
