package com.app4each.autodrawer.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.app4each.autodrawer.R;
import com.app4each.autodrawer.controller.LogicStickyService;
import com.app4each.autodrawer.controller.PermitionsManager;
import com.app4each.autodrawer.controller.SettingsManager;
import com.app4each.autodrawer.utils.Consts;
import com.app4each.autodrawer.view.fragments.CircleFragment;
import com.app4each.autodrawer.view.fragments.SquareFragment;

public class MainActivity extends AppCompatActivity implements Consts{


    private CircleFragment mFragmentA;
    private SquareFragment mFragmentB;
    private String currentShape = TYPE_CIRCLE;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_a:

                    Log.d(getClass().getSimpleName(), "show A fragment");
                    fm.beginTransaction()
                            .remove(mFragmentB)
                            .replace(R.id.content, mFragmentA, TYPE_CIRCLE)
                            .commitAllowingStateLoss();
                    currentShape = TYPE_CIRCLE;
                    return true;

                case R.id.navigation_b:

                    Log.d(getClass().getSimpleName(), "show B fragment");
                    fm.beginTransaction()
                            .remove(mFragmentA)
                            .replace(R.id.content, mFragmentB, TYPE_SQUARE)
                            .commitAllowingStateLoss();
                    currentShape = TYPE_SQUARE;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentA = new CircleFragment();
        mFragmentB = new SquareFragment();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, mFragmentA,"A")
                .commit();
        currentShape = TYPE_CIRCLE;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this, LogicStickyService.class);
        //noinspection SimplifiableIfStatement
        if (id == R.id.time_1) {
            intent.setAction(ACTION_INTERVAL);
            intent.putExtra(EXTRA_TYPE, currentShape);
            intent.putExtra(EXTRA_REFRESH_INTERVAL, 500);
            startService(intent);
            return true;
        }else if (id == R.id.time_2) {
            intent.setAction(ACTION_INTERVAL);
            intent.putExtra(EXTRA_TYPE, currentShape);
            intent.putExtra(EXTRA_REFRESH_INTERVAL, 1000);
            startService(intent);
            return true;
        }else if (id == R.id.time_3) {
            intent.setAction(ACTION_INTERVAL);
            intent.putExtra(EXTRA_TYPE, currentShape);
            intent.putExtra(EXTRA_REFRESH_INTERVAL, 2000);
            startService(intent);
            return true;
        }else if (id == R.id.time_4) {
            intent.setAction(ACTION_INTERVAL);
            intent.putExtra(EXTRA_TYPE, currentShape);
            intent.putExtra(EXTRA_REFRESH_INTERVAL, 4000);
            startService(intent);
            return true;
        }else if (id == R.id.action_pause) {

            boolean isPaused = SettingsManager.isPaused(currentShape);
            SettingsManager.setIsPaused(currentShape, !isPaused);

            intent.setAction(ACTION_PAUSE);
            intent.putExtra(EXTRA_TYPE, currentShape);
            startService(intent);
            return true;
        }else if (id == R.id.action_clear) {
            intent.setAction(ACTION_CLEAR);
            intent.putExtra(EXTRA_TYPE, currentShape);
            startService(intent);
            return true;
        }else if (id == R.id.action_share) {
            if(!PermitionsManager.checkPermission(this)){
                return false;
            }
            ImageView imageView = (ImageView) findViewById(R.id.image);
            if(imageView != null) {

                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
                Uri bitmapUri = Uri.parse(bitmapPath);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/png");
                shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                startActivity(Intent.createChooser(shareIntent, "Share"));

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
