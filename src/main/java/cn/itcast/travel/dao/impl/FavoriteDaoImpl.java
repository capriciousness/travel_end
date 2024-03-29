package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    //根据uid、rid查询Favorite对象
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        String sql = " select * from tab_favorite where rid=? and uid=? ";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {

        }
        return favorite;
    }

    @Override
    public int findFavoriteCount(int rid) {
        String sql = " select count(*) from tab_favorite where rid=? ";
        int count = 0;
        try {
            count = template.queryForObject(sql, Integer.class, rid);
        } catch (DataAccessException e) {

        }
        return count;
    }

    //添加收藏
    @Override
    public void add(int rid, int uid) {
        String sql = " insert into tab_favorite values(?,?,?) ";
        template.update(sql,rid,new Date(),uid);
    }
}
