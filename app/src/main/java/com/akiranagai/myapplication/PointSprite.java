package com.akiranagai.myapplication;

import android.opengl.GLES20;

import java.nio.FloatBuffer;


/**
 * Created by tommy on 2016/01/17.
 */
public class PointSprite {
    //buffer
    private FloatBuffer vertexBuffer;
    //private ByteBuffer indexBuffer;
    //private FloatBuffer normalBuffer;
    float[] vertexs;
    private int numVertexes;

    //コンストラクタ
    public PointSprite(int n) { //nは点の数 255以下
        vertexs = new float[n*3];
        vertexBuffer = BufferUtil.makeFloatBuffer(vertexs);
        numVertexes=n;
        if (n==1) {
            vertexs[0]=vertexs[1]=vertexs[2]=0f;
            BufferUtil.setFloatBuffer(vertexs,vertexBuffer);
        }
    }

    public PointSprite(FloatBuffer fb){
        vertexBuffer = fb;
        vertexBuffer.position(0);
        numVertexes = fb.capacity()/3;
    }

    public void setVertexs(float p[]){ //コンストラクタで指示した点の数だけ，ｘ,y,z, x,y,z, x,y,z,・・ 点の数×3の配列
        System.arraycopy(p, 0, vertexs, 0, numVertexes*3);
        BufferUtil.setFloatBuffer(vertexs,vertexBuffer);
    }

    public void draw(float r,float g,float b,float a, float pointsize){
        //点列
        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);

        //点に使う単色の設定 (r, g, b,a)
        GLES20.glUniform4f(GLES.objectColorHandle, r, g, b, a);

        //パーティクルのサイズ設定
        GLES20.glUniform1f(GLES.pointSizeHandle, pointsize);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, numVertexes);
    }

}
