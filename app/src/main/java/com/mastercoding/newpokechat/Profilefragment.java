package com.mastercoding.newpokechat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;
import com.mastercoding.newpokechat.model.UserModel;
import com.mastercoding.newpokechat.utils.AndroidUtil;
import com.mastercoding.newpokechat.utils.FirebaseUtil;

import org.w3c.dom.Text;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Profilefragment extends Fragment {

ImageView profilepic;
EditText usernameInput,phoneInput;
Button update_btn;
ProgressBar progressBar;
TextView logOut;

UserModel currentUserModel;
ActivityResultLauncher<Intent> imagePickerLauncher;
Uri selectedimageUri;

    public Profilefragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                if (result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    if(data!=null && data.getData()!=null){
                        selectedimageUri=data.getData();
                        AndroidUtil.setprofilePic(getContext(),selectedimageUri,profilepic);
                    }
                }
                }
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_profilefragment, container, false);

       profilepic=view.findViewById(R.id.profile_img_view);
       usernameInput=view.findViewById(R.id.profile_username);
       phoneInput=view.findViewById(R.id.profile_phone);
        update_btn=view.findViewById(R.id.profile_update_btn);
        progressBar=view.findViewById(R.id.progress_bar);
        logOut=view.findViewById(R.id.logout_btn);

        getUserdata();

        update_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBtnclick();
            }
        });

        logOut.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseUtil.logout();
                            Intent intent=new Intent(getContext(), Splash.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        profilepic.setOnClickListener((v -> {

            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickerLauncher.launch(intent);
                    return null;
                }
            });
        }));

       return view;
    }

    void updateBtnclick() {
        String NewUsername = usernameInput.getText().toString();
        if (NewUsername.isEmpty() || NewUsername.length() < 3) {
            usernameInput.setError("Username Length Should Be Al Least 3 Character");
            return;
        }

        currentUserModel.setUsername(NewUsername);
        setInProgress(true);

        if (selectedimageUri!=null){
            FirebaseUtil.getCurrentprofilepic().putFile(selectedimageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    updateTofirestore();
                }
            });

        }
        else{
            updateTofirestore();
        }


    }

    void updateTofirestore(){
            FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    setInProgress(false);
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated Successfully");
                    }
                    else {
                        AndroidUtil.showToast(getContext(),"Update Failed");

                    }
                }
            });
    }

    void getUserdata(){
        setInProgress(true);

        FirebaseUtil.getCurrentprofilepic().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri=task.getResult();
                    AndroidUtil.setprofilePic(getContext(),uri,profilepic);
                }
            }
        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                currentUserModel=task.getResult().toObject(UserModel.class);
                usernameInput.setText(currentUserModel.getUsername());
                phoneInput.setText(currentUserModel.getPhone());
            }
        });
    }

    void setInProgress(boolean inProgress){

        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            update_btn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            update_btn.setVisibility(View.VISIBLE);
        }
    }
}