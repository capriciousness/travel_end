package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询总条数
     *
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        // String sql = "select count(*) from tab_route where cid=? ";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();  //参数集合，即查询条件
        //2.判断参数是否有值，拼接sql
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);    //添加查询条件
        }
        if(rname!=null && rname.length()>0){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");  //添加查询条件,注意此处模糊查询
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class,params.toArray());
    }

    /**
     * 查询当前页数据集合
     *
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //String sql = "select * from tab_route where cid=? limit ? ,? ";
        //1.定义sql模板
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();  //参数集合，即查询条件
        //2.判断参数是否有值，拼接sql
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);    //添加查询条件
        }
        if(rname!=null && rname.length()>0){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");  //添加查询条件,注意此处模糊查询
        }
        sb.append(" limit ? ,? ");   //分页查询的参数
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),
                params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        //1.定义sql语句
        String sql = "select * from tab_route where rid = ? ";
        //2.执行sql
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

}
