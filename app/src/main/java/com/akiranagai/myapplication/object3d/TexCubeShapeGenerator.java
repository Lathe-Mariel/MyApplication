package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * finished
 */
public class TexCubeShapeGenerator extends CreatableShape {
    //bufferの定義
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;

    private float[] texturecoord;
    private int mode =0;

    //頂点座標
    final private float[] vertexs={
            1f,1f,1f,//P0
            1f,1f,-1f,//P1
            -1f,1f,1f,//P2
            -1f,1f,-1f,//P3

            -1f,1f,1f,//P4
            -1f,1f,-1f,//P5
            -1f,-1f,1f,//P6
            -1f,-1f,-1f,//P7

            -1f,-1f,1f,//P8
            -1f,-1f,-1f,//P9
            1f,-1f,1f,//P10
            1f,-1f,-1f,//P11

            1f,-1f,1f,//P12
            1f,-1f,-1f,//P13
            1f,1f,1f,//P14
            1f,1f,-1f,//P15

            -1f,-1f,1f,//P16
            1f,-1f,1f,//P17
            -1f,1f,1f,//P18
            1f,1f,1f,//P19

            1f,-1f,-1f,//P20
            -1f,-1f,-1f,//P21
            1f,1f,-1f,//P22
            -1f,1f,-1f//P23
    };
    //頂点座標番号列
    final private short[] indexs= {
            0,1,2,3, 4,5,6,7,
            8,9,10,11, 12,13,14,15, 15,16,
            16,17,18,19, 19,20, 20,21,22,23
    };
    private int numIndexs = indexs.length;
    //各頂点の法線ベクトル
    final private float[] normals={
            0,1,0,  //P0
            0,1,0,  //P1
            0,1,0,  //P2
            0,1,0,  //P3
            -1,0,0,  //P4
            -1,0,0,  //P5
            -1,0,0,  //P6
            -1,0,0,  //P7
            0,-1,0,  //P8
            0,-1,0,  //P9
            0,-1,0,  //P10
            0,-1,0,  //P11
            1,0,0,  //P12
            1,0,0,  //P13
            1,0,0,  //P14
            1,0,0,  //P15
            0,0,1,  //P16
            0,0,1,  //P17
            0,0,1,  //P18
            0,0,1,  //P19
            0,0,-1,  //P20
            0,0,-1,  //P21
            0,0,-1,  //P22
            0,0,-1  //P23
    };

    final float textcoords0[] = {
        1f,0f, //face 0
        0f,0f,
        1f,1f,
        0f,1f,
        1f,0f, //face 1
        0f,0f,
        1f,1f,
        0f,1f,
        1f,0f, //face 2
        0f,0f,
        1f,1f,
        0f,1f,
        1f,1f, //face 3
        0f,1f,
        1f,0f,
        0f,0f,
        0f,1f, //face 4
        1f,1f,
        0f,0f,
        1f,0f,
        0f,1f, //face 5
        1f,1f,
        0f,0f,
        1f,0f
    };

    final float textcoords1[] = {
            0.5f,1f, //face 0
            0.5f,0f,
            0f,0f,
            0f,1f,

            1f,0f, //face 1
            0.5f,0f,
            1f,1f,
            0.5f,1f,

            0.5f,1f, //face 2
            0.5f,0f,
            1f,1f,
            1f,0f,

            0.5f,1f, //face 3
            1f,1f,
            0.5f,0f,
            1f,0f,

            0.5f,1f, //face 4
            1f,1f,
            0.5f,0f,
            1f,0f,

            0.5f,0f, //face 5
            1f,0f,
            0.5f,1f,
            1f,1f
    };

    final float textcoords6[] = {
            0.125f,1f, //face 0
            0.125f,0f,
            0f,0f,
            0f,1f,

            0.25f,0f, //face 1
            0.125f,0f,
            0.25f,1f,
            0.125f,1f,

            0.25f,1f, //face 2
            0.25f,0f,
            0.375f,1f,
            0.375f,0f,

            0.375f,1f, //face 3
            0.5f,1f,
            0.375f,0f,
            0.5f,0f,

            0.5f,1f, //face 4
            0.625f,1f,
            0.5f,0f,
            0.625f,0f,

            0.625f,0f, //face 5
            0.75f,0f,
            0.625f,1f,
            0.75f,1f
    };

    public TexCubeShapeGenerator() {
        super();
        texturecoord = textcoords0;
    }

    public void setTextureMode(int mode){
        this.mode = mode;

        if(mode == 6) {
            texturecoord = textcoords6;
        }else if(mode ==1){
            texturecoord = textcoords1;
        }else{
            texturecoord = textcoords0;
        }
    }

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
        return shape.generateShape3D(BufferUtil.makeFloatBuffer(vertexs), BufferUtil.makeShortBuffer(indexs), BufferUtil.makeFloatBuffer(normals), BufferUtil.makeFloatBuffer(texturecoord), indexs.length);

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
if(mode ==0) {
    float[] textcoords2 = new float[]{
            x, 0f, //face 0上面
            0f, 0f,
            x, z,
            0f, z,
            z, 0f, //face 1
            0f, 0f,
            z, y,
            0f, y,
            x, 0f, //face 2下面
            0f, 0f,
            x, z,
            0f, z,
            z, y, //face 3右
            0f, y,
            z, 0f,
            0f, 0f,
            0f, y, //face 4後
            x, y,
            0f, 0f,
            x, 0f,
            0f, y, //face 5前
            x, y,
            0f, 0f,
            x, 0f
    };
    texturecoord = textcoords2;
}

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

