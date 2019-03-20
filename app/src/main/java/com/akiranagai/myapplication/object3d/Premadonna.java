package com.akiranagai.myapplication.object3d;

import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.gamecontroller.GLStageRenderer;
import com.akiranagai.myapplication.gamecontroller.GameManager;
import com.akiranagai.myapplication.gamecontroller.QuaterSpaceManager;

public abstract class Premadonna extends TexObject3D {
    private GameManager manager;
    private QuaterSpaceManager qsManager;

    int hp;  //ヒットポイント
    int maxHp;
    float viewDirection;  //自機の向き
    float viewArrowX, viewArrowZ, viewArrowY=-1f;  //自機の向きベクトル
    GLStageRenderer renderer;

    private TexObject3D directionPanel;

    public Premadonna(GameManager manager){
        this.manager = manager;
        this.qsManager = manager.getQuaterSpaceManager();
        setMovable(true);
    }

    public void affectHp(double delta){
        hp += delta;
        if(hp < 0)
            hp = 0;
    }

    public void crash(double value){
          //      renderer.putToast2(manager.CRASH_ID, 500);
        affectHp(value);
    }

    public int getMaxHp(){
        return maxHp;
    }
    public int getHp(){
        return hp;
    }

    /**
     * 東西南北を表す、directionオブジェクト
     * @param panelObject
     */
    public void setDirectionPanel(TexObject3D panelObject){
        this.directionPanel = panelObject;
        renderer.addFrontObject(panelObject);
    }

    public abstract void touched(Object3D o3);

    public void setRenderer(GLStageRenderer renderer){
        this.renderer = renderer;
    }
    public float getX(){        return translateValues[0];    }
    public float getY(){        return translateValues[1];    }
    public float getZ(){        return translateValues[2];    }
    public void setX(float x) { translateValues[0] = x; }
    public void setY(float y){ translateValues[1] = y;    }
    public void setZ(float z){ translateValues[2] = z;    }

    public void translate(float x, float y, float z, float direction, float deltaAlfa){
        calcTranslate[0] = x;
        calcTranslate[1] = y;
        calcTranslate[2] = z;


        synchronized (manager.renderer) {
            reCalcBoundingShape();
            qsManager.registObject3D(Premadonna.this);  //移動したので干渉チェックマネージャーへ位置　再登録通知
            qsManager.checkCollision();  //干渉チェック

            setViewDirection(direction);
            Matrix.setIdentityM(mMatrix, 0);
            Matrix.translateM(mMatrix, 0, translateValues[0], translateValues[1], translateValues[2]);
            //Matrix.scaleM(mMatrix, 0, 0.05f, 0.05f, 0.05f);
            Matrix.rotateM(mMatrix, 0, (int) ((direction + deltaAlfa * -4) * 360 / 6.28), 0, 1, 0);
            Log.d("messager", "HP: " + hp);
        }
    }
    public float getViewArrowX(){
        return viewArrowX;
    }
    public float getViewArrowZ(){
        return viewArrowZ;
    }
    public void setViewDirection(float direction){
        viewDirection = (direction*100 %628)/100;
        if(viewDirection < 0)
            viewDirection += 6.28f;
        Log.d("message", "viewDirection: " + viewDirection);

        viewArrowX = (float)(translateValues[0] + 4* (Math.sin(viewDirection)));
        viewArrowZ = (float)(translateValues[2] + 4* (Math.cos(viewDirection)));

        if(directionPanel ==null)return;
        directionPanel.setTranslate(viewDirection -3.2f, 0.92f, -1f);
        directionPanel.makeMatrix();
    }
    public float getViewDirection(){
        return viewDirection;
    }
    public abstract void init();


}
