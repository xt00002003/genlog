package com.dark.dao;

import com.dark.entity.CountryEntity;

import java.sql.ResultSet;

/**
 * Created by tengxue on 16-9-8.
 */
public class CountryServiceImpl {
    public CountryEntity selectRanodmCountry() throws Exception{
        //SQL语句
        String sql = "select country_code,language_code from test_country order by rand() LIMIT 1";
        DBHelper db = new DBHelper(sql);//创建DBHelper对象
        ResultSet ret = db.pst.executeQuery();//执行语句，得到结果集
        CountryEntity countryEntity=new CountryEntity();
        while (ret.next()) {
            countryEntity.setCountryCode(ret.getString(1));
            countryEntity.setLanguageCode(ret.getString(2));
        }//显示数据
        ret.close();
        db.close();//关闭连接
        return countryEntity;
    }
}
