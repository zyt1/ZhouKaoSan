package com.bwie.disanzhoukao;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.jc_player)
    JCVideoPlayerStandard jcPlayer;
    @BindView(R.id.btn_huan)
    Button btnHuan;
    //增加手机感应，自动全屏/小屏
    JCVideoPlayer.JCAutoFullscreenListener sensorListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorListener = new JCVideoPlayer.JCAutoFullscreenListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                super.onSensorChanged(event);
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                if (x < -10) { //direction right
                } else if (x > 5) { //direction left
                    if (JCVideoPlayerManager.getFirst() != null) {
                        JCVideoPlayerManager.getFirst().autoFullscreenLeft();
                        //mPLayer.startWindowFullscreen();
                    }
                } else if (y > 5) {
                    if (JCVideoPlayerManager.getFirst() != null) {
                        JCVideoPlayerManager.getFirst().autoQuitFullscreen();
                        //mPLayer.autoQuitFullscreen();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                super.onAccuracyChanged(sensor, accuracy);
            }
        };

        jcPlayer.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
        // mPLayer.startWindowFullscreen();//直接全屏播放
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor
                = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorListener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(sensorListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
