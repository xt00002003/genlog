package com.dark.entity;

/**
 * Created by tengxue on 16-9-7.
 */
public class UserHistoryeEntity {
    private long userId;
    private long appId;
    private String packName;
    private String channelName;
    private String createTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserHistoryeEntity{" + "userId=" + userId + ", appId=" + appId + ", packName='" + packName
                + '\'' + ", channelName='" + channelName + '\'' + ", createTime='" + createTime
                + '\'' + '}';
    }
}
