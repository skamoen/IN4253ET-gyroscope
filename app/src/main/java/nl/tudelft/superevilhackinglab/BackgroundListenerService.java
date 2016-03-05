package nl.tudelft.superevilhackinglab;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        context = getApplicationContext();
        //activate the file writer, prepare the folder and file
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gyro");
        File file = new File(path,"gyro_data.txt");
        try {
            if(!path.exists()){
                path.mkdir();
            }
            if(!file.exists()) {
                file.createNewFile();
            }
            stream = new FileOutputStream(file);

            //stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

        //else it will output the Roll, Pitch and Yawn values

        String gData0 = Float.toString(event.values[0]);
        String gData1 = Float.toString(event.values[1]);
        String gData2 = Float.toString(event.values[2]);

        double relative = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        String gData3 = Double.toString(relative);

        //write the data to the file
        try {
            int c_time = (int) System.currentTimeMillis();
            String gyro_record = String.valueOf(c_time)+";"+gData0+";"+gData1+";"+gData2+";"+gData3+"\n";
            stream.write(gyro_record.getBytes());
            Log.d("testing", gyro_record);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //nop
    }
}
