package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteService {
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname);

    Route findOne(String rid);

}
