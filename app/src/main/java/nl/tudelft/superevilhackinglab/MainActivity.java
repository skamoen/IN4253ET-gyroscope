package nl.tudelft.superevilhackinglab;

        import android.app.Activity;
        import android.app.KeyguardManager;
        import android.content.Context;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.os.Environment;
        import android.util.Log;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener
{
    //a TextView
    private TextView gyroData0;
    private TextView gyroData1;
    private TextView gyroData2;
    private TextView gyroData3;
    private TextView deviceInfo;
    //the Sensor Manager
    private SensorManager sManager;

    Context context;
    FileOutputStream stream;
    KeyguardManager KM;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("cek","test");
        //get the TextView from the layout file
        gyroData0 = (TextView) findViewById(R.id.gyroData0);
        gyroData1 = (TextView) findViewById(R.id.gyroData1);
        gyroData2 = (TextView) findViewById(R.id.gyroData2);
        gyroData3 = (TextView) findViewById(R.id.gyroData3);
        deviceInfo = (TextView) findViewById(R.id.deviceInfo);

        String OSver = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        String ApiLevel = android.os.Build.VERSION.SDK_INT + "";
        String Device = android.os.Build.DEVICE;
        String Model = android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

        deviceInfo.setText(Device+" - "+Model);
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

        KM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);



    }

    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);

    }
    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();

        //close the file writer
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        //else it will output the Roll, Pitch and Yawn values

        String gData0 = Float.toString(event.values[0]);
        String gData1 = Float.toString(event.values[1]);
        String gData2 = Float.toString(event.values[2]);


        gyroData0.setText(gData0);
        gyroData1.setText(gData1);
        gyroData2.setText(gData2);

        double relative = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        String gData3 = Double.toString(relative);
        gyroData3.setText(gData3);

        //write the data to the file
        try {
            int c_time = (int) System.currentTimeMillis();
            String gyro_record = String.valueOf(c_time)+";"+gData0+";"+gData1+";"+gData2+";"+gData3+"\n";
            stream.write(gyro_record.getBytes());
            Log.d("testing",gyro_record);
        }catch (Exception e){
           e.printStackTrace();
        }

    }
}