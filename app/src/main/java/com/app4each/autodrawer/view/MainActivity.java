package com.app4each.autodrawer.view;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app4each.autodrawer.R;
import com.app4each.autodrawer.view.fragments.CircleFragment;
import com.app4each.autodrawer.view.fragments.SquareFragment;

public class MainActivity extends AppCompatActivity {


    private CircleFragment mFragmentA;
    private SquareFragment mFragmentB;
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
                            .replace(R.id.content, mFragmentA,"A")
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                    return true;

                case R.id.navigation_b:

                    Log.d(getClass().getSimpleName(), "show B fragment");
                    fm.beginTransaction()
                            .remove(mFragmentA)
                            .replace(R.id.content, mFragmentB,"B")
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
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
                .addToBackStack(null)
                .commit();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
