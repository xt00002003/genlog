package com.tcl.entity;

/**
 * Created by tengxue on 16-9-7.
 * 这里只需要使用到下面的字段.并不是获取所有的字段
 */
public class AppEntity {
    private long id;
    private String pkgName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    @Override
    public String toString() {
        return "AppEntity{" + "id=" + id + ", pkgName='" + pkgName + '\'' + '}';
    }
}
