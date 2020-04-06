package com.example.a0402test2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YJtest";
        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();

        //권한 요청
        checkSelfPermission();



        //#######3버튼 누르면 이거 실행하게 코드 짜자
        //FFmpeg.execute("-f android_camera -i 0:0 -r 30 -pixel_format bgr0 -t 00:00:05 <record file path>");

        //rtmp 프로토콜에 쓸 코드
        //ffmpeg -re -i <input> -f flv -rtmp_playpath some/long/path -rtmp_app long/app/name rtmp://username:password@myserver/
        int rc = FFmpeg.execute("-f android_camera -i 0:0 -r 30 -pixel_format bgr0 -t 00:00:05 /storage/emulated/0/file3.mp4");

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        /*  기존에 있는 video 바꾸는 코드
        int rc = FFmpeg.execute("-i /storage/emulated/0/file1.mp4 -c:v mpeg4 /storage/emulated/0/file3.mp4");

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
            Log.i(Config.TAG, "성공");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
            Log.i(Config.TAG, "유저에의해 실패");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Log.i(Config.TAG, String.format("위에 log참고", rc));
            Config.printLastCommandOutput(Log.INFO);
        }
        */
    }



    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permissions, @NonNull int [] grantResults){
            //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for(int i=0; i<length; ++i){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    //동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }
    }

    private void checkSelfPermission() {

        String temp = "";
        //파일 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        //카메라 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.CAMERA + " ";
        }

        if(TextUtils.isEmpty(temp) == false){
            //권한요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else{
            //모두 허용 상태
            Toast.makeText(this,"권한을 모두 허용", Toast.LENGTH_LONG).show();
        }
    }


    //동영상 녹화하는 화면으로 Intent
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
//            videoView.setVideoURI(videoUri);
//        }
//    }

}
