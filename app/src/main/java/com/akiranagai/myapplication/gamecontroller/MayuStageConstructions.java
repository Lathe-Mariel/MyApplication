package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.CircleRail;
import com.akiranagai.myapplication.object3d.LowerFacePanelShapeGenerator;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.TexSphereShapeGenerator;
import com.akiranagai.myapplication.object3d.StlModel;
import com.akiranagai.myapplication.object3d.TexObject3D;

public class MayuStageConstructions extends StageConstructions {
    public MayuStageConstructions(GameManager manager) {
        Context context = manager.activity.getBaseContext();

        Object3D sphere = new Object3D();
        sphere.setModel(new StlModel(context, "saraa.stl").createShape3D(0 ));
        sphere.setColor(0.92f, 0.92f, 0.92f, 1f);
        sphere.setTranslate(0f, -2.5f, 0f);
        sphere.makeMatrix();
        sphere.setShader(GLES.SP_ObjectWithLight);
        objectList.add(sphere);

        TexObject3D texSphere = new TexObject3D();
        texSphere.setModel(new TexSphereShapeGenerator().createShape3D(0, 360f, 70f, 90f, 24, 10));
        texSphere.setScale(5.5f, 5.f, 5.5f);
        int nikuID = new Texture().addTexture(context, R.drawable.nikuc);
        texSphere.setTexture(nikuID);
        texSphere.setTranslate(0f, -7f, 0f);
        texSphere.makeMatrix();
        texSphere.setShader(GLES.SP_SimpleTexture);
        objectList.add(texSphere);

        Object3D obj = new Object3D();
        obj.setAABB(-2f, -3f, -2f, 2f, 3f, 2f);
        //manager.field.putObstacle(obj);

        TexObject3D mo3d = new TexObject3D();
        mo3d.setTexture(new Texture().addTexture(context, R.drawable.pengina));
        mo3d.setShader(GLES.SP_SimpleTexture);
        mo3d.setModel(new LowerFacePanelShapeGenerator().createShape3D(0, 2));

        CircleRail rail = new CircleRail();
        rail.setOffset(-15, 8, 0);
        rail.setObject3D(mo3d);
        rail.setInterruptTime(100);
        rail.setRailDivide(1080);
        new Thread(rail).start();
        objectList.add(mo3d);
    }

    @Override
    public void setQuestion(Object3D questionObject){}
    @Override
    public void startStage(){}
}
