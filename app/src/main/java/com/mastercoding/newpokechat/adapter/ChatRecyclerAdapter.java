package com.mastercoding.newpokechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mastercoding.newpokechat.ChatActivity;
import com.mastercoding.newpokechat.R;
import com.mastercoding.newpokechat.model.ChatMsgModel;
import com.mastercoding.newpokechat.utils.AndroidUtil;
import com.mastercoding.newpokechat.utils.FirebaseUtil;


public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMsgModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMsgModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMsgModel model) {

        if (model.getSenderId().equals(FirebaseUtil.currentUserId())){
            holder.left_chat.setVisibility(View.GONE);
            holder.right_chat.setVisibility(View.VISIBLE);
            holder.right_chat_text.setText(model.getMessage());
        }else {
            holder.right_chat.setVisibility(View.GONE);
            holder.left_chat.setVisibility(View.VISIBLE);
            holder.left_chat_text.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_msg_recycler ,parent,false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout left_chat,right_chat;
        TextView left_chat_text,right_chat_text;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            left_chat=itemView.findViewById(R.id.left_chat);
            right_chat=itemView.findViewById(R.id.right_chat);
            left_chat_text=itemView.findViewById(R.id.left_chat_text);
            right_chat_text=itemView.findViewById(R.id.right_chat_text);


        }
    }
}
