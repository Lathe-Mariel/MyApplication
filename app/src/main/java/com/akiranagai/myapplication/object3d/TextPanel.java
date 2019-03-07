package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;

import com.akiranagai.myapplication.BufferUtil;
import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.object3d.CreatableShape;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextPanel extends CreatableShape {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;
    private float x,y;

    //頂点座標
    private float[] vertexs= new float[4*3];

    //頂点座標番号列
    private short[] indexs= {
            0,1,2,3
    };
    //拡頂点の法線ベクトル
    private float[] normals= {
            0f,0f,1f,
            0f,0f,1f,
            0f,0f,1f,
            0f,0f,1f
    };
    float textcoords[] = {
            0f, .6f, //左下 2
            1f, 0.6f,  //右下 3
            0f, 0.4f, //左上 0
            1f, 0.4f //右上 1
    };

    public TextPanel() {
        setRectangular(1f, 1f);
    }
    public TextPanel(float width, float height) {
        setRectangular(width, height);
    }

    public void setRectangular(float width, float height) {
        float top=height*.5f;
        float bottom=-top;
        float right=width*.5f;
        float left=-right;

        //頂点座標
        float[] vertexs= {
                left, 0f,top,      //左上 0
                right, 0f, top,    //右上 1
                left, 0f, bottom,  //左下 2
                right, 0f, bottom  //右下 3
        };
        vertexBuffer= BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeShortBuffer(indexs);
        normalBuffer=BufferUtil.makeFloatBuffer(normals);
        texcoordBuffer = BufferUtil.makeFloatBuffer(textcoords);
    }

    @Override
    public int createShape3D(int id, float x, float y){
        float top=y*.5f;
        float bottom=-top;
        float right=x*.5f;
        float left=-right;

        //頂点座標
        float[] vertexs= {
                left, 0f,top,      //左上 0
                right, 0f, top,    //右上 1
                left, 0f, bottom,  //左下 2
                right, 0f, bottom  //右下 3
        };

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(textcoords), indexs.length);
    }
}
