package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;


public class BaseServlet extends HttpServlet {
    /**
     * 子类调用父类的service方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // System.out.println("BaseServlet的service方法被执行...");
        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();
        System.out.println("请求uri:"+uri);
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //System.out.println("方法名称:"+methodName);
        //3.获取方法对象Method,注意this，谁调用，就是谁的对象
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
     * 生成验证码
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkCode(HttpServletRequest request,HttpServletResponse response) throws IOException {

            //服务器通知浏览器不要缓存
            response.setHeader("pragma","no-cache");
            response.setHeader("cache-control","no-cache");
            response.setHeader("expires","0");

            //在内存中创建一个长80，宽30的图片，默认黑色背景
            //参数一：长
            //参数二：宽
            //参数三：颜色
            int width = 80;
            int height = 30;
            BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

            //获取画笔
            Graphics g = image.getGraphics();
            //设置画笔颜色为灰色
            g.setColor(Color.GRAY);
            //填充图片
            g.fillRect(0,0, width,height);

            //产生4个随机验证码，12Ey
            String checkCode = getCheckCode();
            //将验证码放入HttpSession中
            request.getSession().setAttribute("CHECKCODE_SERVER",checkCode);

            //设置画笔颜色为黄色
            g.setColor(Color.YELLOW);
            //设置字体的小大
            g.setFont(new Font("黑体",Font.BOLD,24));
            //向图片上写入验证码
            g.drawString(checkCode,15,25);

            //将内存中的图片输出到浏览器
            //参数一：图片对象
            //参数二：图片的格式，如PNG,JPG,GIF
            //参数三：图片输出到哪里去
            ImageIO.write(image,"PNG",response.getOutputStream());
        }
        /**
         * 产生4位随机字符串
         */
        private String getCheckCode() {
            String base = "0123456789ABCDEFGabcdefg";
            int size = base.length();
            Random r = new Random();
            StringBuffer sb = new StringBuffer();
            for(int i=1;i<=4;i++){
                //产生0到size-1的随机值
                int index = r.nextInt(size);
                //在base字符串中获取下标为index的字符
                char c = base.charAt(index);
                //将c放入到StringBuffer中去
                sb.append(c);
            }
            return sb.toString();

    }


    /**
     * 校验验证码
     * @param request
     * @param response
     * @throws IOException
     */
    public void check_Code(HttpServletRequest request,HttpServletResponse response) throws IOException {
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
            //登录失败或者注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            writeValueAsString(info,response);

        }
    }
}
