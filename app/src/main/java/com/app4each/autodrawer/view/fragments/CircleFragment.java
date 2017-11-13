package com.app4each.autodrawer.view.fragments;

import android.graphics.Bitmap;
import com.app4each.autodrawer.model.ImageData;
import com.app4each.autodrawer.view.fragments.base.BaseImageFragment;

/**
 * Created by Vito on 11/12/2017.
 */

public class CircleFragment extends BaseImageFragment {


    @Override
    public void process(){
        if(item == null)
            return;

        Bitmap result = generateImageWithData();
        mImageView.setImageBitmap(result);
    }

    @Override
    public String getType(){
        return ImageData.CIRCLE;
    }

}
