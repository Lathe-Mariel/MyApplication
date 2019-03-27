package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.CircleRail;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.LowerFacePanelShapeGenerator;
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

        manager.renderer.setBackTexture(new Texture().addTexture(context, R.drawable.ikesora));

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
        int pictureID14 = new Texture().addTexture(context, R.drawable.ikeo); //テクスチャを作成
        int pictureID18 = new Texture().addTexture(context, R.drawable.ikep); //テクスチャを作成
        int pictureID16 = new Texture().addTexture(context, R.drawable.ikeq); //テクスチャを作成
        int pictureID17 = new Texture().addTexture(context, R.drawable.iker); //テクスチャを作成
        int pictureID19 = new Texture().addTexture(context, R.drawable.ikes); //テクスチャを作成
        int pictureID20 = new Texture().addTexture(context, R.drawable.iket); //テクスチャを作成
        int pictureID21 = new Texture().addTexture(context, R.drawable.ikeu); //テクスチャを作成
        int pictureID22 = new Texture().addTexture(context, R.drawable.ikev); //テクスチャを作成

        int fence2PictureID = new Texture().addTexture(context, R.drawable.fenceb);  //   フェンステクスチャ2

        int moguID1 = new Texture().addTexture(context, R.drawable.sunobonekomini); //テクスチャを作成
        int pictureID15 = new Texture().addTexture(context, R.drawable.fireworksa); //テクスチャを作成

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
        TexObject3D bill = new TexObject3D();
        bill.setModel(billShape);
        bill.setTexture(pictureID7);
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -48f, 0f);
        Matrix.scaleM(matrix, 0, 3f,1f,3f);
        bill.calcBoundingShapeByMatrix(matrix);
        bill.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill);

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

        //正面 奥ビル左左
        TexObject3D bill21 = new TexObject3D();
        bill21.setModel(billShape);
        bill21.setTexture(pictureID21);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -48f, 22f);
        Matrix.scaleM(matrix, 0, 3f,1f,2f);
        bill21.calcBoundingShapeByMatrix(matrix);
        bill21.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill21);

        // 裏　奥ビル右 王将
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

        //90度<前　Y+ 右Z+>
        TexObject3D bill24 = new TexObject3D();
        bill24.setModel(billShape);
        bill24.setTexture(pictureID13);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -1f, 24f);
        Matrix.scaleM(matrix, 0, 3f,2f,3f);
        bill24.calcBoundingShapeByMatrix(matrix);
        bill24.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill24);

        // 裏　奥ビル左 甲子園
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

        //
        TexObject3D bill20 = new TexObject3D();
        bill20.setModel(billShape);
        bill20.setTexture(pictureID19);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -7f, -22f);
        Matrix.scaleM(matrix, 0, 3f,2f,2f);
        bill20.calcBoundingShapeByMatrix(matrix);
        bill20.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill20);

        //手前 左奥広場 ビル　パネル Y-mobile
        TexObject3D eki0 = new TexObject3D();
        eki0.setModel(billShape);
        //Texture.setRepeartTexture(ekiwall, true);
        eki0.setTexture(pictureID10);
        eki0.setTranslate(0f, -26f, -3f);
        eki0.setScale(2f,1f,2.f);
        eki0.setRotate(90, 0,0,1);
        eki0.fullCalcBoundingShape();
        eki0.makeMatrix();
        eki0.setShader(GLES.SP_SimpleTexture);
        objectList.add(eki0);

        //手前 左奥広場 ビル　パネル 階段ビル
        TexObject3D eki3 = new TexObject3D();
        eki3.setModel(billShape);
        //Texture.setRepeartTexture(ekiwall, true);
        eki3.setTexture(pictureID17);
        eki3.setTranslate(0f, -26f, 5f);
        eki3.setScale(2f,1f,2.f);
        eki3.setRotate(90, 0,0,1);
        eki3.fullCalcBoundingShape();
        eki3.makeMatrix();
        eki3.setShader(GLES.SP_SimpleTexture);
        objectList.add(eki3);

        //手前 左奥広場 ビル　パネル SEIBU
        TexObject3D eki4 = new TexObject3D();
        eki4.setModel(billShape);
        //Texture.setRepeartTexture(ekiwall, true);
        eki4.setTexture(pictureID20);
        eki4.setTranslate(0f, -26f, 15f);
        eki4.setScale(2f,1f,3.f);
        eki4.setRotate(90, 0,0,1);
        eki4.fullCalcBoundingShape();
        eki4.makeMatrix();
        eki4.setShader(GLES.SP_SimpleTexture);
        objectList.add(eki4);

        //手前 左奥広場 ビル　パネル
        TexObject3D eki = new TexObject3D();
        eki.setModel(billShape);
        //Texture.setRepeartTexture(ekiwall, true);
        eki.setTexture(pictureID12);
        eki.setTranslate(0f, -26f, 28f);
        eki.setScale(2f,1f,3.5f);
        eki.setRotate(90, 0,0,1);
        eki.fullCalcBoundingShape();
        eki.makeMatrix();
        eki.setShader(GLES.SP_SimpleTexture);
        objectList.add(eki);

        //手前 左奥広場 ビル　パネル bigcamera大<上がX+>
        TexObject3D eki2 = new TexObject3D();
        eki2.setModel(billShape);
        //Texture.setRepeartTexture(ekiwall, true);
        eki2.setTexture(pictureID14);
        eki2.setTranslate(1.8f, -26f, 42f);
        eki2.setScale(2.8f,1f,3.5f);
        eki2.setRotate(90, 0,0,1);
        eki2.fullCalcBoundingShape();
        eki2.makeMatrix();
        eki2.setShader(GLES.SP_SimpleTexture);
        objectList.add(eki2);

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

        //手前 右奥広場 ビル1　パネル 4つ谷2<上 X->
        TexObject3D bill14 = new TexObject3D();
        bill14.setModel(billShape);
        //Texture.setRepeartTexture(bill14wall, true);
        bill14.setTexture(pictureID6);
        bill14.setTranslate(-1.6f, -18f, -4f);
        bill14.setScale(2.8f,1f,1.5f);
        bill14.setRotate(-90, 0,0,1);
        bill14.fullCalcBoundingShape();
        bill14.makeMatrix();
        bill14.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill14);

        //手前 右奥広場 ビル2　パネル　オフィス
        TexObject3D bill15 = new TexObject3D();
        bill15.setModel(billShape);
        //Texture.setRepeartTexture(bill14wall, true);
        bill15.setTexture(pictureID16);
        bill15.setTranslate(-2f, -18f, 32.6f);
        bill15.setScale(3f,1f,2.5f);
        bill15.setRotate(-90, 0,0,1);
        bill15.fullCalcBoundingShape();
        bill15.makeMatrix();
        bill15.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill15);

        //手前 右奥広場 ビル3　パネル 歯科
        TexObject3D bill16 = new TexObject3D();
        bill16.setModel(billShape);
        //Texture.setRepeartTexture(bill14wall, true);
        bill16.setTexture(pictureID18);
        bill16.setTranslate(-2f, -18f, 43f);
        bill16.setScale(3f,1f,2.7f);
        bill16.setRotate(-90, 0,0,1);
        bill16.fullCalcBoundingShape();
        bill16.makeMatrix();
        bill16.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill16);

        //広場　右奥ビル オフィス９０度
        TexObject3D bill23 = new TexObject3D();
        bill23.setModel(billShape);
        bill23.setTexture(pictureID16);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -28f, -24f);
        Matrix.scaleM(matrix, 0, 3f,1f,3f);
        bill23.calcBoundingShapeByMatrix(matrix);
        bill23.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill23);

        //右　広場フェンス
        TexObject3D rightFence1 = new TexObject3D();
        int fenceObjectShape = new TilePanelShapeGenerator().createShape3D(0, 1.5f, 6, 0, 12,1);
        rightFence1.setModel(fenceObjectShape);
        Texture.setRepeartTexture(fence2PictureID, true);
        rightFence1.setTexture(fence2PictureID);
        rightFence1.setTranslate(3.2f, -18.1f, 14f);
        rightFence1.setScale(1f,1f,5f);
        rightFence1.setRotate(-90, 0,0,1);
        rightFence1.fullCalcBoundingShape();
        //rightFence.makeMatrix();
        rightFence1.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence1);

        // 裏
        TexObject3D bill22 = new TexObject3D();
        bill22.setModel(billShape);
        bill22.setTexture(pictureID22);
        matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, -90, 0,0,1);
        Matrix.translateM(matrix, 0, -2f, -31f, 0f);
        Matrix.scaleM(matrix, 0, 3f,2f,3.4f);
        bill22.calcBoundingShapeByMatrix(matrix);
        bill22.setShader(GLES.SP_SimpleTexture);
        objectList.add(bill22);

        //右 中景1パネル　花火
        TexObject3D backPanel1 = new TexObject3D();
        int panelShape = new TilePanelShapeGenerator().createShape3D(0, 2);
        backPanel1.setModel(panelShape);
        backPanel1.setTexture(pictureID15);
        backPanel1.setTranslate(-6f, -75.f, 10f);
        backPanel1.setScale(13f,-1f,18f);
        backPanel1.setRotate(-90, 0,0,1);
        backPanel1.makeMatrix();
        backPanel1.setShader(GLES.SP_SimpleTexture);
        objectList.add(backPanel1);


        //走るモグ
        TexObject3D mo3d = new TexObject3D();
        mo3d.setTexture(moguID1);
        mo3d.setShader(GLES.SP_SimpleTexture);
        mo3d.setModel(new LowerFacePanelShapeGenerator().createShape3D(0, 2));

        CircleRail rail = new CircleRail();
        rail.setOffset(-20, 5, 0);
        rail.setObject3D(mo3d);
        rail.setInterruptTime(100);
        rail.setRailDivide(1080);
        new Thread(rail).start();
        objectList.add(mo3d);

        //キー入力パネル
        KeyPanelSet kps = new KeyPanelSet();
        final float y = -3.4f;
        float[] keyPositionArray = new float[]{
                3, 2, -28,  //0A
                5, -3.5f, 39,
                -16, y, 34,
                3, 1, 16,
                -5, -3.2f, -20,  //E
                -18, -2, 20,  //5
                -17.5f, 7f, 32,
                19,-4.65f,19,  //H
                -18,7,0,
                30,9,40,  //J
                -5, y, 10,  //10K
                -16, 0, -5,  //L
                -5, y, -3,  //12M
                6.5f, y, -18,
                12, -3.3f, 3f,  //O
                1, 10, 8,  //P
                20, 0.5f, 36,  //Q
                18,3,24,  //17R
                30,6,26,  //S
                -16,y,28,  //T
        };

        kps.setPanelShapeID(new TexCubeShapeGenerator().createShape3D(0, 1.2f));
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
        questionObject.setScale(0.01f,0.01f,0.02f);
        questionObject.setColor(1f,0.7f,0.7f,1);
        questionObject.setTranslate(0,16,1.8f);
        questionObject.setRotate(90,1,0,0);
        questionObject.fullCalcBoundingShape();
        //questionObject.makeMatrix();
        questionObject.setShader(GLES.SP_ObjectWithLight2);

    }

    @Override
    public void startStage() {
        Premadonna prema = manager.prema;
        prema.translate(prema.getX(),prema.getY(),prema.getZ()-20, 0,0);
        manager.renderer.setGLClearColor(0,0,0);

    }
}
