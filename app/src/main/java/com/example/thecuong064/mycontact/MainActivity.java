package com.example.thecuong064.mycontact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private ArrayList<Contact> contactArrayList = new ArrayList<>();
private ListView contactList;
private ContactAdapter contactAdapter;
int REQUEST_CODE_EDIT = 1;
int REQUEST_CODE_NEW = 2;
String SAVE_KEY = "list_save";
private Toolbar toolBar;
private Bitmap bitmap;
byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieving the data
        if (getContactList(SAVE_KEY)!=null)
            contactArrayList = getContactList(SAVE_KEY);

        contactList = findViewById(R.id.listView);
        contactAdapter = new ContactAdapter(this,contactArrayList);

        contactList.setAdapter(contactAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(MainActivity.this,Profile.class);

                //send data to sub-activity
                intent.putExtra("pos",position);
                intent.putExtra("name",contactArrayList.get(position).getName());
                intent.putExtra("phone",contactArrayList.get(position).getPhoneNumber());
                intent.putExtra("fb",contactArrayList.get(position).getFbLink());
                intent.putExtra("address",contactArrayList.get(position).getAddress());
                if (contactArrayList.get(position).getImgResource() == R.drawable.male)
                    intent.putExtra("gender", true);
                else intent.putExtra("gender", false);

                startActivityForResult(intent,REQUEST_CODE_EDIT);
            }
        });

        //set the custom toolbar
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle("My Contact");
        toolBar.setSubtitle("You have " + contactArrayList.size() + " contact(s)");

        setSupportActionBar(toolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_item:
                Toast.makeText(this, "Not yet finish this content", Toast.LENGTH_SHORT).show();
                Log.e("abcd","active search");
                break;
            case R.id.add_item:
                Log.e("abcd","active add");
                Intent intent = new Intent(this,Profile.class);
              //  intent.putExtra("content",-1);
                startActivityForResult(intent,REQUEST_CODE_NEW);
                break;
            default:
                 break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_NEW)
            {
                Contact newProfile = new Contact();

                //put data into new profile
                newProfile.setName(data.getStringExtra("returnName"));
                newProfile.setPhoneNumber(data.getStringExtra("returnPhone"));
                newProfile.setFbLink(data.getStringExtra("returnFb"));
                newProfile.setAddress(data.getStringExtra("returnAddress"));

                //get gender choice
                boolean gender = data.getBooleanExtra("returnGender",true);
                if (gender) newProfile.setImgResource(R.drawable.male);
                else newProfile.setImgResource(R.drawable.female);

                //add new profile to contact array list
                contactArrayList.add(newProfile);
            }
            else {  //REQUEST_CODE_EDIT
                //get the position of profile need to be edited
                int pos = data.getIntExtra("position",-1);

                //put new data to profile above
                contactArrayList.get(pos).setName(data.getStringExtra("returnName"));
                contactArrayList.get(pos).setPhoneNumber(data.getStringExtra("returnPhone"));
                contactArrayList.get(pos).setFbLink(data.getStringExtra("returnFb"));
                contactArrayList.get(pos).setAddress(data.getStringExtra("returnAddress"));

                //get gender choice
                boolean gender = data.getBooleanExtra("returnGender",true);
                if (gender) contactArrayList.get(pos).setImgResource(R.drawable.male);
                       else contactArrayList.get(pos).setImgResource(R.drawable.female);
            }
        updateContact();    //update after add or edit
        }
    }

    private void updateContact() {
        //call the system if any data changed or added
        contactAdapter.notifyDataSetChanged();

        //update new quantity of contact
        toolBar.setSubtitle("You have " + contactArrayList.size() + " contact(s)");
    }

    @Override
    protected void onDestroy() {
        saveArrayList(contactArrayList,SAVE_KEY);
        Log.e("abcd","saved");
        super.onDestroy();
    }

    private void saveArrayList(ArrayList<Contact> list, String save_key) {
        SharedPreferences  pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(save_key,json);
        editor.apply();
    }

    private ArrayList<Contact> getContactList(String save_key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Gson gson = new Gson();
        String json = pref.getString(save_key,null);
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        return gson.fromJson(json,type);
    }

    @Override
    protected void onPause() {
        saveArrayList(contactArrayList,SAVE_KEY);
        Log.e("abcd","pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveArrayList(contactArrayList,SAVE_KEY);
        Log.e("abcd","stop");
        super.onStop();
    }
}
