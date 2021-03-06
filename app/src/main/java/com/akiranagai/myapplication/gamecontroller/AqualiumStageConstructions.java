package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.PipeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;

import java.util.ArrayList;

public class AqualiumStageConstructions extends StageConstructions {

    AqualiumStageConstructions(GameManager manager){
        Context context = manager.activity.getBaseContext();

        int back1 = new Texture().addTexture(context, R.drawable.rockc);  //前景1テクスチャ作成
        int aneID = new Texture().addTexture(context, R.drawable.seaanemone);  //前景1テクスチャ作成

        //右奥 中景パネル
        TexObject3D backPanel2 = new TexObject3D();
        backPanel2.setModel(new TilePanelShapeGenerator().createShape3D(0, 2));
        backPanel2.setTexture(back1);
        backPanel2.setTranslate(-28.8f, -100.f, 2f);
        backPanel2.setScale(8,1f,8f);
        backPanel2.setRotate(-90, 1,0,0);
        backPanel2.makeMatrix();
        backPanel2.setShader(GLES.SP_SimpleTexture);
        objectList.add(backPanel2);

        TexObject3D seaAnemone = new TexObject3D();
        seaAnemone.setModel(new PipeShapeGenerator().createShape3D(0, 2,0.5f, (float)Math.PI*2, 12,1));
        seaAnemone.setTexture(aneID);
        seaAnemone.setTranslate(0,-3, 10);
        seaAnemone.fullCalcBoundingShape();
        seaAnemone.setShader(GLES.SP_SimpleTexture);
        objectList.add(seaAnemone);

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
    public void setQuestion(Object3D questionObject){}
    @Override
    public void startStage(){}
}
