package com.test.shakephone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;


public class MainActivity extends AppCompatActivity implements SensorEventListener{
    SensorManager sensorManager;
    Sensor sensor;
    Vibrator vibrator;
    long curTime,lastTime;
    ImageView imageView;
    int times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏导航栏
        getSupportActionBar().hide();
        //状态栏透明
        Window window=getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        //初始化对象
        init();
        //
        Glide.with(this)
                .load("https://iw233.cn/api.php?sort=random")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(imageView);
        /** //获取webview
        WebView webView=(WebView)findViewById(R.id.F_picture);
        webView.loadUrl("https://iw233.cn/API/Random.php");
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        **/
    }
    public void init(){
        //计数
        times=0;
        lastTime=System.currentTimeMillis();
        Toast.makeText(this,"晃动手机开冲！",Toast.LENGTH_SHORT).show();
        //获取传感器管理对像
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取指定传感器对象---加速度传感器
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //注册传感器对象
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        //震动
        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imageView=(ImageView)findViewById(R.id.F_picture);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this)
                .load("https://iw233.cn/api.php?sort=random")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(imageView);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(this).clear(imageView);
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values=sensorEvent.values;
        float x=values[0];
        float y=values[1];
        float z=values[2];
        int minValue=30;
        if(Math.abs(x)>minValue||Math.abs(y)>minValue||Math.abs(z)>minValue){
            curTime = System.currentTimeMillis();
            if(curTime-lastTime>1000){
                lastTime=curTime;
                long[] pattern={0,400};
                vibrator.vibrate(pattern,-1);
                Drawable mdrawable=imageView.getDrawable();
                if(mdrawable==null){
                    Toast.makeText(this, "出错了！", Toast.LENGTH_SHORT).show();
                }else {
                    Glide.with(this)
                            .load("https://iw233.cn/api.php?sort=random")
                            .placeholder(mdrawable)
                            .skipMemoryCache(false)
                            .signature(new ObjectKey(System.currentTimeMillis()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .into(imageView);
                    times++;
                    if(times%15==0){
                        Toast.makeText(this, "别冲了，别冲了", Toast.LENGTH_SHORT).show();
                    }
                    if(times==100){
                        Toast.makeText(this, "达成每日百冲成就！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}