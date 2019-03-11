package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.Shape3D;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TexSphereShapeGenerator;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;

import java.util.ArrayList;

public class SpaceStageConstructions extends StageConstructions {

    public SpaceStageConstructions(GameManager manager){
        Context context = manager.activity;
        manager.renderer.setBackTexture(new Texture().addTexture(context, R.drawable.spacebackd));

        int jupiterPictureID = new Texture().addTexture(context, R.drawable.jupiter); //テクスチャを作成
        int marsPictureID = new Texture().addTexture(context, R.drawable.mars); //テクスチャを作成
        //int house1PictureID = new Texture().addTexture(context, R.drawable.brihousea); //テクスチャを作成


        TexObject3D sphere1 = new TexObject3D();
        int sphereShapeID = new TexSphereShapeGenerator().createShape3D(0, 360, -90, 90, 12,12);
        sphere1.setModel(sphereShapeID);
        sphere1.setTranslate(5,0,0);
        sphere1.setTexture(jupiterPictureID);
        sphere1.makeMatrix();
        sphere1.setShader(GLES.SP_TextureWithLight);
        objectList.add(sphere1);

        TexObject3D sphere2 = new TexObject3D();
        sphere2.setModel(sphereShapeID);
        sphere2.setTranslate(-5,0,0);
        sphere2.setTexture(marsPictureID);
        sphere2.makeMatrix();
        sphere2.setShader(GLES.SP_TextureWithLight);
        objectList.add(sphere2);

        //左 枠　パネル
        Object3D fortress = new TexObject3D();
        int fortPanel = new TilePanelShapeGenerator().createShape3D(0, 6,22,0,44, 1);
        fortress.setModel(fortPanel);
        fortress.setTranslate(-1f, -49.5f, 0f);
        fortress.setScale(1f,1f,4.5f);
        fortress.setRotate(90, 0,0,1);
        fortress.fullCalcBoundingShape();
        fortress.makeMatrix();
        fortress.setColor(0,0,0,0);
        fortress.setShader(GLES.SP_SimpleObject);
        objectList.add(fortress);

        //奥 枠 パネル
        float[] matrix;
        Object3D fortress1 = new TexObject3D();
        fortress1.setModel(fortPanel);
        matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix,0, 0,-1, 45.8f);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, 90, 0,0,1);
        Matrix.scaleM(matrix, 0, 1,1,4.5f);
        //fortress1.setCalcTranslate(new float[]{42,-1,48});
        //fortress1.fullCalcBoundingShape();
        fortress1.calcBoundingShapeByMatrix(matrix);
        //fortress1.makeMatrix();
        fortress1.setColor(0,0,0,0);
        fortress1.setShader(GLES.SP_SimpleObject);
        objectList.add(fortress1);

        //右 枠 パネル
        Object3D fortress2 = new TexObject3D();
        fortress2.setModel(fortPanel);
        matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix,0, -48,-1, 0f);
        Matrix.rotateM(matrix, 0, -90,0,0,1);
        Matrix.scaleM(matrix, 0, 1,1,4.5f);
        //fortress2.setCalcTranslate(new float[]{42,-1,48});
        //fortress2.fullCalcBoundingShape();
        fortress2.calcBoundingShapeByMatrix(matrix);
        //fortress2.makeMatrix();
        fortress2.setColor(0,0,0,0);
        fortress2.setShader(GLES.SP_SimpleObject);
        objectList.add(fortress2);

        //手前 枠 パネル
        Object3D fortress3 = new TexObject3D();
        fortress3.setModel(fortPanel);
        matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix,0, 0,-1, -48f);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.scaleM(matrix, 0, 1,1,4.5f);
        //fortress3.setCalcTranslate(new float[]{42,-1,48});
        //fortress3.fullCalcBoundingShape();
        fortress3.calcBoundingShapeByMatrix(matrix);
        //fortress3.makeMatrix();
        fortress3.setColor(0,0,0,0);
        fortress3.setShader(GLES.SP_SimpleObject);
        objectList.add(fortress3);

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
        kps.setForeColor(Color.rgb(255,255,255));
        kps.setBackColor(Color.rgb(150,150,255));
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
        questionObject.setScale(0.01f,0.01f,0.02f);
        questionObject.setColor(0.55f,1,0.6f,1);
        questionObject.setTranslate(0,16,1.8f);
        questionObject.setRotate(90,1,0,0);
        questionObject.fullCalcBoundingShape();
        //questionObject.makeMatrix();
        questionObject.setShader(GLES.SP_ObjectWithLight2);
    }

    @Override
    public void startStage() {

    }
}
