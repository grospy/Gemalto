package com.app4each.autodrawer.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.app4each.autodrawer.model.ImageData;

import io.realm.Realm;

/**
 * Created by Vito on 11/12/2017.
 */

public class LogicStickyService extends Service{

    public static final String EXTRA_TIME_FRAME = "time_frame";
    public static final String EXTRA_CLEAR = "clear_data";

    private ImageData mImageDataA;
    private ImageData mImageDataB;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                mImageDataA = realm.where(ImageData.class).equalTo("type", ImageData.CIRCLE).findFirst();
                if (mImageDataA == null) {
                    mImageDataA = realm.createObject(ImageData.class);
                    mImageDataA.type = ImageData.CIRCLE;
                    mImageDataA.shapes = 0;
                }

                mImageDataB = realm.where(ImageData.class).equalTo("type", ImageData.SQUARE).findFirst();
                if(mImageDataB == null){
                    mImageDataB = realm.createObject(ImageData.class);
                    mImageDataB.type = ImageData.SQUARE;
                    mImageDataB.shapes = 0;
                }
            }
        });
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
