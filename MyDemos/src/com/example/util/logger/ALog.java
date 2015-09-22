package com.example.util.logger;

import android.util.Log;

/**
 * android log
 * Created by xuweidong on 2015/9/21.
 */
public class ALog implements L {
    private String tag;
    private Level level = Level.DEBUG;

    public ALog(String tag){
        this.tag=tag;
    }

    @Override
    public void setLevel(Level level) {
        this.level=level;
    }

    @Override
    public void d(String msg) {
        if(Level.DEBUG.value<=level.value) {
            Log.d(tag, msg);
        }
    }

    @Override
    public void e(String msg) {
        if(Level.ERROR.value<=level.value) {
            Log.e(tag,msg);
        }

    }

    @Override
    public void i(String msg) {
        if(Level.INFO.value<=level.value) {
            Log.i(tag, msg);
        }
    }

    @Override
    public void w(String msg) {
        if(Level.WARM.value<=level.value) {
            Log.w(tag, msg);
        }

    }
}
