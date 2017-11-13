package com.app4each.autodrawer.view.fragments.base;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app4each.autodrawer.R;
import com.app4each.autodrawer.model.ImageData;
import com.app4each.autodrawer.model.ShapeData;
import com.app4each.autodrawer.utils.Consts;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by Vito on 11/12/2017.
 */

public abstract class BaseImageFragment extends Fragment implements Consts,RealmChangeListener {

    private Realm mRealm;
    protected ImageData mImageData;
    protected ImageView mImageView;

    // Abstract functions
    public abstract void process();
    public abstract String getType();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        mImageView = (ImageView) view.findViewById(R.id.image);
        mRealm = Realm.getDefaultInstance();
        mImageData = mRealm.where(ImageData.class).equalTo("type", getType()).findFirst();
        mImageData.addChangeListener(this);

        process();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null){
            mRealm.close();
            mRealm = null;
        }
    }

    @Override
    public void onChange(Object element) {
        process();
        if(mImageView != null)
            mImageView.invalidate();
    }

    protected Bitmap generateImageWithData(){
        int bitmap_width = ImageData.COLOMNS * ImageData.SHAPE_SIZE;
        int bitmap_height = ImageData.ROWS * ImageData.SHAPE_SIZE;
        Bitmap bitMap = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);  //creates bmp
        bitMap = bitMap.copy(bitMap.getConfig(), true);     //lets bmp to be mutable
        Canvas canvas = new Canvas(bitMap);                 //draw a canvas in defined bmp
        canvas.drawColor(Color.BLACK);

        Paint paint = new Paint();                          //define paint and paint color
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setStrokeWidth(0.5f);
        paint.setAntiAlias(true);

        int index = 0;
        for(ShapeData shape : mImageData.shapes) {
            // Possible Black shape on Black background. 00:20 on the clock. So f**k it.
            paint.setColor(shape.color);

            switch (getType()){
                case TYPE_SQUARE:
                    Rect rect = new Rect(ImageData.SHAPE_SIZE * (index % 4),
                            ImageData.SHAPE_SIZE * (index / 4),
                            ImageData.SHAPE_SIZE + ImageData.SHAPE_SIZE * (index % 4),
                            ImageData.SHAPE_SIZE + ImageData.SHAPE_SIZE * (index / 4));
                    canvas.drawRect(rect, paint);
                    break;

                case TYPE_CIRCLE:
                    canvas.drawCircle(ImageData.SHAPE_SIZE/2 + ImageData.SHAPE_SIZE * (index % 4),
                            ImageData.SHAPE_SIZE/2 + ImageData.SHAPE_SIZE * (index / 4),
                            ImageData.SHAPE_SIZE/2,
                             paint);
                    break;
            }
            index++;
        }
        return bitMap;
    }

}
