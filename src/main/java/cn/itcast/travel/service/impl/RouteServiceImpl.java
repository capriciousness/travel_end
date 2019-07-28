package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();

    //根据cid分页查询旅游线路
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //1.封装PageBean
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //1.1设置总条数
        int totalCount = routeDao.findTotalCount(cid,rname);
        pageBean.setTotalCount(totalCount);
        //1.2设置总页数
        int totalPage = totalCount%pageSize ==0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        pageBean.setTotalPage(totalPage);
        //1.3设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;   //开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize,rname);
        pageBean.setList(list);

        return pageBean;
    }

    //根据rid查询一个完整Route对象
    @Override
    public Route findOne(String rid) {
        //1.根据rid查询一个route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //2.根据route的rid查询一个route_image集合
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //3.根据route的sid查询一个seller商家对象
        Seller seller = sellerDao.findBySid(route.getSid());
        //4.设置route的各项值
        route.setRouteImgList(routeImgList);
        route.setSeller(seller);
        //5.返回一个完整的route对象
        return route;
    }


}
