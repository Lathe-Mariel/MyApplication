package com.akiranagai.myapplication.gamecontroller;

import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.object3d.Object3D;

import java.nio.BufferUnderflowException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class Field3D implements Field {

    private int answer = 0;
    final int fieldX = 500;  //グリッド
    final int fieldY = 5;
    final int fieldZ = 500;
    int[] dpi = new int[3];  //フィールド分解能  フィールド距離(OpenGL上)/グリッド

    float userInputU, userInputV, userInputW;
    int[] fieldBias = new int[3];

    private double viewAngle;  //視線方向
    private double tmpX, tmpZ; //位置変位
    private GLStageRenderer stageRenderer;

    Object3D[][][] playField = new Object3D[fieldX][fieldY][fieldZ];

    public Field3D(){
        this(100, 100, 100);
    }

    public Field3D(int x, int y, int z){
        dpi[0] = fieldX / x;
        dpi[1] = fieldY / y;
        dpi[2] = fieldZ / z;
        fieldBias[0] = fieldX /2;
        fieldBias[1] = fieldY /2;
        fieldBias[2] = fieldZ /2;
    }

    public boolean setObject(Object3D addObject) {
        final float[] effectMatrix = addObject.getMMatrix();
        FloatBuffer source = addObject.getVertexBuffer();
        Log.d("check", "source vertexes: " + source.capacity() / 3 + "    source.capacity(): " + source.remaining());
        FloatBuffer fb = FloatBuffer.allocate(source.capacity());
        fb.position(0);
        source.position(0);
        float[] result = {0f, 0f, 0f, 0f};
        float[] affectedMatrix = {0f, 0f, 0f, 1f};

        while (3 <= source.remaining()) {
            try {
                source.get(affectedMatrix, 0, 3);
            } catch (BufferUnderflowException e) {
                e.printStackTrace();
                Log.d("error", "BufferUnderflow");
            }

            Matrix.multiplyMV(result, 0, effectMatrix, 0, affectedMatrix, 0);
            fb.put(result, 0, 3);
            //Log.d("check", "remaining: " + source.remaining());
        }
        source.position(0);
        fb.position(0);
        obstacle = fb;
        calculateAndPutIntoFieldGrid(fb, addObject);
        return true;
    }

    private FloatBuffer obstacle;
    public FloatBuffer getObstacleBuffer(){
        return obstacle;
    }

    private void calculateAndPutIntoFieldGrid(FloatBuffer fb, Object3D addObject){

        float[] v1 = new float[3];
        float[] v2 = new float[3];
        float[] v3 = new float[3];
        fb.position(0);
        while(fb.remaining() > 8) {
            fb.get(v1, 0, 3);
            fb.get(v2, 0, 3);
            fb.get(v3, 0, 3);
            float[][] result = fillDot2Dot(v1, v2, addObject);
            for(int i = 0; i < result.length; i++){
                fillDot2Dot(result[i], v3, addObject);
            }
        }
    }

    private float[][] fillDot2Dot(float[] v1, float[] v2, Object3D addObject){
        int biggestDirection; //2頂点間で最も遠い方向(xyz)
        float[] distance = new float[3];
        float v12[] = new float[3];
        v12[0] = v2[0]-v1[0];
        v12[1] = v2[1]-v1[1];
        v12[2] = v2[2]-v1[2];
        distance[0] = Math.abs(v12[0]);
        distance[1] = Math.abs(v12[1]);
        distance[2] = Math.abs(v12[2]);
        if(distance[0] > distance[1]){
            if(distance[0] > distance[2]) {
                biggestDirection = 0;
            }else{
                biggestDirection = 2;
            }
        }else if(distance[1] < distance[2]){
            biggestDirection = 2;
        }else{
            biggestDirection = 1;
        }

        int realDiv = (int)(v12[biggestDirection]*dpi[biggestDirection]);  //v1-v2間　分割数
        realDiv = Math.abs(realDiv);
        float[][] pointsResult = new float[realDiv][3];
        float v12div[] = new float[3];  //1step計算 当たりの物理距離　＝　頂点間距離　/　分割数
        v12div[0] = v12[0] / realDiv;
        v12div[1] = v12[1] / realDiv;
        v12div[2] = v12[2] / realDiv;

        for(int i = 0; i < realDiv; i++){
            float x = v1[0]+ v12div[0]*i;
            float y = v1[1]+ v12div[1]*i;
            float z = v1[2]+ v12div[2]*i;
            pointsResult[i][0] = x;
            pointsResult[i][1] = y;
            pointsResult[i][2] = z;
            playField[fieldBias[0] + (int)(x*dpi[0])][fieldBias[1] + (int)(y*dpi[1])][fieldBias[2] + (int)(z*dpi[2])] = addObject;
        }
        return pointsResult;
    }

    private FloatBuffer allObstacles;

    public FloatBuffer getAllObstacles(){
        return allObstacles;
    }
    public void showAllField(){
        allObstacles = FloatBuffer.allocate(playField.length * playField[0].length* playField[0][0].length);
        for(int i =0; i<playField.length; i++){
            for(int j =0; j < playField[i].length; j++){
                for(int k =0; k < playField[i][j].length; k++){
                    if(playField[i][j][k] == null)continue;
                    Log.d("check", "[i]: " + i + "  [j]: " + j + "  [k]: " + k + "    content: " + playField[i][j][k]);
                    float[] point = {(float)((i-fieldBias[0])/dpi[0]), (float)((j-fieldBias[1])/dpi[1]), (float)((k-fieldBias[2])/dpi[2])};
                    allObstacles.put(point);

                }
            }
        }
    }

    public boolean setAllObject(ArrayList<Object3D> addList){
        boolean result = true;
        for(Iterator<Object3D> i = addList.iterator(); i.hasNext(); ){
            if(setObject(i.next())) result = false;
        }
        return result;
    }

    /**
     * This method recieves state from ScreenInput object.
     */
    @Override
    public void setInputState(float x, float z, float b){

    }

    private float[] tryThrough(float[] v1, float[] v2){
        int biggestDirection; //2頂点間で最も遠い方向(xyz)
        float[] distance = new float[3];
        float v12[] = new float[3];
        v12[0] = v2[0]-v1[0];
        v12[1] = v2[1]-v1[1];
        v12[2] = v2[2]-v1[2];
        distance[0] = Math.abs(v12[0]);
        distance[1] = Math.abs(v12[1]);
        distance[2] = Math.abs(v12[2]);
        if(distance[0] > distance[1]){
            if(distance[0] > distance[2]) {
                biggestDirection = 0;
            }else{
                biggestDirection = 2;
            }
        }else if(distance[1] < distance[2]){
            biggestDirection = 2;
        }else{
            biggestDirection = 1;
        }

        int realDiv = (int)(v12[biggestDirection]*dpi[biggestDirection]);
        realDiv = Math.abs(realDiv);
        float v12div[] = new float[3];  //グリッド当たりの物理距離　＝　頂点間距離　/　分割数
        v12div[0] = v12[0] / realDiv;
        v12div[1] = v12[1] /realDiv;
        v12div[2] = v12[2]/realDiv;

        int x=0,y=0,z=0;
        for(int i = 0; i < realDiv; i++){
            x = (int)((v1[0]+ v12div[0]*i)*dpi[0]+fieldBias[0]);
            y = (int)((v1[1]+v12div[1]*i)*dpi[1]+fieldBias[1]);
            z = (int)((v1[2]+v12div[2]*i)*dpi[2]+fieldBias[2]);
            if(playField[x][y][z] != null ){
                i = i <1?0:i-1;
                x = (int)(v1[0]+ v12div[0]*i);
                y = (int)(v1[1]+v12div[1]*i);
                z = (int)(v1[2]+v12div[2]*i);
                return new float[]{x,y,z};
            }
        }
        return v2;

    }

    @Override
    public void setAnswer(int answer){
        this.answer = answer;
    }
    @Override
    public int getAnswer(){
        return answer;
    }
}
