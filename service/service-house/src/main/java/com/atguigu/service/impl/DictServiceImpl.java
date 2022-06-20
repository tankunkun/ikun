package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/20 15:44
 * @Author by:Plisetsky
 */
@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    DictDao dictDao;

    @Override
    public BaseDao<Dict> getEntityDao() {
        return dictDao;
    }

    @Override
    public List<Map<String, Object>> findZnodesByParentId(Long parentId) {
        //返回的是list，需要转换为map。虽然可以返回map类型的集合，但是需要做业务处理，所以还是返回为实体对象
        List<Dict> list = dictDao.findZnodesByParentId(parentId);

        //需要进行类型转换
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        for (Dict dict : list) {
            Map<String, Object> map = new HashMap<>();//代表一个节点
            map.put("id",dict.getId());
            map.put("pId",dict.getParentId());
            map.put("name",dict.getName());

            //isParent 表示当前节点是否为父节点;有孩子就是父节点 (样式需要)
            Long id = dict.getId();
            Long pId = id;
            int count = dictDao.countIsParent(pId); //根据pid统计孩子数量
            map.put("isParent", count > 0 ? true : false);


            data.add(map);
        }

        return data;
    }


    //
    @Override
    public List<Dict> findListByParentId(Long parentId) {
        return dictDao.findZnodesByParentId(parentId);
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict  = dictDao.findDictByDictCode(dictCode);
        List<Dict> list = dictDao.findZnodesByParentId(dict.getId());//拿主键当外键使用
        return list;
    }

    //未使用
    public String getNameById(Long id){
        return dictDao.getNameById(id);
    }

}
