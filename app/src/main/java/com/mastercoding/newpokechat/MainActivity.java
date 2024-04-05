package com.mastercoding.newpokechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

public class  MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchbtn;
    Chatfragment chatfragment;
    Profilefragment profilefragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        chatfragment=new Chatfragment();
        profilefragment=new Profilefragment();
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        searchbtn=findViewById(R.id.main_searchbtn);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchUserActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.menu_chat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,chatfragment).commit();

                }
                if (item.getItemId()==R.id.menu_profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,profilefragment).commit();

                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_chat);


        GetFcmToken();
    }

    void GetFcmToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful()){
                    String token=task.getResult();
                    FirebaseUtil.currentUserDetails().update("fcmToken",token);
                }
            }
        });
    }
}