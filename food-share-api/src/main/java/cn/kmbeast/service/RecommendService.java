package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.vo.GourmetVO;

import java.util.List;

public interface RecommendService {

    Result<List<GourmetVO>> recommendGourmet(Integer item);
}
