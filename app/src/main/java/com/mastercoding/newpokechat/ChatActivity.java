package com.mastercoding.newpokechat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mastercoding.newpokechat.adapter.ChatRecyclerAdapter;
import com.mastercoding.newpokechat.adapter.SearchUserRecyclerAdapter;
import com.mastercoding.newpokechat.model.ChatMsgModel;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.model.chatroomModel;
import com.mastercoding.newpokechat.utils.AndroidUtil;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ChatActivity extends AppCompatActivity {

    UserModel otheruser;

    String chatroomId;
    EditText msginput;
    ChatRecyclerAdapter adapter;

    chatroomModel chatroommodel;
    ImageButton sendmsgbtn,backbtn;
    TextView otherUsername;
    RecyclerView recyclerView;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Add this line to set the logger level to FINE
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);



        otheruser= AndroidUtil.getUserModelFromintent(getIntent());
        chatroomId= FirebaseUtil.getChatroomid(FirebaseUtil.currentUserId(),otheruser.getUserId());



        msginput=findViewById(R.id.chat_msg_input);
        sendmsgbtn=findViewById(R.id.send_btn);
        backbtn=findViewById(R.id.backbtn);
        otherUsername=findViewById(R.id.other_username);
        recyclerView=findViewById(R.id.chat_recycler_view);
        imageView=findViewById(R.id.profile_pic_layout);

        FirebaseUtil.getOtherUserprofilepic(otheruser.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri=task.getResult();
                    AndroidUtil.setprofilePic(ChatActivity.this,uri,imageView);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        otherUsername.setText(otheruser.getUsername());

        sendmsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=msginput.getText().toString();
                if (message.isEmpty()){
                    return;
                }
                    sendmessageToUser(message);

            }
        });

        getOrCreatechatroomModel();
        setupChatRecyclerView();
    }

    void setupChatRecyclerView(){
        Query query= FirebaseUtil.getChatroomMessageReference(chatroomId).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMsgModel> options=new FirestoreRecyclerOptions.Builder<ChatMsgModel>().setQuery(query,ChatMsgModel.class).build();
        adapter=new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void sendmessageToUser(String message){

        chatroommodel.setLastmessageTimestamp(Timestamp.now());
        chatroommodel.setLastMessageSenderid(FirebaseUtil.currentUserId());
        chatroommodel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroommodel);

        ChatMsgModel chatmsgModel=new ChatMsgModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatmsgModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    msginput.setText("");
                    sendNotification(message);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    void getOrCreatechatroomModel(){

        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatroommodel=task.getResult().toObject(chatroomModel.class);
                if (chatroommodel==null){
                    chatroommodel=new chatroomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otheruser.getUserId()),
                            Timestamp.now(),""

                    );

                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroommodel);
                }
            }
        });

    }

    void sendNotification(String message){
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        UserModel currentUser=task.getResult().toObject(UserModel.class);

                        try {
                                JSONObject jsonObject=new JSONObject();

                                JSONObject notificationObject=new JSONObject();

                                notificationObject.put("title",currentUser.getUsername());
                                notificationObject.put("body",message);

                                JSONObject dataObject=new JSONObject();
                                dataObject.put("userId",currentUser.getUserId());

                                jsonObject.put("notification",notificationObject);
                                jsonObject.put("data",dataObject);
                                jsonObject.put("to",otheruser.getFcmToken());

                                callApi(jsonObject);

                        }catch (Exception e){

                        }
                    }
                }
            });
    }

    void callApi(JSONObject jsonObject){
        MediaType JSON =MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url="https://fcm.googleapis.com/fcm/send";
        RequestBody body=RequestBody.create(jsonObject.toString(),JSON);
        Request request=new Request.Builder().url(url).post(body).header("Authorization","Bearer AAAAx30kinI:APA91bE_fsZbZOPaDtAEcR2qPW3oAdFCShAL8gyqF154zhF01MgICdSkCqs3RmRDbkSp-m8wlPmh6lNKUkk3qlE5RMT-nXCDTtENf0ViBsMsvdyBnsT0Vrb0pje9yzc9UcbXDNCHycCH")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });




    }
}