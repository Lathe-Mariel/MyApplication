package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Matrix;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.StringTextureGenerator;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.PipeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;
import com.akiranagai.myapplication.object3d.Toast;

import java.util.ArrayList;

public class BritishStageConstructions extends StageConstructions {

    @Override
    public void setQuestion(Object3D questionObject){
        questionObject.setScale(0.01f,0.01f,0.02f);
        questionObject.setColor(0.55f,1,0.6f,1);
        questionObject.setTranslate(0,16,1.8f);
        questionObject.setRotate(90,1,0,0);
        questionObject.fullCalcBoundingShape();
        //questionObject.makeMatrix();
        questionObject.setShader(GLES.SP_ObjectWithLight2);
    }

    public BritishStageConstructions(GameManager manager) {

        Context context = manager.activity.getBaseContext();

        int house1PictureID = new Texture().addTexture(context, R.drawable.brihousea); //テクスチャを作成
        int house2PictureID = new Texture().addTexture(context, R.drawable.brihouseb); //テクスチャを作成
        int house3PictureID = new Texture().addTexture(context, R.drawable.brihousec); //テクスチャを作成
        int house4PictureID = new Texture().addTexture(context, R.drawable.brihoused); //テクスチャを作成
        int house5PictureID = new Texture().addTexture(context, R.drawable.brihousee); //テクスチャを作成
        int house8PictureID = new Texture().addTexture(context, R.drawable.brihousef); //テクスチャを作成
        int house9PictureID = new Texture().addTexture(context, R.drawable.brihouseg); //テクスチャを作成
        int house6PictureID = new Texture().addTexture(context, R.drawable.siroa); //テクスチャを作成
        int house7PictureID = new Texture().addTexture(context, R.drawable.sirob); //テクスチャを作成
        int roadPictureID = new Texture().addTexture(context, R.drawable.tilebrick);  //地面 道 タイル
        int roadPictureID2 = new Texture().addTexture(context, R.drawable.bricktile);  //地面タイル2
        int roadPictureID3 = new Texture().addTexture(context, R.drawable.marble);  //地面タイル3
        int roadPictureID4 = new Texture().addTexture(context, R.drawable.wooda);  //地面タイル4
        int roadPictureID5 = new Texture().addTexture(context, R.drawable.tileb);  //地面タイル5
        int roadPictureID6 = new Texture().addTexture(context, R.drawable.rockb);  //地面 道 タイル
        int circulerPictureID = new Texture().addTexture(context, R.drawable.circulertile); //テクスチャを作成
        int tower = new Texture().addTexture(context, R.drawable.trafalger_tower);  //タワーテクスチャ作成
        int bush = new Texture().addTexture(context, R.drawable.ikegakia);  //テクスチャ作成
        int kadan = new Texture().addTexture(context, R.drawable.kadana);  //花壇テクスチャ作成
        int rose = new Texture().addTexture(context, R.drawable.rosea);  //花壇2テクスチャ作成
        int back1 = new Texture().addTexture(context, R.drawable.castlea);  //前景1テクスチャ作成
        int back2 = new Texture().addTexture(context, R.drawable.backa);  //中景1テクスチャ作成
        int fortresswall = new Texture().addTexture(context, R.drawable.fortresswalla); //城壁テクスチャ
        int fence1PictureID = new Texture().addTexture(context, R.drawable.fencea);  //   フェンステクスチャ
        int fence2PictureID = new Texture().addTexture(context, R.drawable.fenceb);  //   フェンステクスチャ2
        int fountainPictureID = new Texture().addTexture(context, R.drawable.rocka);  //噴水周り用岩テクスチャ
        int fountainPictureSurface = new Texture().addTexture(context, R.drawable.fountaina);  //噴水 水面
        int fountainBodyPicture = new Texture().addTexture(context, R.drawable.fountainb);  //噴水 本体
        int lawnPictureID = new Texture().addTexture(context, R.drawable.lawn);  //芝生テキスチャ
        int signboardPictureID = new Texture().addTexture(context, R.drawable.signboarda);  //看板テキスチャ

        //右　家
        TexObject3D leftHouse = new TexObject3D();
        int modelID = (new TexCubeShapeGenerator().createShape3D(0, 12f));
        leftHouse.setModel(modelID);
        leftHouse.setTexture(house1PictureID);
        leftHouse.setTranslate(-16.5f, 3f, -30f);
        leftHouse.setScale(1,1,1.5f);
        //leftHouse.setCalcTranslate(new float[]{-20f, 3f,-30f});
        leftHouse.reCalcBoundingShapeWithScale();
        leftHouse.makeMatrix();
        leftHouse.setShader(GLES.SP_TextureWithLight);
        objectList.add(leftHouse);

        //左　家
        TexObject3D rightHouse = new TexObject3D();
        rightHouse.setModel(modelID);
        rightHouse.setTexture(house2PictureID);
        rightHouse.setTranslate(15f, 3f, -30f);
        rightHouse.setScale(1,1,1.5f);
        //rightHouse.setCalcTranslate(new float[]{20f, 3f,-30f});
        rightHouse.reCalcBoundingShapeWithScale();
        rightHouse.makeMatrix();
        rightHouse.setShader(GLES.SP_TextureWithLight);
        objectList.add(rightHouse);

        //左　家2
        TexObject3D rightHouse2 = new TexObject3D();
        rightHouse2.setModel(modelID);
        rightHouse2.setTexture(house4PictureID);
        rightHouse2.setTranslate(15f, 3f, -8f);
        rightHouse2.setScale(1,1,1.5f);
        //rightHouse2.setCalcTranslate(new float[]{20f, 3f,-8f});
        rightHouse2.reCalcBoundingShapeWithScale();
        rightHouse2.makeMatrix();
        rightHouse2.setShader(GLES.SP_TextureWithLight);
        objectList.add(rightHouse2);

        //右　家2
        TexObject3D leftHouse2 = new TexObject3D();
        leftHouse2.setModel(modelID);
        leftHouse2.setTexture(house5PictureID);
        leftHouse2.setTranslate(-16.5f, 3f, -15f);
        leftHouse2.setScale(1,1,0.5f);
        //leftHouse2.setCalcTranslate(new float[]{-20f, 3f,-15f});
        leftHouse2.reCalcBoundingShapeWithScale();
        leftHouse2.makeMatrix();
        leftHouse2.setShader(GLES.SP_TextureWithLight);
        objectList.add(leftHouse2);

        //センターハウス
        TexObject3D centerhouse = new TexObject3D();
        centerhouse.setModel(modelID);
        centerhouse.setTexture(house3PictureID);
        centerhouse.setTranslate(0f, 3f, 60.f);
        centerhouse.setScale(2.8f,1,1f);
        //centerhouse.setCalcTranslate(new float[]{0f, 3f,35.5f});
        //centerhouse.reCalcBoundingShapeWithScale();
        centerhouse.makeMatrix();
        centerhouse.setShader(GLES.SP_TextureWithLight);
        objectList.add(centerhouse);

        //右奥ハウス
        TexObject3D rightSideHouse = new TexObject3D();
        rightSideHouse.setModel(modelID);
        rightSideHouse.setTexture(house6PictureID);
        rightSideHouse.setTranslate(-70f, 3f, 18f);
        rightSideHouse.setScale(0.5f,1,4f);
        //rightSideHouse.reCalcBoundingShapeWithScale();
        rightSideHouse.makeMatrix();
        rightSideHouse.setShader(GLES.SP_TextureWithLight);
        objectList.add(rightSideHouse);

        //右奥奥ハウス
        TexObject3D rightSideHouse2 = new TexObject3D();
        rightSideHouse2.setModel(modelID);
        rightSideHouse2.setTexture(house7PictureID);
        rightSideHouse2.setTranslate(-48f, 3f, 60f);
        rightSideHouse2.setScale(2.8f,1f,0.6f);
        //rightSideHouse.reCalcBoundingShapeWithScale();
        rightSideHouse2.makeMatrix();
        rightSideHouse2.setShader(GLES.SP_TextureWithLight);
        objectList.add(rightSideHouse2);

        //右手前奥ハウス
        TexObject3D house2 = new TexObject3D();
        house2.setModel(modelID);
        house2.setTexture(house9PictureID);
        house2.setTranslate(-85f, 3f, -36f);
        house2.setScale(1f,1,3f);
        //house2.reCalcBoundingShapeWithScale();
        house2.makeMatrix();
        house2.setShader(GLES.SP_TextureWithLight);
        objectList.add(house2);

        //左奥ハウス
        TexObject3D leftSideHouse = new TexObject3D();
        leftSideHouse.setModel(modelID);
        leftSideHouse.setTexture(house6PictureID);
        leftSideHouse.setTranslate(34f, 3f, 18f);
        leftSideHouse.setScale(0.5f,1,4f);
        leftSideHouse.reCalcBoundingShapeWithScale();
        leftSideHouse.makeMatrix();
        leftSideHouse.setShader(GLES.SP_TextureWithLight);
        objectList.add(leftSideHouse);

        //右手前前ハウス
        TexObject3D house3 = new TexObject3D();
        house3.setModel(modelID);
        house3.setTexture(house8PictureID);
        house3.setTranslate(-60f, 2.9f, -64f);
        house3.setScale(2.5f,1,1f);
        //house2.reCalcBoundingShapeWithScale();
        house3.makeMatrix();
        house3.setShader(GLES.SP_TextureWithLight);
        objectList.add(house3);

        //センター生垣
        TexObject3D bush1 = new TexObject3D();
        int model2ID = (new TexCubeShapeGenerator().createShape3D(0, 25.0f,1.2f,1f,0,0));
        bush1.setModel(model2ID);
        Texture.setRepeartTexture(model2ID, true);
        bush1.setTexture(bush);
        bush1.setTranslate(0f, -3.4f, 38f);
        bush1.setScale(2.5f,1f,0.5f);
        bush1.reCalcBoundingShapeWithScale();
        bush1.makeMatrix();
        bush1.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush1);

