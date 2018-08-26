package com.example.minim.cit_prototype.Module.Fragment.Main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.example.minim.cit_prototype.R;
import com.example.minim.cit_prototype.User;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

public class MainFragment extends Fragment {
    private final String TAG = MainFragment.class.getSimpleName();
    final String FRIEND_TOKEN = "d26cfd6907fa411b9c72aea1159e8d07";
    final String CHILD_TOKEN = "fdf9f71121544dbf8693b645623f2aff";
    //For Dialogflow
    private Gson gson = GsonFactory.getGson();
    private AIDataService aiDataService;

    //for view
    private ChatView chatView;
    //for chat
    private User myAccount;
    private User citBot;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        initChatView(view, container);
        return view;
    }

    private void initChatView(final View v, final ViewGroup container) {
        Log.d(TAG, "##### initChatView #####");

        int myId = 0;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_user);
        String myName = "Fish";
        myAccount = new User(myId, myName, icon);

        int botId = 1;
        String botName = "CIT";
        citBot = new User(botId, botName, icon);

        chatView = v.findViewById(R.id.chat_view);
        chatView.setRightBubbleColor(ContextCompat.getColor(getActivity(), R.color.green500));
        chatView.setLeftBubbleColor(Color.WHITE);
        chatView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_chat_view));
        chatView.setSendButtonColor(ContextCompat.getColor(getActivity(), R.color.lightBlue500));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setOptionIcon(R.drawable.ic_action_mic);
        chatView.setOptionButtonColor(Color.WHITE);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("뭐라고 할까요?");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new message
                final Message message = new Message.Builder()
                        .setUser(myAccount)
                        .setRightMessage(true)
                        .setMessageText(chatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                chatView.send(message);
                sendRequest(chatView.getInputText());
                //Reset edit text
                chatView.setInputText("");
            }
        });
        // Option button is for voice recognition
        chatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make popup
                View rootView = getLayoutInflater().inflate(R.layout.popup_voice, container, false);
                final PopupWindow popupWindow = new PopupWindow(rootView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

                //Voice Record Button
                Button voice = (Button) rootView.findViewById(R.id.btn_voice);
                voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        //implement voice recording during button click

                        popupWindow.dismiss();
                    }
                });
                //close button
                Button close = (Button) rootView.findViewById(R.id.btn_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        chatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(Message message) {
                String messageText = message.getMessageText();
                if(messageText.contains("#")) {
                    sendRequest(messageText.substring(1));
                    /*
                    final Message nmessage = new Message.Builder()
                            .setUser(myAccount)
                            .setRightMessage(true)
                            .setMessageText(messageText.substring(1))
                            .hideIcon(true)
                            .build();
                    //Set to chat view
                    chatView.send(nmessage);
                    */
                }
            }
        });
    }

    /*
     * AIRequest should have query OR event
     */
    private void sendRequest(String text) {
        Log.d(TAG, text);
        final String queryString = String.valueOf(text);
        final String eventString = null;
        final String contextString = null;

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        new AiTask().execute(queryString, eventString, contextString);
    }

    public class AiTask extends AsyncTask<String, Void, AIResponse> {
        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            String event = params[1];
            String context = params[2];

            if (!TextUtils.isEmpty(query)) {
                request.setQuery(query);
            }

            if (!TextUtils.isEmpty(event)) {
                request.setEvent(new AIEvent(event));
            }

            RequestExtras requestExtras = null;
            if (!TextUtils.isEmpty(context)) {
                final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
                requestExtras = new RequestExtras(contexts, null);
            }

            try {
                return aiDataService.request(request, requestExtras);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }


    private void onResult(final AIResponse response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Variables
                gson.toJson(response);
                final Status status = response.getStatus();
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();
                final Metadata metadata = result.getMetadata();
                final HashMap<String, JsonElement> params = result.getParameters();

                // Logging
                Log.d(TAG, "onResult");
                Log.i(TAG, "Received success response");
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
                Log.i(TAG, "Action: " + result.getAction());
                Log.i(TAG, "Speech: " + speech);

                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s",
                                entry.getKey(), entry.getValue().toString()));
                    }
                }

                JSONObject job = null;
                try {
                    job = new JSONObject(speech);
                } catch (JSONException e) {
                    Log.i(TAG, "Speech was String");
                }

                if (job == null) {
                    //Response is simple String
                    String[] sentences = speech.split("\n");
                    //Update view to bot says
                    for (int i = 0; i < sentences.length; i++) {
                        final Message receivedMessage = new Message.Builder()
                                .setUser(citBot)
                                .setRightMessage(false)
                                .setMessageText(sentences[i])
                                .build();
                        chatView.receive(receivedMessage);
                    }
                }
                else {
                    //Response is JSON
                    //We have to make Graph with it
                }
            }
        });
    }

    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    private void onError(final AIError error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, error.toString());
            }
        });
    }


    private void initService() {
        String token = FRIEND_TOKEN;
        /*
        int agent_type = PreferencesManager.INSTANCE.loadIntegerSharedPreferences(getActivity(), ConstVariables.Companion.getPREF_KEY_AGENT_TYPE());
        switch(agent_type) {
            case 1:
                token = FRIEND_TOKEN;
            case 2:
                token = CHILD_TOKEN;
            default:

        }
        */
        AIConfiguration config = new AIConfiguration(token,
                AIConfiguration.SupportedLanguages.fromLanguageTag("ko"),
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(getActivity(), config);
    }

}
