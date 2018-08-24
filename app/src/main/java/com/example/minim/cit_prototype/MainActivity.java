package com.example.minim.cit_prototype;

import Utils.PreferencesManager;
import ai.api.model.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    //For Dialogflow
    private Gson gson = GsonFactory.getGson();
    private AIDataService aiDataService;
    final String ACCESS_TOKEN = "d26cfd6907fa411b9c72aea1159e8d07";
    //for view
    private ChatView chatView;
    private ListView mListView = null;
    //for chat
    private User myAccount;
    private User citBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initChatView();

        //Language, Dialogflow Client access token
        final LanguageConfig config = new LanguageConfig("ko", ACCESS_TOKEN);
        final String[] items = {"MENU_1", "MENU_2"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        mListView = (ListView) findViewById(R.id.drawer_list_main);
        mListView.setAdapter(adapter);
        initService(config);
    }

    private void initChatView() {
        Log.d(TAG, "##### iniunChatView #####");
        int myId = 0;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_user);
        String myName = "Fish";
        myAccount = new User(myId, myName, icon);

        int botId = 1;
        String botName = "CIT";
        citBot = new User(botId, botName, icon);

        chatView = findViewById(R.id.chat_view);
        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        chatView.setLeftBubbleColor(Color.WHITE);
        chatView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_chat_view));
        chatView.setSendButtonColor(ContextCompat.getColor(this, R.color.lightBlue500));
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
        /* Option button is for voice recognition
        chatView.setOnClickOptionButtonListener(new View.OnClickListener() {
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
        */
        chatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(Message message) {
                final Message nmessage = new Message.Builder()
                        .setUser(myAccount)
                        .setRightMessage(true)
                        .setMessageText("Bubble Click")
                        .hideIcon(true)
                        .build();
                //Set to chat view
                chatView.send(nmessage);
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


    private void onResult (final AIResponse response) {
        runOnUiThread(new Runnable() {
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
                }
                catch(JSONException e) {
                    Log.i(TAG, "Speech was String");
                }

                if(job == null) {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, error.toString());
            }
        });
    }



    private void initService(final LanguageConfig languageConfig) {
        final AIConfiguration.SupportedLanguages lang =
                AIConfiguration.SupportedLanguages.fromLanguageTag(languageConfig.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(languageConfig.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(this, config);
    }
}