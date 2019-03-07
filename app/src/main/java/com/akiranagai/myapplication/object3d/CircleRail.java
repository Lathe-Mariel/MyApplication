package com.akiranagai.myapplication.object3d;

import android.opengl.Matrix;
import android.util.Log;

public class CircleRail extends ObjectRail{
    float currentAngle = 0;
    float oneStep = 1;
    private float shiftX, shiftY, shiftZ;

    public void setOffset(float x, float y, float z){
        shiftX = x;
        shiftY = y;
        shiftZ = z;
    }

    @Override
    public void setRailDivide(int divide){
        railDivide = divide;
        oneStep = 360f/ railDivide;
    }

@Override
    public void next(){
        currentAngle += oneStep;
        if(currentAngle > 360)currentAngle -= 360;
        Matrix.setRotateM(matrix,0, currentAngle, 0,1,0);
        Matrix.translateM(matrix, 0, shiftX, shiftY, shiftZ);
        return ;
    }

}
