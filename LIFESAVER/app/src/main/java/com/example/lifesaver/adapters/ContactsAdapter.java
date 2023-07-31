package com.example.lifesaver.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifesaver.R;
import com.example.lifesaver.models.ContactsModel;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private static final int REQUEST_CALL_PHONE = 1;

    private Context context;
    private List<ContactsModel> list;
    private ContactsModel contactsModel;

    public ContactsAdapter(Context context, List<ContactsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactsModel contact = list.get(position);

        holder.locationName.setText(contact.getLocationName());
        holder.address.setText(contact.getAddress());
        holder.hotlineNumber.setText(contact.getHotlineNumber());

        holder.dialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(contact.getHotlineNumber());
            }
        });

        holder.checkLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(contact.getLocationLatitude(), contact.getLocationLongitude());
            }
        });
    }

    private void openMap(String latitude, String longitude) {
        String location = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
        context.startActivity(intent);
    }

    private void makePhoneCall(String phoneNumber) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        } else {

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall(contactsModel.getHotlineNumber());
            } else {

                Toast.makeText(context, "Permission denied to make a phone call", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView hotlineNumber;
        TextView address;
        Button dialButton, checkLocationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            hotlineNumber = itemView.findViewById(R.id.hotlineNumber);
            address = itemView.findViewById(R.id.address);
            dialButton = itemView.findViewById(R.id.dialButton);
            checkLocationButton = itemView.findViewById(R.id.checkLocationButton);
        }
    }
}
