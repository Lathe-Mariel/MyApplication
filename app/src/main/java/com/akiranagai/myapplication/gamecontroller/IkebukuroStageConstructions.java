package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.Premadonna;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;

import java.util.ArrayList;

public class IkebukuroStageConstructions extends StageConstructions {
    GameManager manager;

    public IkebukuroStageConstructions(GameManager manager){
        this.manager = manager;
        Context context = manager.activity.getBaseContext();

        int pictureID1 = new Texture().addTexture(context, R.drawable.ikea); //テクスチャを作成
        int pictureID2 = new Texture().addTexture(context, R.drawable.ikeb); //テクスチャを作成
        int pictureID3 = new Texture().addTexture(context, R.drawable.ikec); //テクスチャを作成
        int pictureID4 = new Texture().addTexture(context, R.drawable.iked); //テクスチャを作成
        int pictureID5 = new Texture().addTexture(context, R.drawable.ikee); //テクスチャを作成
        int pictureID6 = new Texture().addTexture(context, R.drawable.ikeg); //テクスチャを作成
        int pictureID7 = new Texture().addTexture(context, R.drawable.ikef); //テクスチャを作成
        int pictureID8 = new Texture().addTexture(context, R.drawable.ikeh); //テクスチャを作成
        int pictureID9 = new Texture().addTexture(context, R.drawable.ikei); //テクスチャを作成
        int pictureID10 = new Texture().addTexture(context, R.drawable.ikej); //テクスチャを作成
        int pictureID11 = new Texture().addTexture(context, R.drawable.ikek); //テクスチャを作成
        int pictureID12 = new Texture().addTexture(context, R.drawable.ikel); //テクスチャを作成
        int pictureID13 = new Texture().addTexture(context, R.drawable.ikem); //テクスチャを作成

        int pictureID100 = new Texture().addTexture(context, R.drawable.tilez); //テクスチャを作成

        //手前 左 ビル　パネル
        TexObject3D fortress = new TexObject3D();
        int billShape = new TilePanelShapeGenerator().createShape3D(0, 4,4,0,1, 1);
        fortress.setModel(billShape);
        //Texture.setRepeartTexture(fortresswall, true);
        fortress.setTexture(pictureID1);
        fortress.setTranslate(2f, -6f, -10f);
        fortress.setScale(3f,1f,1.5f);
        fortress.setRotate(90, 0,0,1);
        fortress.fullCalcBoundingShape();
        fortress.makeMatrix();
        fortress.setShader(GLES.SP_SimpleTexture);
        objectList.add(fortress);

               //手前 左 ビル2　パネル
        TexObject3D bill2 = new TexObject3D();
        bill2.setModel(billShape);
        //Texture.setRepeartTexture(bill2wall, true);
        bill2.setTexture(pictureID2);
        bill2.setTranslate(2f, -6f, -16f);
        bill2.setScale(3f,1f,1.5f);
        bill2.setRotate(90, 0,0,1);
        bill2.fullCalcBoundingShape();
        bill2.makeMatrix();
        bill2.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill2);

        //手前 左 ビル3　パネル
        TexObject3D bill3 = new TexObject3D();
        bill3.setModel(billShape);
        //Texture.setRepeartTexture(bill3wall, true);
        bill3.setTexture(pictureID3);
        bill3.setTranslate(2f, -6f, -25f);
        bill3.setScale(3f,1f,3f);
        bill3.setRotate(90, 0,0,1);
        bill3.fullCalcBoundingShape();
        bill3.makeMatrix();
        bill3.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill3);

        //手前 右 ビル1　パネル
        TexObject3D bill4 = new TexObject3D();
        bill4.setModel(billShape);
        //Texture.setRepeartTexture(bill4wall, true);
        bill4.setTexture(pictureID6);
        bill4.setTranslate(-2f, -6f, -10f);
        bill4.setScale(3f,1f,1.5f);
        bill4.setRotate(-90, 0,0,1);
        bill4.fullCalcBoundingShape();
        bill4.makeMatrix();
        bill4.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill4);

        //手前 右 ビル2　パネル
        TexObject3D bill5 = new TexObject3D();
        bill5.setModel(billShape);
        //Texture.setRepeartTexture(bill5wall, true);
        bill5.setTexture(pictureID5);
        bill5.setTranslate(-2f, -6f, -16f);
        bill5.setScale(3f,1f,1.5f);
        bill5.setRotate(-90, 0,0,1);
        bill5.fullCalcBoundingShape();
        bill5.makeMatrix();
        bill5.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill5);

        //手前 右 ビル3　パネル
        TexObject3D bill6 = new TexObject3D();
        bill6.setModel(billShape);
        //Texture.setRepeartTexture(bill6wall, true);
        bill6.setTexture(pictureID4);
        bill6.setTranslate(-2f, -6f, -25f);
        bill6.setScale(3f,1f,3f);
        bill6.setRotate(-90, 0,0,1);
        bill6.fullCalcBoundingShape();
        bill6.makeMatrix();
        bill6.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill6);

        //正面 奥ビル
        TexObject3D rightFence2 = new TexObject3D();
        rightFence2.setModel(billShape);
        rightFence2.setTexture(pictureID7);
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -48f, 0f);
        Matrix.scaleM(matrix, 0, 3f,1f,3f);
        rightFence2.calcBoundingShapeByMatrix(matrix);
        rightFence2.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence2);

        //正面 奥ビル右
        TexObject3D bill7 = new TexObject3D();
        bill7.setModel(billShape);
        bill7.setTexture(pictureID8);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -48f, -12f);
        Matrix.scaleM(matrix, 0, 3f,1f,3f);
        bill7.calcBoundingShapeByMatrix(matrix);
        bill7.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill7);

        //正面 奥ビル左
        TexObject3D bill8 = new TexObject3D();
        bill8.setModel(billShape);
        bill8.setTexture(pictureID9);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -48f, 12f);
        Matrix.scaleM(matrix, 0, 3f,1f,3f);
        bill8.calcBoundingShapeByMatrix(matrix);
        bill8.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill8);

        // 裏　奥ビル右
        TexObject3D bill9 = new TexObject3D();
        bill9.setModel(billShape);
        bill9.setTexture(pictureID13);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -7f, 12f);
        Matrix.scaleM(matrix, 0, 3f,2f,3f);
        bill9.calcBoundingShapeByMatrix(matrix);
        bill9.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill9);

        // 裏　奥ビル左
        TexObject3D bill10 = new TexObject3D();
        bill10.setModel(billShape);
        bill10.setTexture(pictureID11);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -7f, -12f);
        Matrix.scaleM(matrix, 0, 3f,2f,3f);
        bill10.calcBoundingShapeByMatrix(matrix);
        bill10.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill10);

        //センターロード　下部パネル(roadModel 縦長)
        TexObject3D road = new TexObject3D();
        int roadModelID = new TilePanelShapeGenerator().createShape3D(0,24f,43f,0f,48,43);
        road.setModel(roadModelID);
        Texture.setRepeartTexture(pictureID100, true);
        road.setTexture(pictureID100);
        road.setTranslate(0f, -4f, 0f);
        road.setScale(4f,1f,4f);
        road.makeMatrix();
        road.setShader(GLES.SP_SimpleTexture);
        objectList.add(road);

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
        kps.setBackColor(Color.rgb(255,255,180));
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
        Premadonna prema = manager.prema;
        prema.translate(prema.getX(),prema.getY(),prema.getZ()-20, 0,0);
        manager.renderer.setGLClearColor(0,0,0);

    }
}
