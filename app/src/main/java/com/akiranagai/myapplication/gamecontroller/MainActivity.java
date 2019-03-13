package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.akiranagai.myapplication.object3d.Cat;
import com.akiranagai.myapplication.R;

public class MainActivity extends AppCompatActivity {
GameManager manager;


    public static Context ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMessage();
    }

    public void sendMessage() {
        Intent intent = new Intent(this, StageSelectActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, "message");
        startActivityForResult(intent, 1);
    }

    public void onActivityResult( int requestCode, int resultCode, Intent intent )
    {
        // startActivityForResult()の際に指定した識別コードとの比較
        if( requestCode == 1 ){

            // 返却結果ステータスとの比較
            if( resultCode == RESULT_OK ){

                // 返却されてきたintentから値を取り出す
                String str = intent.getStringExtra( "key" );
                if(Build.VERSION.SDK_INT <= 10){
                    finish();
                }else if(Build.VERSION.SDK_INT >= 21){
                    finishAndRemoveTask();
                }
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //si.addListener((ScreenInput.PanelInputListener) surfaceView.getRenderer());
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
