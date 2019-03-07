package com.akiranagai.myapplication.collision;

public class Line {
    Float3 point;
    Vec3 vector;

    public Line(){}
    public Line(Float3 point, Vec3 vector){
        this.point = point;
        this.vector = vector;
    }
    public Float3 getPoint(float operand) {
        return point.add(vector.multi(operand));  //= point + operand * vector
    }
}
