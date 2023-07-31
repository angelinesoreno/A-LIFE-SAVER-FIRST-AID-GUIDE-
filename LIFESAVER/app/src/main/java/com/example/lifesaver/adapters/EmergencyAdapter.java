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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lifesaver.DetailsActivity;
import com.example.lifesaver.R;
import com.example.lifesaver.models.ContactsModel;
import com.example.lifesaver.models.EmergencyModel;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private Context context;
    private List<EmergencyModel> list;

    public EmergencyAdapter(Context context, List<EmergencyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyModel contact = list.get(position);

        Glide.with(context).load(list.get(position).getSakitImage()).into(holder.sakitImage);
        holder.sakitName.setText(contact.getSakitName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("detailed", list.get(position));
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sakitImage;
        TextView sakitName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sakitName = itemView.findViewById(R.id.sakitName);
            sakitImage = itemView.findViewById(R.id.sakitImage);

        }
    }
}
