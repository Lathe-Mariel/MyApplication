package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CrossTreeShapeGenerator extends CreatableShape {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;

    private float[] texturecoord;

    //頂点座標
    final private float[] vertexs={

            0f,1f,1f,//P0
            0f,1f,-1f,//P1
            0f,-1f,1f,//P2
            0f,-1f,-1f,//P3

            1f,1f,0f,//P4
            -1f,1f,0f,//P5
            1f,-1f,0f,//P6
            -1f,-1f,0f,//P7

    };
    //頂点座標番号列
    final private short[] indexs= {
            0,1,2,3,
            3,4,
            4,5,6,7,
            7,1,
            1,0,3,2,
            2,5,
            5,4,7,6

    };
    private int numIndexs = indexs.length;
    //各頂点の法線ベクトル
    final private float[] normals={

            1,0,0,  //P12
            1,0,0,  //P13
            1,0,0,  //P14
            1,0,0,  //P15
            0,0,1,  //P16
            0,0,1,  //P17
            0,0,1,  //P18
            0,0,1,  //P19

    };

    final float textcoords[] = {
            1f,0f, //face 0
            0f,0f,
            1f,1f,
            0f,1f,
            1f,0f, //face 1
            0f,0f,
            1f,1f,
            0f,1f,
            1f,0f, //face 0ura
            0f,0f,
            1f,1f,
            0f,1f,
            1f,0f, //face 1ura
            0f,0f,
            1f,1f,
            0f,1f,
    };


    @Override
    public int createShape3D(int id){
        return createShape3D(id, 1f);
    }
    @Override
    public int createShape3D(int id, float r){ //r rudius of circumsphere
        int i;
        float m,mm;
        m= (float) (r/Math.sqrt(vertexs[0]*vertexs[0]+vertexs[1]*vertexs[1]+vertexs[2]*vertexs[2]));
        mm= (float) (1/Math.sqrt(normals[0]*normals[0]+normals[1]*normals[1]+normals[2]*normals[2]));
        for (i=0; i<vertexs.length; i++) {
            vertexs[i] *= m;
            normals[i] *= mm;
        }

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(textcoords), indexs.length);

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


        int i;
        x /= 2;
        y /= 2;
        z /= 2;
        for (i=0; i<vertexs.length; ) {
            vertexs[i++] *= x;
            vertexs[i++] *= y;
            vertexs[i++] *= z;
        }

        Shape3D shape = new Shape3D(id);
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(texturecoord), indexs.length);

    }
}
