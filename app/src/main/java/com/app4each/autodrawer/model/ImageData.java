package com.app4each.autodrawer.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ImageData extends RealmObject{

    // Can be changed if needed.
    public static final int COLOMNS = 4;
    public static final int ROWS = 6;

    // Image types
    public static final int SHAPE_SIZE = 50;


    public ImageData(){

    }

    public ImageData(String type){
        this.type = type;
    }

    public String type;
    public RealmList<ShapeData> shapes  = new RealmList<>();

}