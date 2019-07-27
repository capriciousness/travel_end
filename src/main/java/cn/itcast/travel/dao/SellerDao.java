package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    //查询一个商家对象
    public Seller findByRid(int sid);
}
