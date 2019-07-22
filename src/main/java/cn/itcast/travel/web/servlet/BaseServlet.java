package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // System.out.println("BaseServlet的service方法被执行...");
        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();
        //System.out.println("请求uri:"+uri);
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("方法名称:"+methodName);
        //3.获取方法对象Method
        //System.out.println(this);//cn.itcast.travel.web.servlet.UserServlet@38ad0b1f
        try {
            ////忽略方法的访问权限不妥，私有方法也会被访问
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            //暴力反射不妥
            //method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 序列化序列化并响应客户端
     * @param o
     * @param response
     * @throws IOException
     */
    public void writeValue(Object o, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),o);

    }

    /**
     * 序列化并响应客户端
     * @param o
     * @param response
     * @throws IOException
     */
    public void writeValueAsString(Object o, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(o);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /**
     * 校验验证码
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //验证校验码
        String check = request.getParameter("check");
        //从sesion中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //登录失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            writeValueAsString(info,response);
            return;
        }
    }
}
