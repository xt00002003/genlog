package com.tcl.dao;

import com.tcl.entity.UserEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengxue on 16-9-6.
 * 从数据库中获取用户信息的dao类
 */
public class UserDaoImpl {


    public List<UserEntity>  selectUserByTime(String createTime,Integer limit) throws Exception{
        //SQL语句
        String sql = "select id,uuid,im,mac,ai,t,c,la,create_time from test_user where create_time=?  limit ?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setString(1,createTime);
        db.pst.setInt(2,limit);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        UserEntity userEntity;
        List<UserEntity> userEntityList=new ArrayList<>();
        while (ret.next()) {
            userEntity=new UserEntity();
            userEntity.setId(ret.getLong(1));
            userEntity.setUuid(ret.getString(2));
            userEntity.setImei(ret.getString(3));
            userEntity.setMac(ret.getString(4));
            userEntity.setAndroidId(ret.getString(5));
            userEntity.setModel(ret.getString(6));
            userEntity.setCountry(ret.getString(7));
            userEntity.setLanguage(ret.getString(8));
            userEntity.setCreateTime(ret.getString(9));
            userEntityList.add(userEntity);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return userEntityList;
    }

    public List<UserEntity>  selectRandomUserByTime(String createTime,Integer limit) throws Exception{
        //SQL语句
        String sql = "select id,uuid,im,mac,ai,t,c,la,create_time from test_user where create_time=?  order by rand() limit ?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setString(1,createTime);
        db.pst.setInt(2,limit);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        UserEntity userEntity;
        List<UserEntity> userEntityList=new ArrayList<>();
        while (ret.next()) {
            userEntity=new UserEntity();
            userEntity.setId(ret.getLong(1));
            userEntity.setUuid(ret.getString(2));
            userEntity.setImei(ret.getString(3));
            userEntity.setMac(ret.getString(4));
            userEntity.setAndroidId(ret.getString(5));
            userEntity.setModel(ret.getString(6));
            userEntity.setCountry(ret.getString(7));
            userEntity.setLanguage(ret.getString(8));
            userEntity.setCreateTime(ret.getString(9));
            userEntityList.add(userEntity);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return userEntityList;
    }

    public int insertUser(UserEntity userEntity)throws Exception{
        String sql="insert into test_user (uuid,im,mac,ai,t,c,la,create_time) VALUES (?,?,?,?,?,?,?,?)";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setString(1, userEntity.getUuid());
        db.pst.setString(2, userEntity.getImei());
        db.pst.setString(3, userEntity.getMac());
        db.pst.setString(4, userEntity.getAndroidId());
        db.pst.setString(5, userEntity.getModel());
        db.pst.setString(6, userEntity.getCountry());
        db.pst.setString(7, userEntity.getLanguage());
        db.pst.setString(8, userEntity.getCreateTime());
        int result= db.pst.executeUpdate();//执行语句，得到结果集
        db.close();//关闭连接
        return result;
    }

}
