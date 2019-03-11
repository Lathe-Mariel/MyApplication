package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.akiranagai.myapplication.object3d.Cat;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;

public class GLActivity extends AppCompatActivity {
    FrameLayout layout;
    LinearLayout linearLayout;
    Cat player;
    GameManager manager;

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static Context ct;
    MyGLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ct == null)
            ct = this;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        manager = new GameManager(this);

        Intent intent = getIntent();
        int stage = intent.getIntExtra("STAGE_NUMBER", 0);
        manager.setCurrentStage(stage);
        manager.alwaysDrawCross = intent.getBooleanExtra("ALWAYS_DRAW_CROSS", false);

        surfaceView = new MyGLSurfaceView(this, manager);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        setContentView(relativeLayout);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(WC, WC);
        param.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL, 1);    //画面横向き固定
        relativeLayout.addView(surfaceView, param);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 2);

        relativeLayout.addView(manager.sInput, param);
        manager.init();
    }

    @Override
    public void onStart(){
        super.onStart();
        //si.addListener((ScreenInput.PanelInputListener) surfaceView.getRenderer());
    }

    @Override
    public void onResume(){
        super.onResume();
        surfaceView.onResume();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Log.d("onResume", "" + surfaceView.getRenderer());

        Log.d("check", "MainActivity - onCreate");
    }

    @Override
    public void onPause(){
        super.onPause();
        //si.removeListener((ScreenInput.PanelInputListener)surfaceView.getRenderer());
        Log.d("screenInput", "onPause()");
        surfaceView.onPause();
        //GLES.created = false;
        Texture.textureList.clear();
        if(manager.field !=null) {
            if (manager.field.objectMap != null)
                manager.field.objectMap.clear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
