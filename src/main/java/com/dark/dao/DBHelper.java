package com.dark.dao;

import com.dark.utils.PropertiesUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by tengxue on 16-9-6.
 */
public class DBHelper {

    public static final String url = PropertiesUtils.getValue("jdbc.url");
    public static final String name = PropertiesUtils.getValue("jdbc.name");
    public static final String user = PropertiesUtils.getValue("jdbc.user");
    public static final String password = PropertiesUtils.getValue("jdbc.password");

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
