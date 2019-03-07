package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.Shape3D;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TexSphereShapeGenerator;

public class SpaceStageConstructions extends StageConstructions {

    public SpaceStageConstructions(GameManager manager){
        Context context = manager.activity;
        manager.renderer.setBackTexture(new Texture().addTexture(context, R.drawable.spacebackd));

        int jupiterPictureID = new Texture().addTexture(context, R.drawable.jupiter); //テクスチャを作成
        //int house1PictureID = new Texture().addTexture(context, R.drawable.brihousea); //テクスチャを作成


        TexObject3D sphere1 = new TexObject3D();
        int sphereShapeID = new TexSphereShapeGenerator().createShape3D(0, 360, -90, 90, 12,12);
        sphere1.setModel(sphereShapeID);
        sphere1.setTranslate(5,0,0);
        sphere1.setTexture(jupiterPictureID);
        sphere1.makeMatrix();
        sphere1.setShader(GLES.SP_TextureWithLight);
        objectList.add(sphere1);
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
