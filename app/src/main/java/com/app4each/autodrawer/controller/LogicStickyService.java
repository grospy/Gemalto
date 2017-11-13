package com.app4each.autodrawer.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app4each.autodrawer.model.CreationTime;
import com.app4each.autodrawer.model.ImageData;
import com.app4each.autodrawer.utils.Consts;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

/**
 * Created by Vito on 11/12/2017.
 */

public class LogicStickyService extends Service implements Consts{


    private Handler mHandler = new Handler();
    private Timer mTimer;
    private UpdateShapeTask mTaskA;
    private UpdateShapeTask mTaskB;

//    private ImageData mImageDataA;
//    private ImageData mImageDataB;

    private class UpdateShapeTask extends TimerTask
    {
        ImageData imageData;
        public UpdateShapeTask(ImageData imageData){
            this.imageData = imageData;
        }
        @Override
        public void run() {

            if(imageData!= null){

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                imageData.shapes++;

                                if(imageData.shapes >= ImageData.ROWS * ImageData.COLOMNS) {
                                    imageData.shapes = 0;
                                    imageData.shapesCreationTime.clear();
                                }

                                CreationTime creationTime = new CreationTime(System.currentTimeMillis());
                                imageData.shapesCreationTime.add(creationTime);
                            }
                        });
                    }
                });
            }
        }
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d("Service","onStartCommand");

        // Handle type specific actions
        if(intent.hasExtra(EXTRA_TYPE)) {

            final String shapeType = intent.getStringExtra(EXTRA_TYPE);

            switch (intent.getAction()) {
                case ACTION_CLEAR:
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ImageData imageData = realm.where(ImageData.class).equalTo("type", shapeType).findFirst();
                            imageData.shapes = 0;
                            imageData.shapesCreationTime.clear();
                        }
                    });
                    break;

                case ACTION_INTERVAL:
                    long newInterval = intent.getLongExtra(EXTRA_REFRESH_INTERVAL, 1000);
                    SettingsManager.setInterval(shapeType, newInterval);
                    break;
            }
        }

        // Clean Up existing timer tasks
        if(mTaskA != null)
        {
            mTaskA.cancel();
        }
        if(mTaskB != null)
        {
            mTaskB.cancel();
        }
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer.purge();
        }

        // Initiate new tasks if needed
        mTimer = new Timer();

        if(!SettingsManager.isPaused(TYPE_CIRCLE)) {
            long periodA = SettingsManager.getInterval(TYPE_CIRCLE);
            ImageData imageData = Realm.getDefaultInstance().where(ImageData.class).equalTo("type", TYPE_CIRCLE).findFirst();
            mTimer.schedule(new UpdateShapeTask(imageData), 0, periodA);
        }

        if(!SettingsManager.isPaused(TYPE_SQUARE)) {
            long periodB = SettingsManager.getInterval(TYPE_SQUARE);
            ImageData imageData = Realm.getDefaultInstance().where(ImageData.class).equalTo("type", TYPE_SQUARE).findFirst();
            mTimer.schedule(new UpdateShapeTask(imageData), 0, periodB);
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
