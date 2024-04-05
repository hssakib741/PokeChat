package com.mastercoding.newpokechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreKtxRegistrar;
import com.mastercoding.newpokechat.utils.AndroidUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class
Loginotp extends AppCompatActivity {

    String phonenumber;
    String verificationcode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;

    EditText otpinput;
    Button nextbtn;
    ProgressBar progressBar;
    TextView resendotpTextview;

    Long timeoutSeconds=60L;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginotp);

        otpinput=findViewById(R.id.login_otp);
        nextbtn=findViewById(R.id.login_next_btn);
        progressBar=findViewById(R.id.login_progress_bar);
        resendotpTextview=findViewById(R.id.resend_otp);

        phonenumber=getIntent().getExtras().getString("phone");
        sendotp(phonenumber,false);


        nextbtn.setOnClickListener(v -> {
            String enteredotp=otpinput.getText().toString();
            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationcode,enteredotp);
            signIn(credential);
        });

        resendotpTextview.setOnClickListener(v -> {
            sendotp(phonenumber,false);
        });
    }

    void sendotp(String phonenumber,boolean isResend){

        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder=PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phonenumber)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(),"OTP Verification Failed");
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationcode=s;
                        ResendingToken=forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"OTP Sent Successfully");
                        setInProgress(false);

                    }
                });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());

        }

    }
    void setInProgress(boolean inProgress){

        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextbtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            nextbtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);

                if(task.isSuccessful()){
                    Intent intent=new Intent(Loginotp.this,Loginusername.class);
                    intent.putExtra("phone",phonenumber);
                    startActivities(new Intent[]{intent});

                }else{
                    AndroidUtil.showToast(getApplicationContext(),"OTP Verification Failed");
                }
            }
        });
    }

    void startResendTimer(){
        resendotpTextview.setEnabled(false);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendotpTextview.setText("Resend OTP in "+timeoutSeconds+"Seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds=60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                            resendotpTextview.setEnabled(true);
                    });
                }

            }
        },0,1000);
    }

}