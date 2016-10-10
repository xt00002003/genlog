package com.tcl.dao;

import com.tcl.entity.UserHistoryeEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengxue on 16-9-7.
 */
public class UserHistoryDaoImpl {

    public int insertUserHistory(UserHistoryeEntity userHistoryeEntity) throws Exception{
        String sql="insert into test_user_history (user_id,app_id,pkg_name,channel_name,create_time) VALUES (?,?,?,?,?)";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setLong(1, userHistoryeEntity.getUserId());
        db.pst.setLong(2, userHistoryeEntity.getAppId());
        db.pst.setString(3, userHistoryeEntity.getPackName());
        db.pst.setString(4, userHistoryeEntity.getChannelName());
        db.pst.setString(5, userHistoryeEntity.getCreateTime());
        int result= db.pst.executeUpdate();//执行语句，得到结果集
        db.close();//关闭连接
        return result;
    }

    public List<UserHistoryeEntity> selectUserHistoryByUserId(long userId) throws Exception{
        String sql="select app_id,channel_name,pkg_name from test_user_history where user_id=?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setLong(1,userId);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        UserHistoryeEntity userHistoryeEntity;
        List<UserHistoryeEntity> userHistoryeEntityEntityList =new ArrayList<>();
        while (ret.next()) {
            userHistoryeEntity =new UserHistoryeEntity();
            userHistoryeEntity.setAppId(ret.getLong(1));
            userHistoryeEntity.setChannelName(ret.getString(2));
            userHistoryeEntity.setPackName(ret.getString(3));
            userHistoryeEntityEntityList.add(userHistoryeEntity);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return userHistoryeEntityEntityList;
    }
}
