package com.akiranagai.myapplication.collision;

public class Capsule {
    Segment segment;
    float r;  //半径
    public Capsule(){
        r=0.5f;
    }
    public Capsule(Segment seg, float r){
        this.segment = seg;
        this.r = r;
    }
    public Capsule(Float3 point1, Float3 point2, float r){
        segment = new Segment(point1, point2);
        this.r = r;
    }
}
