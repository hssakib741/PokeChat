package com.mastercoding.newpokechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mastercoding.newpokechat.adapter.SearchUserRecyclerAdapter;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

import org.checkerframework.common.returnsreceiver.qual.This;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchBtn,backBtn;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchInput=findViewById(R.id.searchuserinput);
        searchBtn=findViewById(R.id.search_user_btn);
        backBtn=findViewById(R.id.backbtn);
        recyclerView=findViewById(R.id.search_user_recycle_view);
        searchInput.requestFocus();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm=searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length()<3){
                    searchInput.setError("Invalid Username");
                    return;
                }
                setupSearchRecycleview(searchTerm);
            }
        });
    }

    void setupSearchRecycleview(String searchTerm){

        Query query= FirebaseUtil.alluserCollectionreference().whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username", searchTerm+'\uf8ff');
        FirestoreRecyclerOptions<UserModel>options=new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();

        adapter=new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }
}