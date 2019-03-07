package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

public class Shape3D {
    FloatBuffer vertexBuffer;
    ShortBuffer indexBuffer;
    FloatBuffer normalBuffer;
    FloatBuffer texcoordBuffer;
    int nIndexs;
    //protected int nPoints;
    private int shapeID =1;

    int stripOrder = GLES20.GL_TRIANGLE_STRIP;

    private static int createdObjects = 1000; //オブジェクト番号の割り当て開始番号(ID指定生成、と自動生成を分けるため。自動生成の場合割り当ては1000～)
    /*
    作成済みオブジェクトとIDのマップ
     */
    private static HashMap<Integer, Shape3D> identicalShape3DList = new HashMap<>();

    /**
    ID自動生成（1000～)コンストラクタ
     */
    public Shape3D(){
    }

    /**
    呼び出し側　ID指定コンストラクタ
     * @param ID 指定ID
     */
    public Shape3D(int ID){
        shapeID = ID;
        if(identicalShape3DList.containsKey(ID)){  //IDが０またはダブっているなら、新しいIDを生成するためにいったん０に設定
            ID = 0;
        }
    }

    /**
     *
     * @param v //頂点配列
     * @param i //頂点番号配列
     * @param n
     * @param t
     * @param ni 頂点数　(=index.length)
     * @return
     */
    public int generateShape3D(FloatBuffer v, ShortBuffer i, FloatBuffer n, FloatBuffer t, int ni){

        vertexBuffer = v;
        indexBuffer = i;
        normalBuffer = n;
        texcoordBuffer = t;
        nIndexs = ni;
        if(shapeID != 0 && identicalShape3DList.containsValue(shapeID)) {
            shapeID = createdObjects++;
            identicalShape3DList.put(shapeID, this);
            return shapeID;
        }else{
            if(shapeID ==0)
                shapeID = createdObjects++;
            identicalShape3DList.put(shapeID, this);
        }
        identicalShape3DList.put(shapeID,this);
        return shapeID;
    }

    public static boolean isExistShape(int ID){
        if(identicalShape3DList.containsKey(ID)){
            return true;
        }
        return false;
    }

    public static Shape3D getShape3D(int ID){
        return identicalShape3DList.get(ID);
    }

    public int getShapeID(){
        return shapeID;
    }

    public void setCcwState(int state) {
        if (state == GLES20.GL_TRIANGLE_STRIP || state == GLES20.GL_TRIANGLES) {
            stripOrder = state;
        } else {
            Log.d("error", "指定された値は、GL_TRIANGLE_STRIPでもGL_TRIANGLESでもありません");
        }
        return;
    }
}
