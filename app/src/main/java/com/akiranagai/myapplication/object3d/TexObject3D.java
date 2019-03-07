package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.Texture;

import java.util.HashMap;

public class TexObject3D extends Object3D {
    private int textureID = 0;

    public TexObject3D setTexture(int textureID) {
        this.textureID = textureID;
        return this;
    }

    @Override
    public void render() {
        GLES.selectProgram(shaderSelectNumber);

        Texture.setTexture(textureID);
        GLES.updateMatrix(mMatrix);
        draw(colorValues[0], colorValues[1], colorValues[2], colorValues[3], colorValues[4]);
    }
    public void backRender(){
            GLES.selectProgram(GLES.SP_SimpleObject);
            GLES.updateMatrix(mMatrix);
            draw(1f, 1f, idcolor, 1f, 1f);
    }

    @Override
    public void draw(float r, float g, float b, float a, float shininess) {
        //頂点点列のテクスチャ座標
            GLES20.glVertexAttribPointer(GLES.texcoordHandle, 2,
                    GLES20.GL_FLOAT, false, 0, model.texcoordBuffer);
            //頂点点列
            GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                    GLES20.GL_FLOAT, false, 0, model.vertexBuffer);

        int k = GLES20.glGetError();
        if (GLES.checkLiting()) {
            //頂点での法線ベクトル
            GLES20.glVertexAttribPointer(GLES.normalHandle, 3,
                    GLES20.GL_FLOAT, false, 0, model.normalBuffer);

            //周辺光反射
            GLES20.glUniform4f(GLES.materialAmbientHandle, r, g, b, a);

            //拡散反射
            GLES20.glUniform4f(GLES.materialDiffuseHandle, r, g, b, a);

            //鏡面反射
            GLES20.glUniform4f(GLES.materialSpecularHandle, 1f, 1f, 1f, a);
            GLES20.glUniform1f(GLES.materialShininessHandle, shininess);

        } else {
            //shadingを使わない時に使う単色の設定 (r, g, b,a)
            //Log.d("else", "program" + GLES.getCurrentProgram());
            GLES20.glUniform4f(GLES.objectColorHandle, r, g, b, a);
        }

        //描画
        model.indexBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, model.nIndexs, GLES20.GL_UNSIGNED_SHORT, model.indexBuffer);
    }

    @Override
    public TexObject3D clone(){
        final TexObject3D object = new TexObject3D();
        copyAttributes(object);
        new Thread(new Runnable(){
            boolean textureIDReady = false;

            public void run() {  //設定ファイル読み込み時　Texture のみGL Threadで実行のため、diplicaate class した場合、待ちが必要
                while (!textureIDReady) {
                    if (textureID != 0) {
                        textureIDReady = true;
                    }
                    try{
                        Thread.sleep(200);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                ((TexObject3D)object).setTexture(TexObject3D.this.textureID);
            }
        }).start();
        return object;
    }
}
