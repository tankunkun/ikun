package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @Date 2022/6/17 14:31
 * @Author by:Plisetsky
 */
public abstract class BaseServiceImpl<T> implements BaseService<T>{
    //用于获取真实的Dao
    public abstract BaseDao<T> getEntityDao();

    @Override
    public Integer insert(T t) {
        return getEntityDao().insert(t);
    }

    @Override
    public T getById(Serializable id) {
        return getEntityDao().selectById(id);
    }

    @Override
    public void update(T t) {
        getEntityDao().update(t);
    }

    @Override
    public void delete(Serializable id) {
        getEntityDao().delete(id);
    }

    //分页
    @Override
    public PageInfo<T> findPage(Map<String, Object> filters) {
        //使用工具类进行类型装换，设置默认值，解决空指针(访问首页时没有pageNum参数)
        int pageNum = CastUtil.castInt(filters.get("pageNum"),1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"),1);

        //开启分页功能 (注意检查是否进行依赖、mybatis.xml是否配置)
        //将这两个参数，与当前线程(ThreadLocal)进行绑定，传递给dao层
        PageHelper.startPage(pageNum,pageSize);
        // select 语句，会自动追加limit ?,?  (limit startIndex,pageSize)
        // 公式: startIndex = (pageNum-1)*pageSize

        Page<T> page = getEntityDao().findPage(filters);  //传递查询条件参数
        return new PageInfo(page,5);   //5: 显示的导航页数
    }
}
