package com.example.thecuong064.mycontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Profile extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private TextView nameDisplay, phoneNum, fbLink, address;
    Toolbar toolbar;
    RadioGroup genderChoice;
    RadioButton male,female;
    int position;
    boolean gender;
    private ImageView imgDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //find view
        nameDisplay = findViewById(R.id.nameDisplay);
        phoneNum = findViewById(R.id.phonenum);
        fbLink = findViewById(R.id.fblink);
        address = findViewById(R.id.address);
        imgDisplay = findViewById(R.    id.imgDisplay);
        genderChoice = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        //get data defining a blank content or existed account
        position = getIntent().getIntExtra("pos",-1);
        editProfile(position);

        //custom toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Gender choice
        genderChoice.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save_item:
                saveItem();
                finish();
                break;
            default:
                finish();
                break;
        }
        return true;
    }

    private void saveItem() {
        Intent dataIntent = new Intent();

        //prepare data returned
        dataIntent.putExtra("position",position);
        dataIntent.putExtra("returnName",nameDisplay.getText().toString());
        dataIntent.putExtra("returnPhone",phoneNum.getText().toString());
        dataIntent.putExtra("returnFb",fbLink.getText().toString());
        dataIntent.putExtra("returnAddress",address.getText().toString());
        dataIntent.putExtra("returnGender",gender);
        //set code OK and return data
        setResult(RESULT_OK,dataIntent);
    }


    private void editProfile(int position) {
        switch (position) {
            case -1:
                nameDisplay.setText("");
                phoneNum.setText("");
                fbLink.setText("");
                address.setText("");
                break;
            default:
                nameDisplay.setText(getIntent().getStringExtra("name"));
                phoneNum.setText(getIntent().getStringExtra("phone"));
                fbLink.setText(getIntent().getStringExtra("fb"));
                address.setText(getIntent().getStringExtra("address"));
                gender = getIntent().getBooleanExtra("gender",true);
                if (gender)
                {
                    imgDisplay.setImageResource(R.drawable.male);
                    male.setChecked(true);
                } else {
                    imgDisplay.setImageResource(R.drawable.female);
                    female.setChecked(true);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        super.onBackPressed();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (male.isChecked())
        {
            gender = true;
            imgDisplay.setImageResource(R.drawable.male);
        }
        else if (female.isChecked())
        {
            gender = false;
            imgDisplay.setImageResource(R.drawable.female);
        }
    }
}
