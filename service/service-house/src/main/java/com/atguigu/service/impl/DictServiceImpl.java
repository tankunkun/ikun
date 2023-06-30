package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/20 15:44
 * @Author by:Plisetsky
 */
//发布服务，注意导包，指定发布的接口类
@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    DictDao dictDao;
    @Autowired
    JedisPool jedisPool;

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
            //代表一个节点: [{ id:1, pId:0, name:"全部分类", isParent:true}]
            Map<String, Object> map = new HashMap<>();
            map.put("id", dict.getId());
            map.put("pId", dict.getParentId());
            map.put("name", dict.getName());

            //isParent 表示当前节点是否为父节点;有孩子就是父节点 (样式需要)
            Long id = dict.getId();
            Long pId = id;
            //调用Dao层方法判断是否有子节点 (是否为父节点)
            int count = dictDao.countIsParent(pId); //根据pid统计孩子数量
            map.put("isParent", count > 0 ? true : false);

            data.add(map);
        }

        return data;
    }


    //小区管理
    //根据上级id获取子节点数据列表
    @Override
    public List<Dict> findListByParentId(Long parentId) {
        //redis java对象，jedis
        Jedis jedis = jedisPool.getResource();
        try {
            //1.先从缓存中查询，如果有数据直接返回
            String key = "shf:ditc:parentId" + parentId;
            String value = jedis.get(key);
            if (!StringUtils.isEmpty(value)) {
                //因为是返回值实际是Object，所以需要指定类型
                Type listType = new TypeReference<List<Dict>>() {
                }.getType();
                //将json字符串转为List集合
                List<Dict> list = JSON.parseObject(value, listType);
                System.out.println("redis list =" + list);
                return list;
            }

            //2.如果缓存中没有，则查询数据库，并将数据存存放到缓存中
            List<Dict> list2 = dictDao.findZnodesByParentId(parentId);
            if (list2 != null && list2.size() > 0) {
                //转为json格式，进行存储
                jedis.set(key, JSON.toJSONString(list2));
                System.out.println("db list = " + list2);
                return list2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //3.关闭链接
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //根据编码获取子节点数据列表 先获取id，在获取子节点
    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Jedis jedis = jedisPool.getResource();

        try {
            //1.从缓存中查询是否存在
            String key = "shf:ditc:code" + dictCode;
            String value = jedis.get(key);
            if (!StringUtils.isEmpty(value)) {
                //指定类型
                Type type = new TypeReference<List<Dict>>() {
                }.getType();
                //将json字符串转为List集合
                List<Dict> list = JSON.parseObject(value, type);
                System.out.println("redis list = " + list);
                return list;
            }

            //2.如果不存在进行查询，存储到redis内
            //查询操作:通过dictCode 获取dict，再获取子节点
            Dict dict = dictDao.findDictByDictCode(dictCode);
            List<Dict> list2 = dictDao.findZnodesByParentId(dict.getId());//拿主键当外键使用
            if (list2 != null && list2.size() > 0) {
                //转为json格式，进行存储
                String jsonList = JSON.toJSONString(list2);
                jedis.set(key, jsonList);
                System.out.println("db list = " + jsonList);
                return list2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameById(id);
    }

}
