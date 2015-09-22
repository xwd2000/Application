package com.example.util.logger;

/**
 * Created by xuweidong on 2015/9/21.
 */
public interface L {
    public enum Level{
        DEBUG(4),
        INFO(3),
        WARM(2),
        ERROR(1);

        int value;
        private Level(int level){
            this.value=level;
        }
    }

    public void setLevel(Level level);

    public void d(String msg);
    public void e(String msg);
    public void i(String msg);
    public void w(String msg);
}
