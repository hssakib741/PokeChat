package com.mastercoding.newpokechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mastercoding.newpokechat.ChatActivity;
import com.mastercoding.newpokechat.R;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.model.chatroomModel;
import com.mastercoding.newpokechat.utils.AndroidUtil;
import com.mastercoding.newpokechat.utils.FirebaseUtil;


public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<chatroomModel, RecentChatRecyclerAdapter.chatroomModelViewHolder> {

    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<chatroomModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull chatroomModelViewHolder holder, int position, @NonNull chatroomModel model) {
     FirebaseUtil.getOtherUserFromChatroom(model.getUserids()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

             boolean lastMsgSendByme =model.getLastMessageSenderid().equals(FirebaseUtil.currentUserId());
             UserModel otherUserModel=task.getResult().toObject(UserModel.class);
                FirebaseUtil.getOtherUserprofilepic(otherUserModel.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri uri=task.getResult();
                            AndroidUtil.setprofilePic(context,uri,holder.profilepic);
                        }
                    }
                });
             holder.usernameText.setText(otherUserModel.getUsername());
             if (lastMsgSendByme){
                 holder.lastMsgText.setText("You: "+model.getLastMessage());
             }
             else {
                 holder.lastMsgText.setText(model.getLastMessage());
             }
             holder.lastMsgTime.setText(FirebaseUtil.timestamptoString(model.getLastmessageTimestamp()));

             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent=new Intent(context, ChatActivity.class);
                     AndroidUtil.passUserModelASintent(intent,otherUserModel);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     context.startActivity(intent);
                 }
             });
         }
     });
    }

    @NonNull
    @Override
    public chatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler,parent,false);
        return new chatroomModelViewHolder(view);
    }

    class chatroomModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText,lastMsgText,lastMsgTime;
        ImageView profilepic;
        public chatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText=itemView.findViewById(R.id.username_text);
            lastMsgText=itemView.findViewById(R.id.last_msg_text);
            lastMsgTime=itemView.findViewById(R.id.last_msg_time_text);
            profilepic=itemView.findViewById(R.id.profilepicimage);

        }
    }
}

