package nl.tudelft.superevilhackinglab;

        import android.app.Activity;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener
{
    //a TextView
    private TextView gyroData0;
    private TextView gyroData1;
    private TextView gyroData2;
    private TextView gyroData3;
    //the Sensor Manager
    private SensorManager sManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the TextView from the layout file
        gyroData0 = (TextView) findViewById(R.id.gyroData0);
        gyroData1 = (TextView) findViewById(R.id.gyroData1);
        gyroData2 = (TextView) findViewById(R.id.gyroData2);
        gyroData3 = (TextView) findViewById(R.id.gyroData3);

        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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
        gyroData0.setText(Float.toString(event.values[0]));
        gyroData1.setText(Float.toString(event.values[1]));
        gyroData2.setText(Float.toString(event.values[2]));

        double relative = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        gyroData3.setText(Double.toString(relative));

    }
}