package com.example.util.android;

import android.content.Context;
import android.os.PowerManager;

import com.example.mydemos.AppApplication;
import com.example.util.logger.L;
import com.example.util.logger.Log;


/**
 * Created by xuweidong on 2015/9/15.
 */
public class WakeLockUtils {
    L logger= Log.getL(WakeLockUtils.class.getName());
    private PowerManager.WakeLock wakeLock = null;

    private static WakeLockUtils instance;
    private int lockTimeMore=0;

    private Context context;

    public WakeLockUtils() {
        this.context = AppApplication.getContext();
    }

    public static WakeLockUtils getInstance(){
        if(instance!=null) {
            return instance;
        }else{
            synchronized(WakeLockUtils.class){
                if(instance==null){
                    instance=new WakeLockUtils();
                }
            }
        }
        return instance;
    }


    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock(Context context)
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLockForSecondUntilNotify(int second)
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                lockTimeMore=second;
                logger.d("device require weakLock");
                wakeLock.acquire();
                new Thread(){
                    @Override
                    public void run() {
                        synchronized (WakeLockUtils.this) {
                            try {
                                while(lockTimeMore!=0) {
                                    logger.d("acquireWakeLockForSecondUntilNotify:WeakLockUtils.this.notifyAll();");
                                    WakeLockUtils.this.notifyAll();
                                    logger.d("acquireWakeLockForSecondUntilNotify:WeakLockUtils.this.wait("+lockTimeMore+");");
                                    WakeLockUtils.this.wait(lockTimeMore * 1000);
                                    logger.d("acquireWakeLockForSecondUntilNotify:go_WeakLockUtils.this.wait("+lockTimeMore+");");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally {
//                                WakeLockUtils.this.notifyAll();
                                releaseWakeLock();
                            }
                        }
                    }
                }.start();
            }
        }
    }

    public void moreWakeLockForSecond(int second){
        synchronized (WakeLockUtils.this){
            if(isWakeLocking()) {
                lockTimeMore=second;
                logger.d("moreWakeLockForSecond:WeakLockUtils.this.notifyAll();");
                WakeLockUtils.this.notifyAll();

                try {
                    logger.d("moreWakeLockForSecond:WeakLockUtils.this.wait();");
                    WakeLockUtils.this.wait();
                    logger.d("moreWakeLockForSecond:go_WeakLockUtils.this.wait();");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lockTimeMore = 0;
                    WakeLockUtils.this.notifyAll();
                }
            }else{
                //启动一个weakLock
                acquireWakeLockForSecondUntilNotify(second);

                try {
                    logger.d("moreWakeLockForSecond:WeakLockUtils.this.wait();");
                    WakeLockUtils.this.wait();
                    logger.d("moreWakeLockForSecond:go_WeakLockUtils.this.wait();");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lockTimeMore = 0;
                }

            }

        }


    }

    public void notifyRealeaseWakeLock(){
        moreWakeLockForSecond(0);
    }
    //释放设备电源锁
    private void releaseWakeLock()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            logger.d("device release weakLock");
            wakeLock = null;
        }
    }

    public boolean isWakeLocking(){
        return wakeLock!=null&&wakeLock.isHeld();
    }

}
