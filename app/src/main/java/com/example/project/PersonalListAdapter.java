package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonalListAdapter extends ArrayAdapter<UserProfile> {

    private  static final String TAG = "PersonalListAdapter";
    private Context mContext;
    int mResource;

    public PersonalListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserProfile> objects) {
        super(context, resource, objects);
       mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String Name = getItem(position).getUserName();
        String BloodGroup = getItem(position).getUserBloodGroup();
        String Phone = getItem(position).getUserPhone();
        String Address = getItem(position).getUserAddress();
        String Password = getItem(position).getUserPassword();

        UserProfile userProfile = new UserProfile(Name,Password,Phone,BloodGroup,Address);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = (TextView) convertView.findViewById(R.id.tv1);
        TextView tvBlood = (TextView) convertView.findViewById(R.id.tv2);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv3);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tv4);

        tvName.setText("Name : "+Name);
        tvBlood.setText("BloodGroup : "+BloodGroup);
        tvPhone.setText("Phone : "+Phone);
        tvAddress.setText("Address : "+Address);

        return convertView;
    }
}
