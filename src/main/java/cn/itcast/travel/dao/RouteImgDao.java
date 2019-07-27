package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //查询一个routeimg集合
    public List<RouteImg> findByRid(int rid);
}
