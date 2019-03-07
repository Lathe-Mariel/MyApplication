package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Toast extends CreatableShape {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;
    private float x,y;

    //頂点座標
    private float[] vertexs= new float[4*3];

    //頂点座標番号列
    private short[] indexs= {
            0,2,1,3
    };
    //拡頂点の法線ベクトル
    private float[] normals= {
            0f,0f,1f,
            0f,0f,1f,
            0f,0f,1f,
            0f,0f,1f
    };
    float textcoords[] = {
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    };

    public Toast() {
    }

    /*
    public void setRectangular(float width, float height) {
        float top=height*.5f;
        float bottom=-top;
        float right=width*.5f;
        float left=-right;

        //頂点座標
        float[] vertexs= {
                left,top,0f,      //左上 0
                right, top, 0f,    //右上 1
                left, bottom, 0f,  //左下 2
                right, bottom, 0f  //右下 3
        };
        vertexBuffer= BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeShortBuffer(indexs);
        normalBuffer=BufferUtil.makeFloatBuffer(normals);
        texcoordBuffer = BufferUtil.makeFloatBuffer(textcoords);
    }*/

    @Override
    public int createShape3D(int id, float x, float y){
        float top=y*.5f;
        float bottom=-top;
        float right=x*.5f;
        float left=-right;

        //頂点座標
        float[] vertexs= {
                left,top,0f,      //左上 0
                right, top, 0f,    //右上 1
                left, bottom, 0f,  //左下 2
                right, bottom, 0f  //右下 3
        };

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(textcoords), indexs.length);
    }
}
