package com.app4each.autodrawer.model;

import io.realm.RealmObject;

/**
 * Created by Vito on 11/12/2017.
 */

public class ShapeData extends RealmObject {

    public Long shapeCreationTime;
    public int color;

    public ShapeData(){}
    public ShapeData(int color, long time){
        this.color = color;
        this.shapeCreationTime = time;
    }
}
