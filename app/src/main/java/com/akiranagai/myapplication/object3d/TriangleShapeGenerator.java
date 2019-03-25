package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TriangleShapeGenerator extends CreatableShape {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;

    private float[] texturecoord;
    private int mode =0;

    //頂点座標
    final private float[] vertexs={
            0f,0f,0f,//P0
            0.05f,0.05f,0f,//P1
            -0.05f,0.05f,0f//P2
    };
    //頂点座標番号列
    final private short[] indexs= {
            0,1,2,0
    };
    private int numIndexs = indexs.length;
    //各頂点の法線ベクトル
    final private float[] normals={
            0,0,1,  //P0
            0,0,1,  //P1
            0,0,1,  //P2

    };
/*
    final float textcoords0[] = {
            1f,0f, //face 0
            0f,0f,
            1f,1f,
            0f,1f,

    };
*/


    @Override
    public int createShape3D(int id){
        return createShape3D(id, 1f);
    }
    @Override
    public int createShape3D(int id, float r){ //r rudius of circumsphere
        int i;
        float m,mm;

        for (i=0; i<vertexs.length; i++) {
            vertexs[i] *= r;
            normals[i] *= r;
        }

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), null, indexs.length);

        /*vertexBuffer=BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeByteBuffer(indexs);
        normalBuffer=BufferUtil.makeFloatBuffer(normals);
        texcoordBuffer = BufferUtil.makeFloatBuffer(textcoords);
        */
    }

    @Override
    public int createShape3D(int id, float x, float y) {
        return 0;
    }

    @Override
    public int createShape3D(int id, float x, float y, float z, int u, int v) {

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(texturecoord), indexs.length);

    }
}
