package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    //分页展示分类条目
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //1.1接收参数线路名称rname
        String rnameStr = request.getParameter("rname");

        //2.参数处理
        String rname = null;
        if(rnameStr!=null && rnameStr.length()>0){
            //tomcat8自动处理了get 请求的乱码，默认为UTF-8,
            //而tomcat7默认为iso-8859-1
            rname = new String(rnameStr.getBytes("iso-8859-1"),"utf-8");
        }
        int cid = 1;
        if(cidStr!=null && cidStr.length()>0){
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
        PageBean<Route> pageBean = service.pageQuery(cid, currentPage, pageSize,rname);
        //4.将PageBean对象序列化为json，返回给客户端
        writeValue(pageBean,response);

    }


}
