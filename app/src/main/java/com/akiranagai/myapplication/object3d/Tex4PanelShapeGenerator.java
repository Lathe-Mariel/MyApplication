package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;

import com.akiranagai.myapplication.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Tex4PanelShapeGenerator extends CreatableShape {
    //bufferの定義
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texcoordBuffer;

    private float[] texturecoord;
    private int mode =0;

    //頂点座標
    final private float[] vertexs={

            -1f,1f,1f,//P0
            -1f,-1f,1f,//P1
            -1f,1f,-1f,//P2
            -1f,-1f,-1f,//P3

            1f,1f,-1f,//P4
            1f,-1f,-1f,//P5

            1f,1f,1f,//P6
            1f,-1f,1f,//P7


    };
    //頂点座標番号列
    final private short[] indexs= {
            0,1,2,3, 4,5, 6,7, 0,1, 6,7, 4,5, 2,3, 0,1
    };
    private int numIndexs = indexs.length;
    //各頂点の法線ベクトル
    final private float[] normals={
            1,0,0,  //P0
            1,0,0,  //P1
            1,0,0,  //P2
            1,0,0,  //P3

            0,0,1,  //P4
            0,0,1,  //P5

            -1,0,0,  //P6
            -1,0,0,  //P7

            0,0,-1,  //P8
            0,0,-1,  //P9


            0,0,1,
            0,0,1,

            1,0,0,
            1,0,0,

            0,0,-1,
            0,0,-1,

            -1,0,0,
            -1,0,0
    };

    final float textcoords0[] = {
            1f,0f, //face 0左
            1f,1f,
            0f,0f,
            0f,1f,

            1f,0f, //face 1奥
            1f,1f,

            0f,0f, //face 2右
            0f,1f,

            1f,0f, //face 3前
            1f,1f,


            1f,0f,
            1f,1f,

            0f,0f,
            0f,1f,

            1f,0f,
            1f,1f,

            1f,0f,
            1f,1f
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

    public Tex4PanelShapeGenerator() {
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
