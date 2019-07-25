package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        //1.封装PageBean
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //1.1设置总条数
        int totalCount = routeDao.findTotalCount(cid);
        pageBean.setTotalCount(totalCount);
        //1.2设置总页数
        int totalPage = totalCount%pageSize ==0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        pageBean.setTotalPage(totalPage);
        //1.3设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;   //开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize);
        pageBean.setList(list);

        return pageBean;
    }
}
