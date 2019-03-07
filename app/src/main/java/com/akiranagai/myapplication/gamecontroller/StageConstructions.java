package com.akiranagai.myapplication.gamecontroller;

import com.akiranagai.myapplication.object3d.Object3D;

import java.util.ArrayList;

public abstract class StageConstructions {
    ArrayList<Object3D> objectList = new ArrayList<>();
    ArrayList<Object3D> wireObjectList = new ArrayList<>();

StageConstructions(){}
    StageConstructions(GameManager manager){
    }

    ArrayList<Object3D>  getObjectList(){
        return objectList;
    }

    abstract public void setQuestion(Object3D questionObject);

    abstract public void startStage();
}
