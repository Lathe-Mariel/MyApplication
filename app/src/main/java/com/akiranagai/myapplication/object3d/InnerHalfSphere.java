package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.util.Log;

import com.akiranagai.myapplication.BufferUtil;
import com.akiranagai.myapplication.GLES;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class InnerHalfSphere {
    public InnerHalfSphere(){}
    public InnerHalfSphere(float maxlongitude, float minlatitude, float maxlatitude, int nSlices, int nStacks) {
        makeSphere(maxlongitude, minlatitude, maxlatitude, nSlices, nStacks, false);
        makeTexSphere(maxlongitude, minlatitude, maxlatitude, nSlices, nStacks, false);
    }

    public InnerHalfSphere(float maxlongitude, float minlatitude, float maxlatitude, int nSlices, int nStacks, boolean face) {
        makeSphere(maxlongitude, minlatitude, maxlatitude, nSlices, nStacks, face);
        makeTexSphere(maxlongitude, minlatitude, maxlatitude, nSlices, nStacks, face);
    }

    //bufferの定義
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer indexBuffer;
    protected FloatBuffer normalBuffer;

    private FloatBuffer texcoordBuffer;

    protected int nIndexs;
    protected int nPoints;

    protected void makeSphere(float maxlongitude, float minlatitude, float maxlatitude, int nSlices, int nStacks, boolean face) {
        int nSlices1=nSlices+1;
        int nStacks1=nStacks+1;
        nPoints = nSlices1*nStacks1;
        int sizeArray=nPoints*3;
        //頂点3Dベクトル
        float[] vertexs;
        double dlongitude=3.141592653589793/180.*maxlongitude/nSlices;
        double dlatitude=3.141592653589793/180.*(maxlatitude-minlatitude)/nStacks;
        double longitude, latitude;
        double minlatitudeR=3.141592653589793/180.*minlatitude;
        int p=0;

        vertexs = new float[sizeArray];

        for (int i=0; i<nSlices1; i++) {
            longitude = i*dlongitude;
            for (int j=0; j<nStacks1; j++) {
                latitude = minlatitudeR + j * dlatitude;
                vertexs[p++]=(float)(Math.cos(latitude)* Math.sin(longitude));      //x
                vertexs[p++]=(float) Math.sin(latitude);                            //y
                vertexs[p++]=(float)(Math.cos(latitude)* Math.cos(longitude));      //z
            }
        }
        //頂点の3D法線ベクトル 頂点座標と同じになる
        //float[] normals= new float[sizeArray];
        //for (int i=0;i<sizeArray;i++) normals[i]=vertexs[i];

        //頂点座標番号列
        p=0;
        nIndexs=nStacks1*nSlices*2 + nSlices*2-2;
        short[] indexs= new short[nIndexs];
        for (int i=0;i<nSlices; ) {
            for (int j=0;j<nStacks1;j++) {
                indexs[p++]=(short)(i*nStacks1+j);
                indexs[p++]=(short)((i+1)*nStacks1+j);
            }
            if(++i >= nSlices)break;
            indexs[p] = indexs[p-1];
            p++;
            indexs[p++] = (short)(i * nStacks1);
        }

        //Log.d("shpere", indexs[i] + "");
        /************************************
         nStacks = 4, nSlices = 5 のとき
         4 9 14 19 24 29
         3 8 13 18 23 28
         2 7 12 17 22 27
         1 6 11 16 21 26
         0 5 10 15 20 25
         *************************************/

        vertexBuffer= BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeShortBuffer(indexs);
        normalBuffer=vertexBuffer; //同じでよいので，同じ実体を用いる
    }

    private void makeTexSphere(float maxlongitude, float minlatitude, float maxlatitude, int nSlices, int nStacks, boolean face) {
        int sizeArray=nPoints*2;
        float dx = 1f/(float)nSlices;
        float dy = 1f/(float)nStacks;
        int p = 0;
        float textcoords[] = new float[sizeArray];
        for (int i=0; i <= nSlices;i++ ) {
            for (int j = 0; j <= nStacks; j++) {
                textcoords[p++] = i * dx;        // x
                textcoords[p++] = 1f - j * dy;   // y
            }

        }
        /************************************
         nStacks = 4, nSlices = 5 のときの点の位置
         4 9 14 19 24 29
         3 8 13 18 23 28
         2 7 12 17 22 27
         1 6 11 16 21 26
         0 5 10 15 20 25
         texture座標系
         (0,0)               (1,0) →x

         (0,1)               (1,1)
         ↓y
         *************************************/
        texcoordBuffer = BufferUtil.makeFloatBuffer(textcoords);
    }

    public void draw(float r,float g,float b,float a, float shininess){
        //頂点点列のテクスチャ座標
        GLES20.glVertexAttribPointer(GLES.texcoordHandle, 2,
                GLES20.GL_FLOAT, false, 0, texcoordBuffer);

        //頂点点列
        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);

        if (GLES.checkLiting()) {

            //頂点での法線ベクトル
            GLES20.glVertexAttribPointer(GLES.normalHandle, 3,
                    GLES20.GL_FLOAT, false, 0, normalBuffer);

            //周辺光反射
            GLES20.glUniform4f(GLES.materialAmbientHandle, r, g, b, a);

            //拡散反射
            GLES20.glUniform4f(GLES.materialDiffuseHandle, r, g, b, a);

            //鏡面反射
            GLES20.glUniform4f(GLES.materialSpecularHandle, 1f, 1f, 1f, a);
            GLES20.glUniform1f(GLES.materialShininessHandle, shininess);

        }

        //描画
        indexBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP,
                nIndexs, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

    }
}
