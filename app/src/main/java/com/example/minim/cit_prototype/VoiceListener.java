package com.example.minim.cit_prototype;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceListener implements RecognitionListener {

    private final String TAG = com.example.minim.cit_prototype.VoiceListener.class.getSimpleName();
    private Intent intentSpeech;
    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> voiceInput;
    private Activity activity;

    public VoiceListener(Activity activity) {
        this.activity = activity;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(this);
        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
    }

    public void startListening() {
        if(intentSpeech != null && speechRecognizer != null) {
                Log.d(TAG, "startListening");
                speechRecognizer.startListening(intentSpeech);

        }
    }

    public String stopListening() {
        if(speechRecognizer != null && voiceInput != null) {
            Log.d(TAG, "stopListening");
            speechRecognizer.stopListening();
            return voiceInput.get(0);
        }
        else {
            return "Try again!";
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
        stopListening();
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError");

    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
        voiceInput = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
    }
}
