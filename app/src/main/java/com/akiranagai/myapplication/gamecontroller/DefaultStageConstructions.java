package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.StringTextureGenerator;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.CircleRail;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.LowerFacePanelShapeGenerator;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.TexSphereShapeGenerator;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.Toast;

import java.util.ArrayList;

public class DefaultStageConstructions extends StageConstructions {
    public DefaultStageConstructions(GameManager manager) {
        //Object3D wireSphere;
        float SunRadius=1.5f;
        float EarthRadius=1f;
        float MoonRadius=0.4f;
        int earthPictureID;
        int MoonPictureID;
        Context context = manager.activity.getBaseContext();
        float   angle=-1f; //回転角度

        earthPictureID = new Texture().addTexture(context, R.drawable.neko256a); //テクスチャを作成
        MoonPictureID = new Texture().addTexture(context,R.drawable.moonpicture); //テクスチャを作成

        TexObject3D texSphere = new TexObject3D();
        texSphere.setModel(new TexSphereShapeGenerator().createShape3D(0, 360f, -90f, 90f, 72, 36));
        texSphere.setTexture(earthPictureID);
        texSphere.setRotate((int) (2 * angle), 0f, 1f, 0f);
        texSphere.setTranslate(0f, 1.5f, 0f);
        texSphere.setScale(EarthRadius, EarthRadius, EarthRadius);
        texSphere.makeMatrix();
        texSphere.setShader(GLES.SP_TextureWithLight);
        objectList.add(texSphere);

        texSphere = new TexObject3D();
        texSphere.setModel(new TexSphereShapeGenerator().createShape3D(0, 360f, -90f, 90f, 72, 36));
        texSphere.setTexture(MoonPictureID);
        texSphere.setRotate((int) (0.5f * angle), 0f, 1f, 0f);
        texSphere.setTranslate(5f, 1.5f, 0f);
        texSphere.setScale(MoonRadius, MoonRadius, MoonRadius);
        texSphere.makeMatrix();
        texSphere.setShader(GLES.SP_TextureWithLight);
        objectList.add(texSphere);

        //wireSphere = new Object3D();
        //wireSphere.setModel(new TexSphereShapeGenerator().createShape3D(5));
        //wireSphere.setTranslate(5, 2, 5)
        //        .setColor(1, 0, 0, 1)
        //        .setShader(GLES.SP_ObjectWithLight);
        //wireSphere.makeMatrix();

        TexObject3D cube6textureTest = new TexObject3D();
        TexCubeShapeGenerator tcsg = new TexCubeShapeGenerator();
        tcsg.setTextureMode(6);  //6面　別Texture に設定
        cube6textureTest.setModel(tcsg.createShape3D(0));
        cube6textureTest.setTranslate(3f, 0f, 18f)
                .setShader(GLES.SP_SimpleTexture);
        cube6textureTest.makeMatrix();
        int[] testID = new StringTextureGenerator().makeString6Texture("MARIEL", 36, Color.BLUE, Color.WHITE,0);
        cube6textureTest.setTexture(testID[0]);
        objectList.add(cube6textureTest);

        TexObject3D mo3d = new TexObject3D();
        mo3d.setTexture(new Texture().addTexture(context, R.drawable.pengina));
        mo3d.setShader(GLES.SP_SimpleTexture);
        mo3d.setModel(new LowerFacePanelShapeGenerator().createShape3D(0, 2));

        //飛ぶペンギン
        CircleRail rail = new CircleRail();
        rail.setOffset(-15, 8, 0);
        rail.setObject3D(mo3d);
        rail.setInterruptTime(100);
        rail.setRailDivide(1080);
        new Thread(rail).start();
        objectList.add(mo3d);

        final KeyPanelSet kps = new KeyPanelSet();
        kps.manager = manager;
        kps.setPanelShapeID(new TexCubeShapeGenerator().createShape3D(0, 0.8f));
        kps.createAlphabets(0, 19);
        TexObject3D cubeAlpha = kps.make6Cube("UVWXYZ", new int[]{20,21,22,23,24,25});
        cubeAlpha.setTranslate(-8, 0, 8);
        ArrayList<TexObject3D> list = kps.getPanels();
        objectList.addAll(list);
        manager.sInput.setBuckBufferObjectList(list);  //バックバッファに設定する表示オブジェクトを登録(Alphabet Boxes)

        //方角　指示器
        TexObject3D directionPanel = new TexObject3D();
        directionPanel.setModel(new Toast().createShape3D(0, 7.85f, 0.1f));
        directionPanel.setShader(GLES.SP_SimpleTexture);
        directionPanel.setTexture(new StringTextureGenerator().makeStringTextureWithSpace("ESWNE", 30f, Color.BLACK));
        manager.prema.setDirectionPanel(directionPanel);
    }

    @Override
    public void setQuestion(Object3D questionObject){}

    @Override
    public void startStage(){}
}
