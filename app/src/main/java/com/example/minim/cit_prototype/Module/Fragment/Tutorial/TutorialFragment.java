package com.example.minim.cit_prototype.Module.Fragment.Tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.minim.cit_prototype.R;

import org.greenrobot.eventbus.EventBus;

import Common.CommonEventBusObject;
import Common.ConstVariables;
import Utils.PreferencesManager;


public class TutorialFragment extends Fragment implements View.OnClickListener {
    private final String TAG = TutorialFragment.class.getSimpleName();
    private ImageView mButton1, mButton2, mButton3;

    public TutorialFragment() {
    }

    public static TutorialFragment newInstance() {
        TutorialFragment fragment = new TutorialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        initializeUI(view);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                mButton1.setSelected(true);
                if (mButton2.isSelected()) {
                    mButton2.setSelected(false);
                }
                break;

            case R.id.button2:
                mButton2.setSelected(true);
                if (mButton1.isSelected()) {
                    mButton1.setSelected(false);
                }
                break;

            case R.id.cancel_action:
                if (mButton1.isSelected() && mButton2.isSelected()) {
                    Log.d(TAG, "##### onClick cancel_action ##### multiSelected Exception");
                }
                if (mButton1.isSelected()) {
                    Log.d(TAG, "##### onClick cancel_action ##### mButton1 is selected");
                    savePref(ConstVariables.Companion.getPREF_AGENT_TYPE_GRAND_CHILD());

                } else if (mButton2.isSelected()) {
                    Log.d(TAG, "##### onClick cancel_action ##### mButton2 is selected");
                    savePref(ConstVariables.Companion.getPREF_AGENT_TYPE_FRIEND());
                }
                break;
        }

    }

    private void initializeUI(View v) {
        Log.d(TAG, "##### initializeUI #####");
        mButton1 =  v.findViewById(R.id.button1);
        mButton1.setOnClickListener(this);

        mButton2 =  v.findViewById(R.id.button2);
        mButton2.setOnClickListener(this);

        mButton3 =  v.findViewById(R.id.cancel_action);
        mButton3.setOnClickListener(this);

    }

    private void savePref(int type) {
        Log.d(TAG, "##### savePRef ##### type : " + type);
        if (type == ConstVariables.Companion.getPREF_AGENT_TYPE_FRIEND()) {
            PreferencesManager.INSTANCE.saveIntegerPreferencesco(getActivity(), ConstVariables.Companion.getPREF_KEY_AGENT_TYPE(), ConstVariables.Companion.getPREF_AGENT_TYPE_GRAND_CHILD());
        } else if (type == ConstVariables.Companion.getPREF_AGENT_TYPE_GRAND_CHILD()) {
            PreferencesManager.INSTANCE.saveIntegerPreferencesco(getActivity(), ConstVariables.Companion.getPREF_KEY_AGENT_TYPE(), ConstVariables.Companion.getPREF_AGENT_TYPE_FRIEND());
        }
        EventBus.getDefault().post(new CommonEventBusObject(ConstVariables.Companion.getEVENTBUS_TYPE_TUTORIAL_DONE(), 1));
    }

}
