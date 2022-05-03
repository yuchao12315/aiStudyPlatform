package com.csuft.studyplatform.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.csuft.studyplatform.GlideImageLoader;
import com.csuft.studyplatform.PicassoImageLoader;
import com.csuft.studyplatform.VideoException;
import com.csuft.studyplatform.helper.DemoHelper;
import com.csuft.studyplatform.helper.QiYuCloudServerHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.mob.mobapi.MobAPI;
import com.squareup.leakcanary.LeakCanary;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import cn.smssdk.SMSSDK;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;


public class BaseApplication extends Application {

    private static final String MSG_APP_KEY = "35b3799be58d6";// 短信验证的app_key
    private static final String MSG_APP_SECRET = "22513fed999ca675d3b55b78d0a7b3b4";// 短信验证的app_secret
    private static final String MOB_APP_KEY = "1730bae762bbc";// MobApi的应用app_key
    private static final String TAG = "App";

    private static final String MIPUSH_APP_KEY = "5681752153371"; // 小米推送App_key
    private static final String MIPUSH_APP_ID = "2882303761517521371"; //小米推送app_id

    private static BaseApplication sBaseApplication;

    public static BaseApplication getInstance() {
        return sBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = this;


        // LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);


        // 初始化小米推送相关
        initMiPush();

        // 初始化短信验证SDK
        SMSSDK.initSDK(this, MSG_APP_KEY, MSG_APP_SECRET);

        // 初始化MobApiSDK
        MobAPI.initSDK(getApplicationContext(), MOB_APP_KEY);

        //init demo helper
        //TODO:
//        DemoHelper.getInstance().init(BaseApplication.getBaseApplication());

        // 七鱼客服初始化
        QiYuCloudServerHelper.initCloudServer(this);

        // OkGo初始化
        OkGo.init(this);
        OkGo.getInstance().debug("okgo", Level.WARNING,true)
                .setConnectTimeout(20000)  //全局的连接超时时间
                .setReadTimeOut(20000)     //全局的读取超时时间
                .setWriteTimeOut(20000)    //全局的写入超时时间
                .setCookieStore(new PersistentCookieStore());

        // NineGridView的图片加载方式初始化
        NineGridView.setImageLoader(new PicassoImageLoader());
        initImagePicker(); // 初始化ImagePicker


        // 小视频
//        try {
//            // 不知道小视频为什么不可用
//            initSmallVideo(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new VideoException("当前手机暂不支持微视频");
//        }

    }

    public static void initSmallVideo(Context context){
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        VCamera.setDebugMode(true);
        try{
            VCamera.initialize(context);
        } catch (Exception e){
            e.printStackTrace();
            throw new VideoException("当前手机暂不支持微视频");
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


    /**
     * 初始化小米推送相关
     */
    private void initMiPush() {
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, MIPUSH_APP_ID, MIPUSH_APP_KEY);
        }

        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);


    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取Application Context
     */
    public static Context getBaseApplication() {
        return sBaseApplication != null ? sBaseApplication.getApplicationContext() : null;
    }

    public static String currentUserNick = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
