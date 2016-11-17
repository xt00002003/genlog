package com.dark.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tengxue on 16-9-6.
 */
public class PropertiesUtils {

    private static final Properties prop =  new  Properties();
    static  {

        InputStream in = Object.class.getResourceAsStream( "/conf.properties" );
        try  {
            prop.load(in);
        }  catch  (IOException e) {
            e.printStackTrace();
        }
    }

    public static String  getValue(String key){
        return prop.getProperty(key);
    }
}
