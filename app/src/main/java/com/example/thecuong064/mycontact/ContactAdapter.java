package com.example.thecuong064.mycontact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by thecuong064 on 2/27/2018.
 */

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> arrayContactList = new ArrayList<>();
    public ContactAdapter(Context context, ArrayList<Contact> arr) {
        this.context = context;
        this.arrayContactList = arr;
    }

    @Override
    public int getCount() {
        if (arrayContactList != null)
            return arrayContactList.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return arrayContactList.get(i);
    }

    @Override
    public long getItemId(int i) {return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            listItemView = inflater.inflate(R.layout.item_layout,parent,false);
        }

        //find View of the layout view
        ImageView img = listItemView.findViewById(R.id.avatar);
        TextView name = listItemView.findViewById(R.id.fullname);
        TextView phoneNumber = listItemView.findViewById(R.id.phone);
        final ImageView callBtn = listItemView.findViewById(R.id.call);
        final ImageView mesBtn = listItemView.findViewById(R.id.mes);


        //get item of the Contact List and set the content
        Contact model = arrayContactList.get(position);
        img.setImageResource(model.getImgResource());
        name.setText(model.getName());
        phoneNumber.setText(model.getPhoneNumber());

        //over ride to call when click call button
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = arrayContactList.get(position).getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                context.startActivity(intent);
            }
        });

        //override to send message
        mesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = arrayContactList.get(position).getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+number));
                context.startActivity(intent);
            }
        });
        return listItemView;
    }
}
