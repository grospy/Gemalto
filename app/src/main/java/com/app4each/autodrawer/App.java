package com.app4each.autodrawer;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;

import com.app4each.autodrawer.controller.LogicStickyService;
import com.app4each.autodrawer.controller.SettingsManager;
import com.app4each.autodrawer.model.ImageData;
import com.app4each.autodrawer.utils.Consts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

/**
 * Created by Vito on 11/12/2017.
 */

public class App extends Application implements Consts{

    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Realm.init(instance);

        SettingsManager.init(instance);

        //Init DB records
        initDBRecords();

        startService(new Intent(instance, LogicStickyService.class));
    }

    private void initDBRecords() {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ImageData record = realm.where(ImageData.class).equalTo("type", TYPE_CIRCLE).findFirst();
                    if(record == null)
                        record = realm.createObject(ImageData.class);

                    record.type = TYPE_CIRCLE;

                    ImageData record2 = realm.where(ImageData.class).equalTo("type", TYPE_SQUARE).findFirst();
                    if(record2 == null)
                        record2 = realm.createObject(ImageData.class);

                    record2.type = TYPE_SQUARE;
                }
            });
    }
//
//    // Save file
//    public static String saveBitmapToInternalStorage(Bitmap bitmapImage){
//        ContextWrapper cw = new ContextWrapper(instance);
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,"image.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return directory.getAbsolutePath();
//    }
}
