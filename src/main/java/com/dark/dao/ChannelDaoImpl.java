package com.dark.dao;

import com.dark.entity.ChannelEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengxue on 16-9-7.
 */
public class ChannelDaoImpl {

    public List<ChannelEntity> selectRandomChannelByTime(Integer limit) throws Exception{
        //SQL语句
        String sql = "select tracker,app_id from channel order by rand() limit ?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setInt(1,limit);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        ChannelEntity channelEntity;
        List<ChannelEntity> channelEntityList=new ArrayList<>();
        while (ret.next()) {
            channelEntity=new ChannelEntity();
            channelEntity.setTracker(ret.getString(1));
            channelEntity.setAppId(ret.getLong(2));
            channelEntityList.add(channelEntity);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return channelEntityList;
    }

    public int  countChannel() throws Exception{
        //SQL语句
        String sql = "select count(*) from channel ";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        int result=0;
        while (ret.next()) {
            result=ret.getInt(1);
        }
        ret.close();
        db.close();//关闭连接
        return result;
    }
}
