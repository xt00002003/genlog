package com.tcl.dao;

import com.tcl.entity.AppEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengxue on 16-9-7.
 */
public class AppDaoImpl {

    public List<AppEntity> selectRandomAppByTime(Integer limit) throws Exception{
        //SQL语句
        String sql = "select id,pkg_name from app order by rand() limit ?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setInt(1,limit);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        AppEntity appEntity;
        List<AppEntity> appEntityList=new ArrayList<>();
        while (ret.next()) {
            appEntity=new AppEntity();
            appEntity.setId(ret.getLong(1));
            appEntity.setPkgName(ret.getString(2));
            appEntityList.add(appEntity);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return appEntityList;
    }

    public int  countApp() throws Exception{
        //SQL语句
        String sql = "select count(*) from app ";
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

    public  String  selectPackNameByAppId(long appId) throws Exception{
        //SQL语句
        String sql = "select pkg_name from app where id=?";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        db.pst.setLong(1,appId);
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        if (ret.next()) {
            return ret.getString(1);
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return null;
    }
}
