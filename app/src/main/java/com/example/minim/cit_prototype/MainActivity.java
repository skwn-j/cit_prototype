package com.example.minim.cit_prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.minim.cit_prototype.Module.Fragment.Main.MainFragment;
import com.example.minim.cit_prototype.Module.Fragment.Tutorial.TutorialFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import Common.CommonEventBusObject;
import Common.ConstVariables;
import Utils.PreferencesManager;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private View mContentView = null;
    private ImageView mMenuButton, mMyProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "##### onCreate #####");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mContentView = findViewById(R.id.view_content);
        initializeUI();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy #####");
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initializeUI() {
        Log.d(TAG, "##### initializeUI #####");
        mMenuButton = findViewById(R.id.btn_menu);
        mMyProfileButton = findViewById(R.id.btn_myprofile);

        handleFragment(PreferencesManager.INSTANCE.loadIntegerSharedPreferences(this, ConstVariables.Companion.getPREF_KEY_AGENT_TYPE()));
    }

    private void handleFragment(int val) {
        Log.d(TAG, "##### handleFragment ##### val : " + val);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(mContentView.getId());
        if (val == ConstVariables.Companion.getEVENTBUS_TYPE_TUTORIAL_DONE()) {
            if (fragment != null) {
                MainFragment mainFragment = MainFragment.newInstance();
                fragmentTransaction.add(mContentView.getId(), mainFragment, "main");
                fragmentTransaction.replace(mContentView.getId(), mainFragment, "main");
                fragmentTransaction.show(mainFragment);
                mMenuButton.setVisibility(View.VISIBLE);
                mMyProfileButton.setVisibility(View.VISIBLE);
            }

        } else if (val == ConstVariables.Companion.getPREF_AGENT_TYPE_NONE()) {
            if (fragment == null) {
                TutorialFragment tutorialFragment = TutorialFragment.newInstance();
                fragmentTransaction.add(mContentView.getId(), tutorialFragment, "main");
                fragmentTransaction.replace(mContentView.getId(), tutorialFragment, "main");
                fragmentTransaction.show(tutorialFragment);
            }
        } else {
            if (fragment == null) {
                MainFragment mainFragment = MainFragment.newInstance();
                fragmentTransaction.add(mContentView.getId(), mainFragment, "main");
                fragmentTransaction.replace(mContentView.getId(), mainFragment, "main");
                fragmentTransaction.show(mainFragment);
                mMenuButton.setVisibility(View.VISIBLE);
                mMyProfileButton.setVisibility(View.VISIBLE);
            }
        }

        fragmentTransaction.commit();
    }

    @Subscribe
    void onEvent(CommonEventBusObject object) {
        Log.d(TAG, "##### onEvent #####");
        if (object.getType() == ConstVariables.Companion.getEVENTBUS_TYPE_TUTORIAL_DONE()) {
            handleFragment(object.getType());
        }
    }
}