package com.example.util.logger;

/**
 * Created by xuweidong on 2015/9/21.
 */
public class Log {

    static {
        //init the static config here
    }

    public static L getL(String tag){
        return new ALog(tag);
    }

}
