package com.seven.eajy.gif.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.seven.eajy.gif.R;
import com.seven.eajy.gif.fragment.DecomposeGifFragment;
import com.seven.eajy.gif.fragment.EditImageFragment;
import com.seven.eajy.gif.fragment.MakeGifFragment;

public class MainActivity extends AppCompatActivity {

    private DecomposeGifFragment decomposeGifFragment;
    private EditImageFragment editImageFragment;
    private MakeGifFragment makeGifFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (decomposeGifFragment == null) {
            decomposeGifFragment = new DecomposeGifFragment();
        }
        transactionFragment(decomposeGifFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_decompose:
                    if (decomposeGifFragment == null) {
                        decomposeGifFragment = new DecomposeGifFragment();
                    }
                    transactionFragment(decomposeGifFragment);
                    return true;
                case R.id.menu_edit:
                    if (editImageFragment == null) {
                        editImageFragment = new EditImageFragment();
                    }
                    transactionFragment(editImageFragment);
                    return true;
                case R.id.menu_make:
                    if (makeGifFragment == null) {
                        makeGifFragment = new MakeGifFragment();
                    }
                    transactionFragment(makeGifFragment);
                    return true;
            }
            return false;
        }
    };

    private void transactionFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {

        } else if (item.getItemId() == R.id.menu_about) {

        }
        return true;
    }

}
