package com.akiranagai.myapplication.collision;

import java.util.Vector;

public class Segment extends Line {
    public Segment(){}
    public Segment(Float3 point, Vec3 vector){
        super(point, vector);
    }
    public Segment(Float3 point1, Float3 point2){
        super(point1, new Vec3(point2.sub(point1)));
    }
    public Float3 getEndPoint(){
        return point.add(vector);
    }
}
