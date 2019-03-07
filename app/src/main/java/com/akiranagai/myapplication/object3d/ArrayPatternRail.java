package com.akiranagai.myapplication.object3d;

public class ArrayPatternRail extends ObjectRail {

    private float incX, incY, incZ;
    private int segmentNumber, segmentDivide;
    private int segmentStep = 0;  //何セグメント目か
    private int step=0;  //何ステップ目か
    private float objectAngle = 0;

    ArrayPatternRail(){
        segmentNumber =(int)(pattern0.length / 4);
        segmentDivide = segmentNumber / railDivide;
    }

    private final float[][] pattern0 =  //x, y, z, sleepTime
            {{1,0,1, 50},
                    { -1,0,1, 50},
                    {-1,0,-1, 50},
                    {1,0,-1, 50}};

    private void calcInc(){
        step = 0;
        int oldSegmentStep = segmentStep;
        segmentStep = ++segmentStep%segmentNumber;
        incX = pattern0[segmentStep][0] - pattern0[oldSegmentStep][0] /segmentDivide;
        incY = pattern0[segmentStep][1] - pattern0[oldSegmentStep][1] /segmentDivide;
        incZ = pattern0[segmentStep][2] - pattern0[oldSegmentStep][2] /segmentDivide;
    }

    @Override
    public void next(){
        step++;
        if(step > segmentDivide) {
            calcInc();
        }
        object.translateValues[0] += incX;
        object.translateValues[1] += incY;
        float newAngle = (float)(Math.atan(incY/incX)*360/6.283);
        if(newAngle < 0){
            newAngle += 360;
        }
        float sub = newAngle - objectAngle;
        if(sub > 2){
            objectAngle += 2;
        }else if(sub < -2){
            objectAngle -= 2;
        }else{
            objectAngle = newAngle;
        }
        object.rotateAngleValue=(int)objectAngle;
        return;
    }
}
