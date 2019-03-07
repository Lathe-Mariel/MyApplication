package com.akiranagai.myapplication.collision;

public class Sphere {
    Float3 point;  //中心座標
    float r;  //半径
    public Sphere(){}
    public Sphere(Float3 point, float r){
        this.point = point;
        this.r = r;
    }
}
