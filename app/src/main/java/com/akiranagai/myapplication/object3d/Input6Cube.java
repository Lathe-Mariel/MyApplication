package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.Texture;

import java.util.Timer;
import java.util.TimerTask;

public class Input6Cube extends TexObject3D {
    private int backTextureID = 0;
    private TexObject3D innerObject3D;
    private final float INNER_BIG = 0.95f;  //インナーオブジェクト選択時のサイズ（アウターに対する割合 3軸共通)
    private Timer spinTimer;

    public Input6Cube(){
        super();
        innerObject3D = new TexObject3D();
        innerObject3D.setColor(0.2f,0.2f,0.5f, 0.2f);

        spinTimer = new Timer();  //自転タイマー
        spinTimer.schedule(new TimerTask(){
            public void run(){
                spinX(2.0f);
            }
        }, 2000, 100);
    }

    public Input6Cube setBackTexture(int textureID){
        this.backTextureID = textureID;
        return this;
    }

    public Input6Cube setTranslate(float x, float y, float z){
        super.setTranslate(x,y,z);
        innerObject3D.setTranslate(x,y,z);
        return this;
    }

    public Input6Cube setModel(int modelID){
        super.setModel(modelID);
        innerObject3D.setModel(modelID);
        return this;
    }

    public Input6Cube setRotate(int r, float x, float y, float z){
        super.setRotate(r, x, y, z);
        innerObject3D.setRotate(r, x, y, z);
        return this;
    }

    public Input6Cube setScale(float x, float y, float z){
        super.setScale(x, y, z);
        innerObject3D.setScale(x*0.12f, y*0.12f, z*0.12f);
        return this;
    }

    public Input6Cube setShader(int number){
        super.setShader(number);
        innerObject3D.setShader(GLES.SP_SimpleObject);
        return this;
    }

    @Override
    public void makeMatrix(){
        super.makeMatrix();
        innerObject3D.makeMatrix();
    }

    public synchronized void spinX(float value){
        Matrix.rotateM(mMatrix, 0, value, 1,0,0);
        Matrix.translateM(innerObject3D.mMatrix, 0, -movedDirection[0], -movedDirection[1], -movedDirection[2]);
        Matrix.rotateM(innerObject3D.mMatrix, 0, value, 1,0,0);
        Matrix.translateM(innerObject3D.mMatrix, 0, movedDirection[0], movedDirection[1], movedDirection[2]);
    }

    @Override
    public void render(){
        super.render();
        //GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        //GLES20.glPolygonOffset(-2f,1f);  //Zファイティング対策のポリゴンオフセット ON(Function of PolygonOffset ON to handle Z fighting.)
        innerObject3D.render();
        //GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
    }

    @Override
    public void backRender(){
        if(backTextureID == 0) {
            GLES.selectProgram(GLES.SP_SimpleObject);
            GLES.updateMatrix(mMatrix);
            draw(1f, 1f, idcolor, 1f, 1f);
        }else{
            Texture.setTexture(backTextureID);
            GLES.selectProgram(GLES.SP_SimpleTexture);
            GLES.updateMatrix(mMatrix);
            draw(1f,1f,1f,1f,1f);
        }
    }

    float[] movedDirection = new float[3];
    boolean isBig;
    /**
     * inner オブジェクトを選択した面方向へ移動
     * @param faceNumber
     */
    public void selectFace(int faceNumber){
        float[] direction = new float[3];

        int realNumber=-1;
        switch(faceNumber){
            case 0:
                realNumber =0;
                break;
            case 1:
                realNumber =3;
                break;

            case 2:
                realNumber =5;
                break;

            case 3:
                realNumber =1;
                break;
            case 4:
                realNumber =2;
            break;
            case 5:
                realNumber =4;
                break;
        }

        Shape3D shape = Shape3D.getShape3D(modelID);
        if(realNumber != -1) {
            shape.normalBuffer.position(realNumber * 4);
            shape.normalBuffer.get(direction, 0, 3);
            for (int i = 0; i < 3; i++) {
                direction[i] *= 0.08f;
            }
            //Matrix.scaleM(innerObject3D.mMatrix, 0, INNER_BIG, INNER_BIG, INNER_BIG);
            if(!isBig) {
                Matrix.scaleM(innerObject3D.mMatrix, 0, 8, 8, 8);
                isBig =true;
            }
//            innerObject3D.setScale(INNER_BIG,INNER_BIG,INNER_BIG);
        }else{  //2回目タップ(決定)
            Matrix.translateM(innerObject3D.mMatrix, 0, -movedDirection[0], -movedDirection[1], -movedDirection[2]);
            Matrix.scaleM(innerObject3D.mMatrix, 0, 0.125f, 0.125f, 0.125f);
            isBig = false;
            movedDirection = direction;
            return;
        }
        //innerObject3D.setTranslate(translateValues[0]+direction[0], translateValues[1] + direction[1], translateValues[2] + direction[2]);
        //innerObject3D.makeMatrix();
        synchronized (this) {
            Matrix.translateM(innerObject3D.mMatrix, 0, direction[0] - movedDirection[0], direction[1] - movedDirection[1], direction[2] - movedDirection[2]);
            movedDirection = direction;
        }
    }
}