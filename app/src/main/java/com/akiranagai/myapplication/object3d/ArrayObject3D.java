package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;

public class ArrayObject3D extends Object3D {
    float[][] objectArray;
    int time;
//    float[] xyz = new float[3];
    private int incrementValue=1;

    public ArrayObject3D(int rowNumber, int columnNumber) {
        objectArray = new float[rowNumber*columnNumber][16];

        for (int i = 0; i < rowNumber; i++) {
            for(int j =0; j< columnNumber; j++) {
                Matrix.setIdentityM(objectArray[j+i*columnNumber], 0);
                Matrix.translateM(objectArray[j+i*columnNumber], 0, (float) ((0.3f + ((i+2)*(j+2) % 3 / 10.0)) * (j+1)), (float)(Math.sin((i+2)*(j+1))*2.5f), (i+1)*0.3f+(float)(((i+1)%4)/16.0));
            }
        }

    }

    /**
     * timeの増分値.
     * @param value
     */
    public void setIncrementValue(int value){
        incrementValue = value;
    }

    @Override
    public Object3D setTranslate(float x, float y, float z) {
        for (int i = 0; i < objectArray.length; i++) {
            //Matrix.setIdentityM(objectArray[i], 0);
            Matrix.translateM(objectArray[i], 0, x, y, z);
        }
        return this;
    }

    @Override
    public void render() {
        if (GLES.getCurrentProgram() != shaderSelectNumber)
            GLES.selectProgram(shaderSelectNumber);
        for (int i = 0; i < objectArray.length; i++) {
            GLES.updateMatrix2(objectArray[i]);
            draw();
        }

        time += incrementValue;
        time %= 250;
    }

    public void draw() {
        //頂点点列

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, model.vboNumber);
        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


            //shadingを使わない時に使う単色の設定 (r, g, b,a)
            //GLES20.glUniform4f(GLES.objectColorHandle, r, g, b, a);
            GLES20.glUniform1i(GLES.timeHandle, time);

        //
        model.indexBuffer.position(0);
        GLES20.glDrawElements(model.stripOrder, model.nIndexs, GLES20.GL_UNSIGNED_SHORT, model.indexBuffer);
    }
}