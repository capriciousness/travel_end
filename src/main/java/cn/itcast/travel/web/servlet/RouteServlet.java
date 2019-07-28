package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    //分页展示分类的旅游线路
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //1.1接收参数线路名称rname
        String rnameStr = request.getParameter("rname");

        //2.参数处理
        String rname = null;
        if(rnameStr!=null && rnameStr.length()>0 && !"null".equals(rnameStr)){
            //tomcat8自动处理了get 请求的乱码，默认为UTF-8,
            //而tomcat7默认为iso-8859-1
            rname = new String(rnameStr.getBytes("iso-8859-1"),"utf-8");
        }
        int cid = 0;
        if(cidStr!=null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;    //当前页码，若没传递，默认第一页
        if(currentPageStr!=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1;
        }
        int pageSize = 0;   //每页显示条数，若没传递，默认五条
        if(pageSizeStr!=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5;
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize,rname);
        //4.将PageBean对象序列化为json，返回给客户端
        writeValue(pageBean,response);

    }

    //查询一个完整route对象
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收线路id,rid
        String rid = request.getParameter("rid");
        //2.调用service查询
        //2.1查询一个route对象
        Route route = routeService.findOne(rid);

        //3.响应数据
        writeValue(route,response);

    }

    //判断用户是否收藏
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收线路id,rid
        String rid = request.getParameter("rid");
        //1.1接收用户id，uid
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;
        if(user != null){
            //用户已登录
            uid = user.getUid();
        }else{
            //用户未登录
            return;
        }
        //2.调用service查询判断
        boolean flag =  favoriteService.isFavorite(rid,uid);

        //3.响应数据
        writeValue(flag,response);

    }

    //添加收藏
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收线路id,rid
        String rid = request.getParameter("rid");
        //1.1接收用户id，uid
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;
        if(user != null){
            //用户已登录
            uid = user.getUid();
        }else{
            //用户未登录
            return;
        }
        //2.调用service查询判断
        favoriteService.add(rid,uid);

    }


}
