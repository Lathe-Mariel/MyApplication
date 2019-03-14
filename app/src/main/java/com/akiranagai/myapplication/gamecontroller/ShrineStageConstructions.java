package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.StlModel;
import com.akiranagai.myapplication.object3d.Tex4PanelShapeGenerator;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;

import java.util.ArrayList;

public class ShrineStageConstructions extends StageConstructions {

    public ShrineStageConstructions(GameManager manager){
        Context context = manager.activity.getBaseContext();

        int back1 = new Texture().addTexture(context, R.drawable.shrinea);  //   八坂神社
        int fence2PictureID = new Texture().addTexture(context, R.drawable.wish);  //   フェンステクスチャ2
        int handyLightID = new Texture().addTexture(context, R.drawable.lumpa);  //提灯

        //右奥 中景パネル
        TexObject3D backPanel2 = new TexObject3D();
        backPanel2.setModel(new TilePanelShapeGenerator().createShape3D(0, 2));
        backPanel2.setTexture(back1);
        backPanel2.setTranslate(-28.8f, -100.f, 2f);
        backPanel2.setScale(36,1f,8f);
        backPanel2.setRotate(-90, 1,0,0);
        backPanel2.makeMatrix();
        backPanel2.setShader(GLES.SP_SimpleTexture);
        objectList.add(backPanel2);

        //絵馬パネル
        TexObject3D rightFence = new TexObject3D();
        int fenceObjectShape = new TilePanelShapeGenerator().createShape3D(0, 1.5f, 6, 0, 6,1);
        rightFence.setModel(fenceObjectShape);
        Texture.setRepeartTexture(fence2PictureID, true);
        rightFence.setTexture(fence2PictureID);
        rightFence.setTranslate(3f, -45.f, -1f);
        rightFence.setScale(1.5f,1.5f,4.5f);
        rightFence.setRotate(-90, 0,0,1);
        rightFence.fullCalcBoundingShape();
        //rightFence.makeMatrix();
        rightFence.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence);

        Object3D tourou = new Object3D();
        tourou.setModel(new StlModel(context, "tourou.stl").createShape3D(0));
        tourou.setShader(GLES.SP_ObjectWithLight2);
        tourou.setTranslate(10,-3.5f,10);
        tourou.setScale(0.6f, 0.6f, 0.6f);
        tourou.setColor(0.6f, 0.6f, 0.65f, 1);
        tourou.fullCalcBoundingShape();
        objectList.add(tourou);

        //提灯
        TexObject3D waku = new TexObject3D();
        waku.setModel(new Tex4PanelShapeGenerator().createShape3D(0,2));
        waku.setTexture(handyLightID);
        waku.setTranslate(0,0,0);
        waku.setScale(3,1,3);
        waku.makeMatrix();
        waku.setShader(GLES.SP_SimpleTexture);
        objectList.add(waku);

        //キー入力パネル
        KeyPanelSet kps = new KeyPanelSet();
        final float y = -3.5f;
        final float x1 = -30;
        final float x2 = -25;
        float[] keyPositionArray = new float[]{
                x1, 2, -38,  //0A
                25, -1, -6,
                -40, y, 34,
                x1, 1, 16,
                -35, -3.2f, -50,  //E
                -38, -2, 20,  //5
                -45, -3.5f, -22,
                x1,-3.4f,39,  //H
                31,1,-36,
                10,-2,-42,  //J
                x2, y, 10,  //10K
                -38, 0, -5,  //L
                -5, y, -3,  //12M
                32, y, -18,
                12, -3.5f, 3f,  //O
                31, 10, 8,  //P
                20, 0.5f, 36,  //Q
                18,3,24,  //17R
                5,y,26,  //S
                x2,y,28,  //T
        };

        kps.setPanelShapeID(new TexCubeShapeGenerator().createShape3D(0, 0.8f));
        kps.setForeColor(Color.rgb(255,0,0));
        kps.setBackColor(Color.rgb(150,250,50));
        kps.createAlphabets(0, 19, keyPositionArray);
        TexObject3D cubeAlpha = kps.make6Cube("UVWXYZ", new int[]{20,21,22,23,24,25});
        cubeAlpha.setTranslate(18, 0, 8);
        cubeAlpha.makeMatrix();
        ArrayList<TexObject3D> list;
        list = kps.getPanels();
        objectList.addAll(list);
        manager.sInput.setBuckBufferObjectList(list);  //バックバッファに設定する表示オブジェクトを登録(Alphabet Boxes)
    }

    @Override
    public void setQuestion(Object3D questionObject) {

    }

    @Override
    public void startStage() {

    }
}
