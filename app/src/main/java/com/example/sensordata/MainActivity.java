package com.example.sensordata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView magneticView;
    private TextView orientationView;
    private TextView totalaccelerationView;
    private TextView accelerationView;
    private TextView frequencyView;
    private SensorManager sensorManager;
    private MySensorEventListener sensorEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorEventListener = new MySensorEventListener();
        magneticView = (TextView) findViewById(R.id.magneticView);
        accelerationView = (TextView) findViewById(R.id.accelerationView);
        orientationView = (TextView) findViewById(R.id.orientationView);
        frequencyView = (TextView) findViewById(R.id.frequencyView);
        totalaccelerationView = (TextView) findViewById(R.id.totalaccelerationView);
        //获取感应器管理器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    @Override
    protected void onResume()
    {
        //获取方向传感器
        @SuppressWarnings("deprecation")
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //获取加速度传感器
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //获取磁传感器
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private final class MySensorEventListener implements SensorEventListener
    {
        //可以得到传感器实时测量出来的变化值
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            //得到方向的值
            if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
            {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                orientationView.setText("方向: " + x + ", " + y + ", " + z);
            }
            //得到加速度的值
            else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                long time = System.currentTimeMillis();
                long last_time = time;
                accelerationView.setText("加速度X、Y、Z分量: " + x + ", " + y + ", " + z);
                //frequencyView.setText("传感器频率：" +(1000/(time - last_time)));
                //计算和磁场强度
                float total=(float)(Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2)));
                totalaccelerationView.setText("合成加速度: " + total);
            }
            //得到磁场的值
            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                magneticView.setText("磁场强度X、Y、Z分量: " + x + ", " + y + ", " + z);
            }
        }
        //重写变化
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
        }
    }

    //暂停传感器的捕获
    @Override
    protected void onPause()
    {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

}