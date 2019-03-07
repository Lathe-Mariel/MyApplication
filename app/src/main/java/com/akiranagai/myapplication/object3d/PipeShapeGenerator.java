package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;

import com.akiranagai.myapplication.BufferUtil;
import com.akiranagai.myapplication.GLES;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.akiranagai.myapplication.object3d.TexRectBase.textcoords;

public class PipeShapeGenerator extends CreatableShape {
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer indexBuffer;
    protected FloatBuffer normalBuffer;

    public static int nPoints;

    public PipeShapeGenerator(){}

    public PipeShapeGenerator(int id, float x, float y, float z, int u, int v){
        createShape3D(id, x, y, z, u, v);
    }

    /**
     *
     * @param id
     * @param length パイプ長さ
     * @param radius パイプ半径
     * @param maxlongitude ０～経度で何度まで作るか（πならハーフパイプ, 通常のパイプは2π)
     * @param nSlices 経度方向分割
     * @param nStacks 長手方向段数(通常 1)
     * @return
     */
    @Override
    public int createShape3D(int id, float length, float radius, float maxlongitude, int nSlices, int nStacks) {
        //頂点3Dベクトル
        float[] vertexs;
        /*
        double dlongitude=3.141592653589793/180.*maxlongitude/nSlices;

        double dlatitude=3.141592653589793/180.*(maxlatitude-minlatitude)/nStacks;
        double longitude, latitude;
        double minlatitudeR=3.141592653589793/180.*minlatitude;
        */

        int vertexNumber = (nSlices+1) * (nStacks+1);
        float stepRound = maxlongitude / nSlices;
        float stepVertical = 0;
        int p =0;

        vertexs = new float[vertexNumber * 3];

        for (int i=0; i<nStacks+1; i++) {
            //longitude = i*dlongitude;
            for (int j=0; j<nSlices+1; j++) {

                //latitude = minlatitudeR + j * dlatitude;
                vertexs[p++]=(float)(Math.cos(j * stepRound)*radius);      //x
                vertexs[p++]= stepVertical;                                 //y
                vertexs[p++]=(float)(Math.sin(j * stepRound)*radius);      //z
            }
            stepVertical += length / nStacks;
    }

        //頂点の3D法線ベクトル 頂点座標と同じになる
        //float[] normals= new float[sizeArray];
        //for (int i=0;i<sizeArray;i++) normals[i]=vertexs[i];

        //頂点座標番号列
        int indexsNumber = vertexNumber + 2*nStacks;
        short [] indexs= new short[indexsNumber];
        p=0;

        for (int i=0;i<nStacks;i++) {
            for (int j=0;j<nSlices+1;j++) {
                indexs[p++]=(short)(i*(nSlices+1) +j);
                indexs[p++]=(short)((i+1)*(nSlices+1) +j);

            }

            indexs[p++] = (short)(nSlices *(i)+nSlices);
            indexs[p++] = (short)(nSlices*(i+1)+nSlices);
        }

        /************************************
         nStacks = 12, nSlices = 1 のとき
         13  14  15  16  17  18  19  20  21  22  23  24  25
         0   1   2   3   4   5   6   7   8   9   10  11  12
         *************************************/

        float dx = 1f/(nSlices);
        float dy = 1f/nStacks;
        p = 0;
        int textcoordsNumber = (2+indexsNumber) *2;
        float textcoords[] = new float[textcoordsNumber];
        for (int i=0; i < (nStacks+1); i++) {
            for (int j = 0; j < nSlices; j++) {
                textcoords[p++] = j * dx;        // x
                textcoords[p++] = 1-(i * dy);  // y
            }
            textcoords[p++] = 1;
            textcoords[p++] = 1-(i*dy);
        }

        /************************************
         nStacks = 12, nSlices = 1 のとき
         12  13  14  15  16  17  18  19  20  21  22  23
         0   1   2   3   4   5   6   7   8   9   10  11

         texture座標系
         (0,0)               (1,0) →x

         (0,1)               (1,1)
         ↓y
         *************************************/

        FloatBuffer fb = BufferUtil.makeFloatBuffer(vertexs);
        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(fb, BufferUtil.makeShortBuffer(indexs), fb, BufferUtil.makeFloatBuffer(textcoords), indexs.length);


        /*
        vertexBuffer=BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeShortBuffer(indexs);
        normalBuffer=vertexBuffer; //同じでよいので，同じ実体を用いる
        */
    }

    @Override
    public int createShape3D(int id) {
        return createShape3D(id, 360f,-90f, 90f,24,24);
    }

    @Override
    public int createShape3D(int id, float x) {
        return createShape3D(id,360f, -90f,90f,(int)x, (int)x);
    }

    @Override
    public int createShape3D(int id, float x, float y) {
        return createShape3D(id, 360f, -90f, 90f, (int)x, (int)y);
    }
}
