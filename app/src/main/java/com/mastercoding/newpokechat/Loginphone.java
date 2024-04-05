package com.mastercoding.newpokechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class Loginphone extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneinput;
    Button sendotpbtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphone);

        countryCodePicker=findViewById(R.id.login_countrycode);
        phoneinput=findViewById(R.id.login_mobile_number);
        progressBar=findViewById(R.id.login_progress_bar);
        sendotpbtn=findViewById(R.id.send_otp_btn);
        progressBar.setVisibility(View.GONE);



        countryCodePicker.registerCarrierNumberEditText(phoneinput);

        sendotpbtn.setOnClickListener(v -> {
            if(!countryCodePicker.isValidFullNumber()){
                phoneinput.setError("Phone Number Not Valid");
                return;

            }
            Intent intent=new Intent(Loginphone.this,Loginotp.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivities(new Intent[]{intent});

        });


    }
}