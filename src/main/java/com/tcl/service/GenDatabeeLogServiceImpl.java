package com.tcl.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tcl.dao.AppDaoImpl;
import com.tcl.dao.ChannelDaoImpl;
import com.tcl.dao.UserDaoImpl;
import com.tcl.dao.UserHistoryDaoImpl;
import com.tcl.entity.AppEntity;
import com.tcl.entity.ChannelEntity;
import com.tcl.entity.UserEntity;
import com.tcl.entity.UserHistoryeEntity;
import com.tcl.utils.LzoCompress;
import com.tcl.utils.OperatingFiles;
import com.tcl.utils.PropertiesUtils;
import com.tcl.vo.Se;
import com.tcl.vo.Us;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tengxue on 16-9-7.
 * 生成日志的业务逻辑类.需要注意：
 * 1.选取当天用户５条
 * 2.查询是否有３天前的用户.如果有则需要选择3个.并从user_history表中获取用户使用以前使用过的记录
 * 3.查询是否有7天前的用户.如果有则需要选择3个.
 * 4.查询是否有30天前的用户.如果有则需要选择3个.
 * 5.从app表中随机获取80%的数据.
 * 6.从channel表中获取80%的数据.
 * 7.所有的时间都是utc时间.示例:2016-09-01 目录中的日志所有的时间都应该是2016-09-01 xx:xx:xx
 *
 */
public class GenDatabeeLogServiceImpl {

    private UserDaoImpl userDao=new UserDaoImpl();
    private AppDaoImpl appDao=new AppDaoImpl();
    private ChannelDaoImpl channelDao=new ChannelDaoImpl();
    private UserHistoryDaoImpl userHistoryDao=new UserHistoryDaoImpl();
    private OperatingFiles ofs = new OperatingFiles();



    public void genLog(String dateTime) throws Exception{
        //查询３天前的用户记录

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        String startDate= toEsTimeString(sdf.parse(dateTime).getTime());
        List<UserEntity> nowUserList=userDao.selectUserByTime(dateTime,5);
        Date start=sdf.parse(dateTime);
        //查询次日的用户记录
//        String before1= toEsTimeString(sdf.parse(DateUtils.getTime(start,-1)).getTime()) ;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        String before1=sdf.format(calendar.getTime());

        List<UserEntity> before1UserList=userDao.selectRandomUserByTime(before1,3);
        //查询3天前的用户记录
        calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH,-3);
//        String before3= toEsTimeString(sdf.parse(DateUtils.getTime(start,-3)).getTime()) ;
        String before3= sdf.format(calendar.getTime()) ;

        List<UserEntity> before3UserList=userDao.selectRandomUserByTime(before3,3);
        //查询7天前的用户记录
        calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH,-7);
//        String before7= toEsTimeString(sdf.parse(DateUtils.getTime(start,-7)).getTime());
        String before7= sdf.format(calendar.getTime()) ;
        List<UserEntity> before7UserList=userDao.selectRandomUserByTime(before7,3);
        //查询30天前的用户记录
        calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH,-30);
