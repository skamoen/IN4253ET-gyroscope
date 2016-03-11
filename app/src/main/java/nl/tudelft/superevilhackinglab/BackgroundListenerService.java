package nl.tudelft.superevilhackinglab;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by divhax on 05/03/2016.
 */
public class BackgroundListenerService extends Service implements SensorEventListener {

    private SensorManager sManager;
    private Context context;
    private FileOutputStream stream;
    KeyguardManager KM;
    private boolean isPrevLocked;
    File path;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("cek", "the service has been running");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        context = getApplicationContext();
        KM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        // initiate lockstate
        isPrevLocked = KM.inKeyguardRestrictedInputMode();



        //activate the file writer, prepare the folder and file
        path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gyro");

        File userinfo_file = new File(path,"device_info.txt");
        try {
            if(!path.exists()){
                path.mkdir();
            }
            if(!userinfo_file.exists()) {
                userinfo_file.createNewFile();
                FileOutputStream dstream;
                dstream = new FileOutputStream(userinfo_file);

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String DeviceID = tm.getDeviceId();

                String OSver = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                String ApiLevel = android.os.Build.VERSION.SDK_INT + "";
                String Device = android.os.Build.DEVICE;
                String Model = android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

                dstream.write((DeviceID+"\n").getBytes());
                dstream.write((OSver+"\n").getBytes());
                dstream.write((ApiLevel+"\n").getBytes());
                dstream.write((Device+"\n").getBytes());
                dstream.write((Model+"\n").getBytes());
                dstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);

        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        //unregister the sensor listener
        sManager.unregisterListener(this);
        //close the file writer
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        if(!KM.inKeyguardRestrictedInputMode()){
            if(isPrevLocked){
                isPrevLocked = false;
                Log.d("ceklock","it is unlocked");
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        int c_time = (int) System.currentTimeMillis();

        if(!isPrevLocked){
            isPrevLocked = true;
            Log.d("ceklock","it is locked");
            File gyro_file = new File(path,"gyro_"+c_time+".txt");

            try {
                gyro_file.createNewFile();
                stream = new FileOutputStream(gyro_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //else it will output the Roll, Pitch and Yawn values

        String gData0 = Float.toString(event.values[0]);
        String gData1 = Float.toString(event.values[1]);
        String gData2 = Float.toString(event.values[2]);

        double relative = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        String gData3 = Double.toString(relative);

        //write the data to the file
        try {
                String gyro_record = String.valueOf(c_time)+";"+gData0+";"+gData1+";"+gData2+";"+gData3+";"+"\n";
                stream.write(gyro_record.getBytes());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //nop
    }
}
