package com.akiranagai.myapplication;

import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.object3d.Object3D;

import java.util.ArrayList;
import java.util.Arrays;

public class MoveObject3D extends Object3D implements Runnable{

        private float posX, posY, posZ, posA;
        private int interval = 50;
        private boolean moving = true;
        private float angle;
        private float[] shiftMatrix = new float[16];
        private float[] shiftTranslate = new float[]{0,0,0};
        private float[] shiftRotate = new float[]{0,1,0};
        private int shiftAngle = 0;

        ArrayList<MatrixEffect> postCommandList = new ArrayList();
        ArrayList<MatrixEffect> preCommnadList = new ArrayList();

    public void makeShiftMatrix(){
            Matrix.setIdentityM(shiftMatrix,0);
        Matrix.rotateM(shiftMatrix, 0, shiftAngle, shiftRotate[0], shiftRotate[1], shiftRotate[2]);
        Matrix.translateM(shiftMatrix, 0, shiftTranslate[0], shiftTranslate[1], shiftTranslate[2]);
    }

    void setShiftTranslate(float x, float y, float z){
        shiftTranslate[0] = x;
        shiftTranslate[1] = y;
        shiftTranslate[2] = z;
    }

@Override
    public void makeMatrix(){

    mMatrix = Arrays.copyOf(shiftMatrix, shiftMatrix.length);
    Matrix.setIdentityM(mMatrix,0);
                Matrix.rotateM(mMatrix, 0, rotateAngleValue, rotateValues[0], rotateValues[1], rotateValues[2]);
    for(int i =0; i<16; i++) Log.d("message", "after rotateM mMatrix[" + i + "]: " + mMatrix[i]);
    Matrix.multiplyMM(mMatrix, 0, mMatrix,0, shiftMatrix, 0);
    for(int i =0; i<16 ;i++) Log.d("message", "after * shiftMatrix mMatrix[" + i + "]: " + mMatrix[i]);
                Matrix.translateM(mMatrix, 0, translateValues[0], translateValues[1], translateValues[2]);
                Matrix.scaleM(mMatrix, 0, scaleValues[0], scaleValues[1], scaleValues[2]);
    }

        public void setPos(float x, float y, float z){
            posX = x;
            posY = y;
            posZ = z;
        }

        public void circleSet(float radius, float angleStep){
           posX = radius;
           angle += angleStep;
           translateValues[0] = posX;
           translateValues[1] = -2f;
           translateValues[2] = 0;
           rotateValues[0] = 0;
           rotateValues[1] = 1;
           rotateValues[2] = 0;
           rotateAngleValue = (int)angle;
           makeMatrix();
        }

        public void move(float x, float y, float z){
           float oldX = posX;
           float oldY = posY;
           float oldZ = posZ;
           posX = x;
           posY  = y;
           posZ = z;
           oldX -= posX;
           oldY -= posY;
           posA = (float)(Math.atan(oldX/oldY));

        }
        public void run(){
            while(moving) {
                try {
                    Thread.sleep(interval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                circleSet(7f,0.4f);
                //Log.d("message", "angle: " + angle);
                /*
                posX -= incX;
                posY -= incY;
                posZ -= incZ;
                posA -= incA;
                if(rotateAxis == 1) {
                    rotateValues[0] = 0;
                    rotateValues[1] = 1;
                    rotateValues[2] = 0;
                }else if(rotateAxis == 0){
                    rotateValues[0] = 1;
                    rotateValues[1] = 0;
                    rotateValues[2] = 0;
                }else{
                    rotateValues[0] = 0;
                    rotateValues[1] = 0;
                    rotateValues[2] = 1; */
                }
            }
            class MatrixEffect{
            static final int ROTATE = 0;
            static final int TRANSLATE = 1;
            static final int SCALE = 2;

            private float value[] = new float[4];
            private int methodType;

            void makeEffect(){
                if(methodType == ROTATE){

                }else if(methodType == TRANSLATE){

                }else if(methodType == SCALE){

                }
            }
            }
}
