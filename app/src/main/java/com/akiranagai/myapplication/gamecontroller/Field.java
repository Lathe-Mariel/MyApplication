package com.akiranagai.myapplication.gamecontroller;

import android.opengl.GLSurfaceView;

import com.akiranagai.myapplication.object3d.Object3D;

public interface Field {

    void setInputState(float x, float y, float z);
    boolean setObject(Object3D o);
    public int getAnswer();
    public void setAnswer(int answer);
}
