package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService extends BaseService<Dict> {
    //数据字典
    List<Map<String, Object>> findZnodesByParentId(Long parentId);


    //小区管理
    List<Dict> findListByParentId(Long parentId);

    List<Dict> findListByDictCode(String dictCode);

    //暂未使用
    public String getNameById(Long id);
}