//        String before30= toEsTimeString(sdf.parse(DateUtils.getTime(start,-30)).getTime());
        String before30= sdf.format(calendar.getTime()) ;
        List<UserEntity> before30UserList=userDao.selectRandomUserByTime(before30,3);

        int countApp=appDao.countApp();
        //app随机获取的数量
        int limit=(int)Math.ceil(countApp*0.8);
        List<AppEntity> appEntityList=appDao.selectRandomAppByTime(limit);
        int countChannel=channelDao.countChannel();
        limit=(int)Math.ceil(countChannel*0.8);
        List<ChannelEntity> channelEntityList=channelDao.selectRandomChannelByTime(limit);
        int count=0;
        //生成当前的用户日志记录
        StringBuilder sb=new StringBuilder();
        for (UserEntity userEntity:nowUserList){
            for (AppEntity appEntity:appEntityList){
                JSONObject json=new JSONObject();
                JSONArray seArray=new JSONArray();
                int number = new Random().nextInt(10) + 1;
                //随机生成启动次数
                for (int i=0;i<number;i++){
                    seArray.add(genSe());
                }
                json.put("se",seArray);
                //生成设备相关的数据
                Us us=new Us();
                us.setAi(userEntity.getAndroidId());
                us.setIm(userEntity.getImei());
                us.setMac(userEntity.getMac());
                us.setT(userEntity.getModel());
                us.setC(userEntity.getCountry());
                us.setLa(userEntity.getLanguage());
                us.setAppkey(String.valueOf(appEntity.getId()));
                us.setDateTime(dateTime);
                //随机获取一个渠道
                Collections.shuffle(channelEntityList);
                String packName="";
                long appId=0;
                String tracker="";
                if (channelEntityList.size()>0){
                    tracker=channelEntityList.get(0).getTracker();
                    us.setCh(tracker);
                    appId=channelEntityList.get(0).getAppId();
                    packName=appDao.selectPackNameByAppId(appId);
                    us.setPn(packName);
                }else {
                    tracker="default";
                    us.setCh(tracker);
                }
                json.put("us",us);


                sb.append(start.getTime()+number);
                sb.append("|");
                sb.append("127.0.0.1");
                sb.append("|");
                sb.append(json.toJSONString());
                String lineSeparator=System.getProperty("line.separator");
                sb.append(lineSeparator);

                count++;

                //保存每次生成记录的关系
                UserHistoryeEntity userHistoryeEntity =new UserHistoryeEntity();
                userHistoryeEntity.setAppId(appId);
                if (channelEntityList.size()>0){
                    userHistoryeEntity.setChannelName(tracker);
                }else {
                    userHistoryeEntity.setChannelName("default");
                }

                userHistoryeEntity.setCreateTime(dateTime);
                userHistoryeEntity.setPackName(packName);
                userHistoryeEntity.setUserId(userEntity.getId());
                userHistoryDao.insertUserHistory(userHistoryeEntity);

            }
        }

        //次日留存率
        genBeforeData(before1UserList, sb,before1,start);
        //3日留存率
        genBeforeData(before3UserList, sb,before3,start);
        //7日留存率
        genBeforeData(before7UserList, sb,before7,start);
        //30日留存率
        genBeforeData(before30UserList, sb,before30,start);

        System.out.println("生成的记录条数-----------------"+count);
        //选择生成的文件目录
        String localDir=System.getProperty("user.dir")+ PropertiesUtils.getValue("local.path");
        File file=new File(localDir);
        if (!file.exists()){
            file.mkdir();
        }
        String fileName="CheckData."+sdf.parse(dateTime).getTime()+".lzo";

        //写入规定的目录中
        LzoCompress.write2LzoFile(localDir+fileName,LzoCompress.getDefaultConf(),sb.toString().getBytes("utf-8"));
        String lzoFilePath = localDir+fileName;
        String dir=PropertiesUtils.getValue("local.path")+dateTime+"/";
        if (!ofs.exists(dir)){
            ofs.createDir(dir);

        }
        ofs.copyFile(lzoFilePath, dir);
    }

    private Se genSe(){
        int number = new Random().nextInt(10) + 1;
        Se se=new Se();
        se.setS(new Date().getTime());
        se.setE(new Date().getTime()+number);
        return se;
    }

    private void genBeforeData(List<UserEntity> userList, StringBuilder sb,String beforeTime, Date start) throws Exception {

        List<UserHistoryeEntity> userHistoryEntityList;
        for (UserEntity userEntity:userList){
            userHistoryEntityList=userHistoryDao.selectUserHistoryByUserId(userEntity.getId());
            for (UserHistoryeEntity userHistoryeEntity:userHistoryEntityList) {
                JSONObject json = new JSONObject();
                JSONArray seArray = new JSONArray();
                int number = new Random().nextInt(10) + 1;
                //随机生成启动次数
                for (int i = 0; i < number; i++) {
                    seArray.add(genSe());
                }
                json.put("se", seArray);
                //生成设备相关的数据
                Us us = new Us();
                us.setAi(userEntity.getAndroidId());
                us.setIm(userEntity.getImei());
                us.setMac(userEntity.getMac());
                us.setT(userEntity.getModel());
                us.setC(userEntity.getCountry());
                us.setLa(userEntity.getLanguage());
                us.setAppkey(String.valueOf(userHistoryeEntity.getAppId()));
                us.setCh(userHistoryeEntity.getChannelName());
                us.setPn(userHistoryeEntity.getPackName());
                us.setDateTime(beforeTime);
                json.put("us", us);
                sb.append(start.getTime()+number);
                sb.append("|");
                sb.append("127.0.0.1");
                sb.append("|");
                sb.append(json.toJSONString());
                String lineSeparator = System.getProperty("line.separator");
                sb.append(lineSeparator);
            }

        }
    }

    /**
     * 根据传递的时间字符串
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)throws Exception{
        if (args.length < 1) {
            System.exit(1);
        }
        String statDateStr = args[0];
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if (null==statDateStr){
            statDateStr=sdf.format(new Date());
        }
        GenDatabeeLogServiceImpl genDatabeeLogService=new GenDatabeeLogServiceImpl();
        long  between=(new Date().getTime()-sdf.parse(statDateStr).getTime())/1000;//除以1000是为了转换成秒
        int   day;
        if (between>0 && between<24*3600){
            day=1;
        }else {
            day=(int) between/(24*3600);
        }

        Calendar calendar;
        for (int i=0;i<day;i++){
            calendar=Calendar.getInstance();
            calendar.setTime(sdf.parse(statDateStr));
            calendar.add(Calendar.DAY_OF_MONTH,i);
            genDatabeeLogService.genLog(sdf.format(calendar.getTime()));
        }

//        genDatabeeLogService.genLog(statDateStr);

//        long dateTime=1470585600007L;
//        System.out.println("1472918400009L--->"+sdf.format(new Date(dateTime)));

    }

}
