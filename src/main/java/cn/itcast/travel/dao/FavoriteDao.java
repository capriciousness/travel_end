package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    public Favorite findByRidAndUid(int rid,int uid);

    public int findFavoriteCount(int rid);

    void add(int parseInt, int uid);
}
