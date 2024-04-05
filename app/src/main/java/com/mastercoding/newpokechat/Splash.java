package com.mastercoding.newpokechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.utils.AndroidUtil;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (FirebaseUtil.isLoggedin() && getIntent().getExtras()!=null){
                String userId=getIntent().getExtras().getString("userId");
                FirebaseUtil.alluserCollectionreference().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        UserModel model=task.getResult().toObject(UserModel.class);
                        Intent mainintent=new Intent(Splash.this,MainActivity.class);
                        mainintent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivities(new Intent[]{mainintent});
                        Intent intent=new Intent(Splash.this,ChatActivity.class);
                        AndroidUtil.passUserModelASintent(intent,model);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirebaseUtil.isLoggedin()) {
                        Intent intent=new Intent(Splash.this,MainActivity.class);
                        startActivities(new Intent[]{intent});
                    }else{
                        Intent intent=new Intent(Splash.this,Loginphone.class);
                        startActivities(new Intent[]{intent});
                    }

                    finish();
                }
            },3000);
        }


    }
}