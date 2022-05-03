package com.csuft.studyplatform.base;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.csuft.studyplatform.helper.QiYuCloudServerHelper;
import com.qiyukf.nimlib.sdk.NimIntent;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;
    private Dialog mDialog;
    private static long mLastClickTime;
    private boolean isDestroyed = false;
    private static final String TAG = "ActivityBase";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isDestroyed = false;
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //==========================================
        //ColorMatrix cm = new ColorMatrix();
        //cm.setSaturation(0);
        //Paint paint = new Paint();
        //paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //View contentContainer = getWindow().getDecorView();
        //contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
        //===========================================
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract void initPresenter();

    /**
     * 需要的时候覆写
     */
    protected void initEvent() {

    }

    protected abstract void initView();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        // 禁用通知
        QiYuCloudServerHelper.disableNotify();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        QiYuCloudServerHelper.enableNotify(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
        if (mDialog != null){
            mDialog.cancel();
            mDialog = null;
        }
        if (inputMethodManager != null){
            inputMethodManager = null;
        }
        if(mBind != null) {
            mBind.unbind();
        }
        this.release();
    }

    /**
     * 子类需要释放资源，覆盖即可
     */
    protected void release() {

    }

    protected abstract int getLayoutResId();


    /**
     * 是否可以对UI进行操作，比如更新UI控件，显示/消失对话框等
     * 由于Activity中存在大量的异步网络操作，若异步回调时，Activity已经被销毁，则不可以对UI进行更新操作
     *
     *  @return true - Activity未被销毁，可更新UI  false - Activity已被销毁，不可更新UI
     */
    public boolean canUpdateUI(){
        return (!isFinishing()) && (!isDestroyed());
    }


    public void setOnCancelListener(DialogInterface.OnCancelListener cancelListener){
        if (mDialog != null){
            mDialog.setOnCancelListener(cancelListener);
        }
    }

    /**
     * 检测是否是双击退出应用程序
     * @return true - 快速双击，间隔不少于1秒  false 不是快速双击
     */
    public synchronized static boolean isFastClick(){
        long time = System.currentTimeMillis();
        if (time - mLastClickTime < 1000){
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    protected InputMethodManager inputMethodManager;

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            consultService();
            // 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
            setIntent(new Intent());
        }
    }

    private void consultService() {
        // 启动聊天界面
        ConsultSource source = new ConsultSource(null, null, null);
        Unicorn.openServiceActivity(this, "爱吖客服", source);
    }

}
