package com.mastercoding.newpokechat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mastercoding.newpokechat.adapter.RecentChatRecyclerAdapter;
import com.mastercoding.newpokechat.adapter.SearchUserRecyclerAdapter;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.model.chatroomModel;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

public class Chatfragment extends Fragment {


    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;
    public Chatfragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chatfragment, container, false);
       recyclerView=view.findViewById(R.id.recycleview);
       setUpRecyclerView();
        return view;
    }

    void setUpRecyclerView(){

        Query query= FirebaseUtil.allChatroomCollectionReference().whereArrayContains("userids",FirebaseUtil.currentUserId())
                .orderBy("lastmessageTimestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<chatroomModel> options=new FirestoreRecyclerOptions.Builder<chatroomModel>().setQuery(query,chatroomModel.class).build();

        adapter=new RecentChatRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}