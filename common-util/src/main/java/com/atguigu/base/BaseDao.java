package com.atguigu.base;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Map;

/**
 * @Date 2022/6/17 14:19
 * @Author by:Plisetsky
 */
//通用的Base接口
public interface BaseDao <T>{

    Integer insert(T t);
    //Serializable 基本数据类型都继承了
    T selectById(Serializable id);

    Integer update(T t);

    void delete(Serializable id);

    Page<T> findPage(Map<String, Object> filters);

}