        //前生垣
        TexObject3D bush2 = new TexObject3D();
        bush2.setModel(model2ID);
        //Texture.setRepeartTexture(model2ID, true);
        bush2.setTexture(bush);
        bush2.setTranslate(-3.9f, -3.4f, -47f);
        bush2.setScale(3.54f,1f,0.4f);
        bush2.reCalcBoundingShapeWithScale();
        bush2.makeMatrix();
        bush2.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush2);

        //右前フェンス
        TexObject3D rightFence = new TexObject3D();
        int fenceObjectShape = new TilePanelShapeGenerator().createShape3D(0, 1.5f, 6, 0, 12,1);
        rightFence.setModel(fenceObjectShape);
        Texture.setRepeartTexture(fence1PictureID, true);
        rightFence.setTexture(fence1PictureID);
        rightFence.setTranslate(3.2f, -48.f, -33.5f);
        rightFence.setScale(1f,1f,4.5f);
        rightFence.setRotate(-90, 0,0,1);
        rightFence.fullCalcBoundingShape();
        //rightFence.makeMatrix();
        rightFence.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence);

        //右奥フェンス1
        TexObject3D rightFence1 = new TexObject3D();
        rightFence1.setModel(fenceObjectShape);
        Texture.setRepeartTexture(fence2PictureID, true);
        rightFence1.setTexture(fence2PictureID);
        rightFence1.setTranslate(3.2f, -48.f, 24f);
        rightFence1.setScale(1f,1f,4.5f);
        rightFence1.setRotate(-90, 0,0,1);
        rightFence1.fullCalcBoundingShape();
        //rightFence.makeMatrix();
        rightFence1.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence1);

        //右奥フェンス2
        TexObject3D rightFence2 = new TexObject3D();
        rightFence2.setModel(fenceObjectShape);
        rightFence2.setTexture(fence2PictureID);
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix,0);
        Matrix.translateM(matrix, 0, -43f, -3.2f, 37.7f);
        Matrix.rotateM(matrix, 0, -90, 1,0,0);
        Matrix.rotateM(matrix, 0, 90,0,1,0);
        Matrix.scaleM(matrix, 0, 1f,1f,4.5f);
        rightFence2.calcBoundingShapeByMatrix(matrix);
        rightFence2.setShader(GLES.SP_SimpleTexture);
        objectList.add(rightFence2);

        //右花壇3
        TexObject3D bush3 = new TexObject3D();
        TexCubeShapeGenerator tcsg = new TexCubeShapeGenerator();
        tcsg.setTextureMode(1);
        int model3ID = (tcsg.createShape3D(0, 1f,1f,20f,0,0));
        bush3.setModel(model3ID);
        Texture.setRepeartTexture(kadan, true);
        bush3.setTexture(kadan);
        bush3.setTranslate(-48f, -3.4f, -5f);
        bush3.setScale(1f,1f,1.5f);
        bush3.reCalcBoundingShapeWithScale();
        bush3.makeMatrix();
        bush3.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush3);

        //花壇5 ハコ(左手前)
        TexObject3D bush5 = new TexObject3D();
        bush5.setModel(model3ID);
        Texture.setRepeartTexture(kadan, true);
        bush5.setTexture(kadan);
        bush5.setTranslate(45f, -3.5f, -25f);
        bush5.setScale(1f,1f,1f);
        bush5.reCalcBoundingShapeWithScale();
        bush5.makeMatrix();
        bush5.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush5);

        //花壇4左 ハコ
        TexObject3D bush4a = new TexObject3D();
        bush4a.setModel(model3ID);
        Texture.setRepeartTexture(kadan, true);
        bush4a.setTexture(kadan);
        bush4a.setTranslate(45f, -3.5f, 25f);
        bush4a.setScale(1f,1f,1f);
        bush4a.reCalcBoundingShapeWithScale();
        bush4a.makeMatrix();
        bush4a.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush4a);

        //花壇4左 フタ
        TexObject3D bush4b = new TexObject3D();
        int model4 = new TilePanelShapeGenerator().createShape3D(0, 1,20,0, 20,1);
        bush4b.setModel(model4);
        Texture.setRepeartTexture(rose, true);
        bush4b.setTexture(rose);
        bush4b.setTranslate(45f, -2.99f, 25f);
        bush4b.setScale(1f,1f,1f);
        bush4b.reCalcBoundingShapeWithScale();
        bush4b.makeMatrix();
        bush4b.setShader(GLES.SP_TextureWithLight);
        objectList.add(bush4b);

        //センターロード　下部パネル(roadModel 縦長)
        TexObject3D road = new TexObject3D();
        int roadModelID = new TilePanelShapeGenerator().createShape3D(0,19f,43f,0f,38,43);
        road.setModel(roadModelID);
        Texture.setRepeartTexture(roadPictureID, true);
        road.setTexture(roadPictureID);
        road.setTranslate(0f, -3.9f, -19.f);
        road.setScale(1f,1f,1f);
        road.makeMatrix();
        road.setShader(GLES.SP_SimpleTexture);
        objectList.add(road);

        //センター奥　下部パネル(roadModel2 横長)
        TexObject3D road4 = new TexObject3D();
        int roadModel2ID = new TilePanelShapeGenerator().createShape3D(0,15f,11f,0f,12,24);
        road4.setModel(roadModel2ID);
        Texture.setRepeartTexture(roadPictureID, true);
        road4.setTexture(roadPictureID);
        road4.setTranslate(1.9f, -3.9f, 32.5f);
        road4.setScale(1f,1f,1f);
        road4.makeMatrix();
        road4.setShader(GLES.SP_SimpleTexture);
        objectList.add(road4);

        //センター最奥　下部パネル 6角
        TexObject3D hexPanel = new TexObject3D();
        hexPanel.setModel(roadModel2ID);
        //Texture.setRepeartTexture(roadPictureID, true);
        hexPanel.setTexture(roadPictureID5);
        hexPanel.setTranslate(2f, -3.95f, 57f);
        hexPanel.setScale(3.4f,1f,3.4f);
        hexPanel.makeMatrix();
        hexPanel.setShader(GLES.SP_SimpleTexture);
        objectList.add(hexPanel);

        //右側　下部パネル(roadModel)
        TexObject3D roadR = new TexObject3D();
        roadR.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        roadR.setTexture(roadPictureID);
        roadR.setTranslate(-15f, -3.9f, 11f);
        roadR.setScale(1f,1f,1.1f);
        roadR.makeMatrix();
        roadR.setShader(GLES.SP_SimpleTexture);
        objectList.add(roadR);

        objectList.add(hexPanel);
        //右側奥　下部パネル(roadModel)
        TexObject3D roadR2 = new TexObject3D();
        roadR2.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        roadR2.setTexture(roadPictureID3);
        roadR2.setTranslate(-65f, -4f, 7f);
        roadR2.setScale(4.5f,1f,3f);
        roadR2.makeMatrix();
        roadR2.setShader(GLES.SP_SimpleTexture);
        objectList.add(roadR2);

        //左側 下部パネル(roadModel)
        TexObject3D roadL = new TexObject3D();
        roadL.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        roadL.setTexture(roadPictureID2);
        roadL.setTranslate(19f, -3.9f, 16.5f);
        roadL.setScale(1f,1f,1f);
        roadL.makeMatrix();
        roadL.setShader(GLES.SP_SimpleTexture);
        objectList.add(roadL);

        //左左側 下部パネル
        TexObject3D roadL2 = new TexObject3D();
        roadL2.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        roadL2.setTexture(roadPictureID4);
        roadL2.setTranslate(39f, -3.95f, 16f);
        roadL2.setScale(1.1f,1.f,1.4f);
        roadL2.makeMatrix();
        roadL2.setShader(GLES.SP_SimpleTexture);
        objectList.add(roadL2);

        //円形広場　下部パネル
        TexObject3D circulerTile = new TexObject3D();
        circulerTile.setModel(new TilePanelShapeGenerator().createShape3D(0, 1));
        //Texture.setRepeartTexture(circulerPictureID, false);
        circulerTile.setTexture(circulerPictureID);
        circulerTile.setTranslate(0f, -3.89f, 15f);
        circulerTile.setScale(24f,1f,24f);
        circulerTile.makeMatrix();
        circulerTile.setShader(GLES.SP_TextureWithLight);
        objectList.add(circulerTile);

        //手前　下部パネル 芝
        TexObject3D lawnPanel0 = new TexObject3D();
        lawnPanel0.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        lawnPanel0.setTexture(lawnPictureID);
        lawnPanel0.setTranslate(-17.3f, -4.05f, -61.5f);
        lawnPanel0.setScale(2.8f,1f,1.f);
        lawnPanel0.makeMatrix();
        lawnPanel0.setShader(GLES.SP_SimpleTexture);
        objectList.add(lawnPanel0);
        //左前芝　下部パネル　コピー1
        TexObject3D lawnPanel1 = new TexObject3D();
        lawnPanel1.setModel(roadModelID);
        //Texture.setRepeartTexture(roadPictureID, true);
        lawnPanel1.setTexture(lawnPictureID);
        lawnPanel1.setTranslate(31.5f, -4f, -61.5f);
        lawnPanel1.setScale(2.4f,1f,1.f);
        lawnPanel1.makeMatrix();
        lawnPanel1.setShader(GLES.SP_SimpleTexture);
        objectList.add(lawnPanel1);

        //左手前　岩道　下部パネル
        TexObject3D road6 = new TexObject3D();
        road6.setModel(roadModelID);
        Texture.setRepeartTexture(roadPictureID6, true);
        road6.setTexture(roadPictureID6);
        road6.setTranslate(34f, -4.0f, -22.9f);
        road6.setScale(1.4f,1f,0.8f);
        road6.makeMatrix();
        road6.setShader(GLES.SP_SimpleTexture);
        objectList.add(road6);

        //タワー
        TexObject3D pipe1 = new TexObject3D();
        pipe1.setModel(new PipeShapeGenerator().createShape3D(0, 10f, 1.5f, (float)(2*Math.PI), 12, 1));
        pipe1.setTexture(tower);
        pipe1.setTranslate(74, -4f, -2f);
        pipe1.setRotate(215, 0,1,0);
        pipe1.setScale(1f,1.5f,1f);
        //pipe1.reCalcBoundingShapeWithScale();
        pipe1.makeMatrix();
        pipe1.setShader(GLES.SP_TextureWithLight);
        objectList.add(pipe1);

        //城壁コラム
        TexObject3D roundColumn = new TexObject3D();
        roundColumn.setModel(new PipeShapeGenerator().createShape3D(0,6,1.f, (float)Math.PI, 18, 1));
        roundColumn.setTexture(fortresswall);
        matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix, 0, 49.5f,2.f,0);
        Matrix.rotateM(matrix, 0, 180, 0,0,1);
        Matrix.rotateM(matrix, 0, 90, 0,1,0);
        roundColumn.setScale(1,1.f,1);
        roundColumn.calcBoundingShapeByMatrix(matrix);
        roundColumn.setShader(GLES.SP_SimpleTexture);
        objectList.add(roundColumn);

        //左城壁　パネル
        TexObject3D fortress = new TexObject3D();
        fortress.setModel(new TilePanelShapeGenerator().createShape3D(0, 6,22,0,44, 1));
        Texture.setRepeartTexture(fortresswall, true);
        fortress.setTexture(fortresswall);
        fortress.setTranslate(-1f, -49.5f, 0f);
        fortress.setScale(1f,1f,4.5f);
        fortress.setRotate(90, 0,0,1);
        fortress.fullCalcBoundingShape();
        fortress.makeMatrix();
        fortress.setShader(GLES.SP_SimpleTexture);
        objectList.add(fortress);

        //左城壁奥 パネル
        TexObject3D fortress1 = new TexObject3D();
        int fortPanel = new TilePanelShapeGenerator().createShape3D(0, 6,6,0,6, 1);
        fortress1.setModel(fortPanel);
        fortress1.setTexture(fortresswall);
        matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix,0, 41.f, -1, 45.8f);
        Matrix.rotateM(matrix, 0, -90,0,1,0);
        Matrix.rotateM(matrix, 0, 90, 0,0,1);
        Matrix.scaleM(matrix, 0, 1,1,2.8f);
        //fortress1.setCalcTranslate(new float[]{42,-1,48});
        //fortress1.fullCalcBoundingShape();
        fortress1.calcBoundingShapeByMatrix(matrix);
        //fortress1.makeMatrix();
        fortress1.setShader(GLES.SP_SimpleTexture);
        objectList.add(fortress1);

        //円形プール
        TexObject3D roundPool = new TexObject3D();
        int roundPole = new PipeShapeGenerator().createShape3D(0,1,4.4f, (float)Math.PI*2, 18, 1);
        roundPool.setModel(roundPole);
        roundPool.setTexture(fountainPictureID);
        roundPool.setTranslate(44.7f,-4f,-46.5f);
        roundPool.setScale(1,1.f,1);
        roundPool.reCalcBoundingShapeWithScale();
        roundPool.makeMatrix();
        roundPool.setShader(GLES.SP_SimpleTexture);
        objectList.add(roundPool);

        //噴水水面
        TexObject3D waterSurface = new TexObject3D();
        waterSurface.setModel(new TilePanelShapeGenerator().createShape3D(0, 1));
        //Texture.setRepeartTexture(circulerPictureID, false);
        waterSurface.setTexture(fountainPictureSurface);
        waterSurface.setTranslate(44.7f, -3f, -46.5f);
        waterSurface.setScale(8.8f,8.8f,8.8f);
        waterSurface.makeMatrix();
        waterSurface.setShader(GLES.SP_TextureWithLight);
        objectList.add(waterSurface);

        //噴水ボディー 水柱
        TexObject3D fountainBody = new TexObject3D();
        fountainBody.setModel(roundPole);
        fountainBody.setTexture(fountainBodyPicture);
        fountainBody.setTranslate(44.7f,-3f,-46.5f);
        fountainBody.setScale(0.3f,4f,0.3f);
        fountainBody.makeMatrix();
        fountainBody.setShader(GLES.SP_SimpleTexture);
        objectList.add(fountainBody);

        //右 中景1パネル城
        TexObject3D backPanel1 = new TexObject3D();
        int panelShape = new TilePanelShapeGenerator().createShape3D(0, 2);
        backPanel1.setModel(panelShape);
        backPanel1.setTexture(back1);
        backPanel1.setTranslate(-3f, -90.f, -1f);
        backPanel1.setScale(13f,1f,14f);
        backPanel1.setRotate(-90, 0,0,1);
        backPanel1.makeMatrix();
        backPanel1.setShader(GLES.SP_SimpleTexture);
        objectList.add(backPanel1);

        //パネル 猫スキー
        TexObject3D signBoard1 = new TexObject3D();
        signBoard1.setModel(panelShape);
        signBoard1.setTexture(signboardPictureID);
        signBoard1.setTranslate(22.5f, -45f, 0.3f);
        signBoard1.setScale(2.f,1f,4f);
        signBoard1.setRotate(-90, 1,0,0);
        signBoard1.makeMatrix();
        signBoard1.setShader(GLES.SP_SimpleTexture);
        objectList.add(signBoard1);

        //右奥 中景パネル
        TexObject3D backPanel2 = new TexObject3D();
        backPanel2.setModel(new TilePanelShapeGenerator().createShape3D(0, 2));
        backPanel2.setTexture(back2);
        backPanel2.setTranslate(-28.8f, -100.f, 4f);
        backPanel2.setScale(38,1f,10f);
        backPanel2.setRotate(-90, 1,0,0);
        backPanel2.makeMatrix();
        backPanel2.setShader(GLES.SP_SimpleTexture);
        objectList.add(backPanel2);

        //方角　指示器
        TexObject3D directionPanel = new TexObject3D();
        directionPanel.setModel(new Toast().createShape3D(0, 7.85f, 0.1f));
        directionPanel.setShader(GLES.SP_SimpleTextureSimpleZ);
        directionPanel.setTexture(new StringTextureGenerator().makeStringTextureWithSpace("ESWNE", 30f, Color.BLACK));
        manager.prema.setDirectionPanel(directionPanel);

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
        kps.createAlphabets(0, 19, keyPositionArray);
        TexObject3D cubeAlpha = kps.make6Cube("UVWXYZ", new int[]{20,21,22,23,24,25});
        cubeAlpha.setTranslate(18, 0, 8);
        ArrayList<TexObject3D> list;
        list = kps.getPanels();
        objectList.addAll(list);
        manager.sInput.setBuckBufferObjectList(list);  //バックバッファに設定する表示オブジェクトを登録(Alphabet Boxes)

    }

    @Override
    public void startStage(){}
}
