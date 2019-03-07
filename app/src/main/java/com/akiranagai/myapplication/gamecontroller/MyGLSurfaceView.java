package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final Context mContext;
    //private GLRenderer renderer;
    GLStageRenderer renderer;


    // サーフェースビューのコンストラクタ
    public MyGLSurfaceView(Context context, GameManager manager) {
    //public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context);
        //super(context, attrs);
        mContext=context;
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 24, 0);
        //renderer = new GLRenderer(context);
        renderer = new GLStageRenderer(context, manager);
        setRenderer(renderer);
        manager.setRenderer(renderer);
        manager.surfaceView = this;
    }

    public GLStageRenderer getRenderer(){
        return renderer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
