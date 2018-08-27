package com.example.minim.cit_prototype.Module.Fragment.Voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.example.minim.cit_prototype.R;

import java.util.Locale;

public class VoiceFragment extends Fragment implements View.OnClickListener, RecognitionListener{

    private final String TAG = VoiceFragment.class.getSimpleName();
    private Intent intentSpeech;
    private SpeechRecognizer speechRecognizer;
    private Activity activity;

    public VoiceFragment() {
        // Required empty public constructor
    }

    public static VoiceFragment newInstance() {
        VoiceFragment fragment = new VoiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        initService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_voice, container, false);
        return view;
    }

    private void initService() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(this);
        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
    }

    @Override
    public void onClick(View view) {
        speechRecognizer.startListening(intentSpeech);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
        //mainBinding.textViewSpeechStatus.setText("onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
       // mainBinding.textViewSpeechStatus.setText("onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
      //  mainBinding.textViewSpeechStatus.setText("onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
      //  mainBinding.textViewSpeechStatus.setText("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
        //stopCustomSTT();
      //  mainBinding.textViewSpeechStatus.setText("onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError");
       // mainBinding.textViewSpeechStatus.setText("onError");
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
       // mainBinding.textViewSpeechResult.setText(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
       // mainBinding.textViewSpeechStatus.setText("onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
        //mainBinding.textViewSpeechStatus.setText("onEvent");
    }

}
