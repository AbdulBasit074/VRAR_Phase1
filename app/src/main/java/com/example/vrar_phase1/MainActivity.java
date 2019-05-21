package com.example.vrar_phase1;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


//////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity /**implements SeekBar.OnSeekBarChangeListener, SensorEventListener **/{



    private VrVideoView mVrVideoView;
    public VrPanoramaView mVrPanoramaView;
    public Sensor sensor;
    public SensorManager sensorManager;
    VrPanoramaView.Options ioptions = new VrPanoramaView.Options();


////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initview();//intilize Components
        ImageViewer();
        //    FFMPEG_INSTANCE();//FFMPEG INTILIZATION
        //    SensorControl();//Sensor Control
        //    Playvideo();//Playing 360 video
    }

    /**
    //////////////////////////////FFMPEG INSTANCE//////////////////////

     public void FFMPEG_INSTANCE()
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(getApplicationContext());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),"FFMPEG WORKING",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Toast.makeText(getApplicationContext(),"FFMPEG NOT SUPPORTED",Toast.LENGTH_LONG).show();

        }
    }**/
     /////////////////////-------Initilize COMPONENTS-------/////////////////////////////////////////////
     public void Initview()
    {
        mVrVideoView  = findViewById(R.id.video_view);
        mVrPanoramaView = findViewById(R.id.pano_view);
        //mVrPanoramaView.setVisibility(View.GONE);
    }
    /**
     /////////////////////-------Play Video-------//////////////////////////////////////////////////////
     public void Playvideo()
     {
         mVrVideoView.setEventListener(new ActivityEventListener());
         VideoLoaderTask mBackgroundVideoLoaderTask = new VideoLoaderTask();
         mBackgroundVideoLoaderTask.execute();
     }
     /////////////////////////////////////Sensor Control///////////////////////////////////////////////////
     public Sensor SensorControl()
     {
         sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
         sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
         sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
         return sensor;
     }

    ///////////////////////////-----END----------///////////////////////////////////////////////////

///////////////////////////-----IMU SENSOR PHASE------//////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        int b = (int) event.values[1];
          if(b>0)
          {
              mVrVideoView.pauseVideo();
              mVrVideoView.setVisibility(View.GONE);
              mVrPanoramaView.setVisibility(View.VISIBLE);

              String[] Command   = {"-i","/storage/0000-0000/VrVideo/Produce.mp4","-ss",mVrVideoView.getCurrentPosition()/1000+"",
                      "-vframes","1",
                  "storage/emulated/0/folder/thd.jpg"};
              EXTRACT(Command);
          }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//////////////////////////-----END IMU PHASE-------/////////////////////////////////////////////////
//////////////////////////-----SEEK BAR(360 VIDEO)-----/////////////////////////////////////////////
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
////////////////////////////////---END SEEK BAR(360 VIDEO)-----//////////////////////////////////////

///////////////////////////////----CLASS VIDEO PLAY------------/////////////////////////////////////
   private   class ActivityEventListener extends VrVideoEventListener{
    @Override
    public void onLoadSuccess() {
        super.onLoadSuccess();

        Toast.makeText(getApplicationContext(), "Success"+mVrVideoView.getDuration()/1000, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onLoadError(String errorMessage) {
        super.onLoadError(errorMessage);
        Toast.makeText(getApplicationContext(), "error found", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onClick() {
        super.onClick();
    }
    @Override
    public void onNewFrame() {
        super.onNewFrame();
    }
    @Override
    public void onCompletion() {
        super.onCompletion();
    }
}
////////////////////////////////////////END CLASS///////////////////////////////////////////////////
    ////////////////////////////////////////SYNCRONIZE VIDEO///////////////////////////////////////////
class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.TYPE_MONO;
            String path = "/storage/0000-0000/VrVideo/Produce.mp4";
            Uri uri = Uri.parse(path);
           //mVrVideoView.loadVideoFromAsset("Produce.mp4", options);
            mVrVideoView.loadVideo(uri,options);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////IMAGE EXTRACTION BY FFMPEG COMMAND///////////////////////////////
    public void EXTRACT(String[] cmd)
    {

        FFmpeg ffmpeg = FFmpeg.getInstance(getApplicationContext());
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(String message) {
                    Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_LONG).show();
                    File imgFile = new File("/storage/emulated/0/folder/thd.jpg");
                    Bitmap mybitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mVrPanoramaView.loadImageFromBitmap(mybitmap,ioptions);

                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String message) {


                }
                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }


    }
   /**
    //////////////////////////////////////-----FOR IMAGE----////////////////////////////////////////
    **/
    public void ImageViewer()
    {
        InputStream inputStream = null;

        AssetManager assetManager = getAssets();

        try {
            ioptions.inputType = VrPanoramaView.Options.TYPE_MONO;
            inputStream = assetManager.open("image.jpg");
            mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(inputStream), ioptions);
            inputStream.close();
        } catch (IOException e) {
            Log.e("Tuts+", "Exception in loadPhotoSphere: " + e.getMessage() );
        }
    }
    /**
     //////////////////////////////////////////////////////////////////////////////////////////////
     **/
}

