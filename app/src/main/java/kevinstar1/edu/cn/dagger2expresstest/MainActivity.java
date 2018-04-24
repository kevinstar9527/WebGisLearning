package kevinstar1.edu.cn.dagger2expresstest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import kevinstar1.edu.cn.dagger2expresstest.widget.MyCompassView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private  final String TAG = this.getClass().getSimpleName();

    /**
     * 加速度传感器
     */
    private TextView tvAccelerometer;
    private SensorManager mSensorManager;
    private float[] gravity = new float[3];
    private TextView mTvNearBy;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: "+msg.what);
        }
    };

    Timer mTimer = new Timer();
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            //获取数据
            Log.d(TAG, "run: "+"执行定时任务");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private MyCompassView mCompassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();
        tvAccelerometer = findViewById(R.id.tv_sensor);
        tvAccelerometer.setText("");

        mTvNearBy = findViewById(R.id.tv_near_by);

        mCompassView = findViewById(R.id.compass_view);

        //获取传感器技术参数
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //mTimer.schedule(mTimerTask,0,1000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        * 注册加速度传感器
        * */
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        //采集频率
        SensorManager.SENSOR_DELAY_NORMAL);
        /*注册重力传感器*/
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
        //采样频率，即两次采样的时间间隔
        SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
        //
        SensorManager.SENSOR_DELAY_NORMAL,mHandler);

        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        ,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     *
     * 传感器数据变化时回调
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //判断传感器类别
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER://加速度传感器
               /* float alpha = (float) 0.8;
                gravity[0] = alpha*gravity[0]+(1-alpha)*event.values[0];
                gravity[1] = alpha*gravity[1]+(1-alpha)*event.values[1];
                gravity[2] = alpha*gravity[2]+(1-alpha)*event.values[2];
                String accelerometer = "加速度传感器\n"+"x:"
                        +(event.values[0]-gravity[0])+"\n"+"y:"
                        +(event.values[1]-gravity[1])+"\n"+"z:"
                        +(event.values[2]-gravity[2])+"\n";*/
               String accelerometer = "加速度传感器\n"+"x:"
                       +event.values[0]+"\n"+"Y:"
                       +event.values[1]+"\n"+"Z:"
                       +event.values[2];

                tvAccelerometer.setText(accelerometer);
                break;
            case Sensor.TYPE_GRAVITY://重力传感器
                //单位m/s`2
                gravity[0] = event.values[0];
                gravity[1] = event.values[1];
                gravity[2] = event.values[2];
                break;
            case Sensor.TYPE_PROXIMITY:
                mTvNearBy.setText("距离传感器:"+String.valueOf(event.values[0]));

                Sensor sensor = event.sensor;

                //DELAY 表示最大采样率

                int delay = sensor.getMinDelay();
                //输出传感器可测的最大范围
                Log.d(TAG, "onSensorChanged: "+sensor.getMaximumRange());

                break;
            case Sensor.TYPE_ORIENTATION:
                //angle 表示距离正北方向的角度
                float angle = event.values[0];
                mCompassView.updateData(angle);
                break;
            default:
                break;

        }
    }
    /*
    *
    * 传感器精度变化时回调
    * */

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
