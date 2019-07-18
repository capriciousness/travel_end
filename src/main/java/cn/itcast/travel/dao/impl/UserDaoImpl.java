package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }

        return user;
    }

    /**
     * 用户保存
     * @param user
     */
    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //2.执行sql

        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
                );
    }

    /**
     * 根据激活码查询用户
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        //1.定义sql
        String sql = "select * from tab_user where code=? ";
        User user = null;
        //2.执行sql
        try {
            user = template.queryForObject(sql,
                    new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {

        }

        return user;
    }

    /**
     * 修改用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        //1.定义sql
        String sql = "update tab_user set status='Y' where uid=? ";
        try {
            //2.执行sql
            template.update(sql,user.getUid());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


}
