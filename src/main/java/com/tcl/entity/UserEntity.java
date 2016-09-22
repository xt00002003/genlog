package com.tcl.entity;

/**
 * Created by tengxue on 16-9-6.
 * 用户的实体类
 */
public class UserEntity {
    private Long  id;
    private String uuid;
    private String imei;
    private String mac;
    private String androidId;
    private String model;
    private String country;
    private String language;
    //格式yyyy-MM-dd
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", uuid='" + uuid + '\'' + ", imei='" + imei + '\''
                + ", mac='" + mac + '\'' + ", androidId='" + androidId + '\'' + ", model='" + model
                + '\'' + ", country='" + country + '\'' + ", language='" + language + '\''
                + ", createTime='" + createTime + '\'' + '}';
    }
}
