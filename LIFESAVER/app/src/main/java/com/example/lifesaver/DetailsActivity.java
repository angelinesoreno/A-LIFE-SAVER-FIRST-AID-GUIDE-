package com.example.lifesaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lifesaver.models.EmergencyModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {

    ImageView sakitImageDetails;
    TextView sakitNameDetails, sakitDescription, remedies;
    Button link;


    EmergencyModel emergencyModel = null;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        firestore = FirebaseFirestore.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof EmergencyModel){
            emergencyModel = (EmergencyModel) obj;
        }


        sakitImageDetails = findViewById(R.id.sakitImageDetails);
        sakitNameDetails = findViewById(R.id.sakitNameDetails);
        sakitDescription = findViewById(R.id.sakitDescription);
        remedies = findViewById(R.id.remedies);


        if (emergencyModel != null){
            Glide.with(getApplicationContext()).load(emergencyModel.getSakitImage()).into(sakitImageDetails);
            sakitNameDetails.setText(emergencyModel.getSakitName());
            sakitDescription.setText(emergencyModel.getSakitDescription());
            remedies.setText(emergencyModel.getRemedies());
        }

        link = findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emergencyModel != null) {
                    String url = emergencyModel.getLink();
                    if (url != null && !url.isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } else {
                        Toast.makeText(DetailsActivity.this, "Website link not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}