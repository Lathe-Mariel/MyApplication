package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.BufferUtil;

/**
 * 表面が下向きのパネル
 */
public class LowerFacePanelShapeGenerator extends CreatableShape {

    //頂点座標
    static private float[] vertexs= new float[4*3];

    //頂点座標番号列
    static final private short[] indexs= {
            0,1,2,3
    };
    //拡頂点の法線ベクトル
    static final private float[] normals= {
            0f,-1f,0f,
            0f,-1f,0f,
            0f,-1f,0f,
            0f,-1f,0f
    };
    private float[] textcoords = new float[]{
            0f, 1f, //左上 0
            1f,1f, //左下 2
            0f,0f, //右上 1
            1f, 0f //右下 3
    };

    @Override
    public int createShape3D(int id){
        return createShape3D(id, 100f, 100f);
    }
    @Override
    public int createShape3D(int id, float x){
        return createShape3D(id, x, x);
    }

    /**
     *
     * @param id ShapeID 形状IDを指定してShape生成する場合(Shape3Dクラスでstaticにリストされる) 0の場合はID自動生成.
     * @param x width.
     * @param z height.
     * @return ShapeID
     */
    @Override
    public int createShape3D(int id, float x, float z){
        float top=-z*.5f;
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

    /**
     *
     * @param id
     * @param x width.
     * @param z height.
     * @param a researve.
     * @param divX X方向タイル(Texture)幅.
     * @param divZ Z方向タイル(Texture)幅 (奥行方向).
     * @return ShapeID.
     */
    @Override
    public int createShape3D(int id, float x, float z, float a, int divX, int divZ){
        textcoords = new float[]{
                0f, 0f, //左上 0
                0f, divZ, //左下 2
                divX, 0f, //右上 1
                divX, divZ  //右下 3
        };
        return createShape3D(id, x, z);
    }
}
