package com.dark.service;

import com.dark.dao.CountryServiceImpl;
import com.dark.dao.UserDaoImpl;
import com.dark.entity.CountryEntity;
import com.dark.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by tengxue on 16-9-8.
 */
public class GenUserServiceImpl {

    private CountryServiceImpl countryService=new CountryServiceImpl();
    private UserDaoImpl userDao=new UserDaoImpl();

    private static final String basicChar="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /** 产生一个随机的字符串*/
    public static String RandomString(int length) {
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(basicChar.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 1.每天生成估计的５条记录
     * 2.可以传递时间参数进行用户的生成
     * @param endDate
     */
    public void genUser(String endDate)throws Exception{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        if (null==endDate){
            endDate=sdf.format(new Date());
        }
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        Date startDate=calendar.getTime();
        long between=(startDate.getTime()-sdf.parse(endDate).getTime())/1000;//除以1000是为了转换成秒
        int   day;
        if (between>0 && between<24*3600){
            day=1;
        }else {
            day=(int) between/(24*3600);
        }
        UserEntity userEntity;
        for (int i=0;i<day;i++){
            for (int j=0;j<5;j++){
                //获取时间
                calendar.setTime(sdf.parse(endDate));
                calendar.add(Calendar.DAY_OF_MONTH,i);
                userEntity=new UserEntity();
                userEntity.setCreateTime(sdf.format(calendar.getTime()));
                userEntity.setUuid(UUID.randomUUID().toString());
                userEntity.setImei(RandomString(15));
                userEntity.setMac(RandomString(12));
                userEntity.setAndroidId(RandomString(16));
                userEntity.setModel(RandomString(7));
                CountryEntity countryEntity=countryService.selectRanodmCountry();
                userEntity.setCountry(countryEntity.getCountryCode());
                userEntity.setLanguage(countryEntity.getLanguageCode());
                userDao.insertUser(userEntity);
            }
        }

    }

    public static void main(String[] args)throws Exception{
        GenUserServiceImpl genUserService=new GenUserServiceImpl();
        genUserService.genUser("2016-09-20");


//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        long dateTime=1471622593759L;
//        System.out.println("1471622593759--->"+sdf.format(new Date(dateTime)));
//        System.out.println("1471622593759--->"+ DateTimeUtils.toEsTimeString(dateTime));
//
//        long dateTime2=1471622586000L;
//        System.out.println("1471622586000--->"+sdf.format(new Date(dateTime2)));
//        System.out.println("1471622586000--->"+ DateTimeUtils.toEsTimeString(dateTime2));
//        DateTime x=DateTimeUtils.fromUtcDateString("2016-09-01");
//        String y=DateTimeUtils.toEsTimeString(DateTimeUtils.fromDateString("2016-09-01"));
//        DateTime z=DateTimeUtils.fromEsTimeString(y);
//        System.out.println("1471622586000--->"+ x.toString());
//        System.out.println("1471622586000--->"+ y.toString());
//        System.out.println("1471622586000--->"+ z.toString());
    }

}
