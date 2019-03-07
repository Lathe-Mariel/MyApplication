package com.akiranagai.myapplication.object3d;

import android.opengl.Matrix;
import android.util.Log;

import static java.lang.Math.abs;

public abstract class ObjectRail implements Runnable {
    int railDivide = 360;  //1周を何分割するか？
    int interruptTime=50;  //SleepTime
    Object3D object;  //動きを制御する対象のオブジェクト
    boolean alive;
    float[] matrix = new float[16];

    public void setObject3D(Object3D object){
        this.object = object;
    }
    public void setRailDivide(int divide){ this.railDivide = divide;}
    public void setInterruptTime(int time){ this.interruptTime = time;}

    public void setAlive(boolean state){
        alive = state;
    }

    public void run(){
        alive = true;
        while(alive) {
            try {
                Thread.sleep(interruptTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            next();
            object.setMatrix(matrix);
        }
    }

    abstract void next();

}
