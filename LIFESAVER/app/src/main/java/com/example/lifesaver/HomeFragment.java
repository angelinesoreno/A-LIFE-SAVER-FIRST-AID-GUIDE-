package com.example.lifesaver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifesaver.adapters.ContactsAdapter;
import com.example.lifesaver.adapters.EmergencyAdapter;
import com.example.lifesaver.models.ContactsModel;
import com.example.lifesaver.models.EmergencyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView downloadButton;
    RecyclerView contactRecyclerView, emergencyRecycleView;
    // Contacts Recycler View
    ContactsAdapter contactsAdapter;
    List<ContactsModel> contactsModelList;
    // Sakit Recycle View
    EmergencyAdapter emergencyAdapter;
    List<EmergencyModel> emergencyModelList;
    // FireStore
    FirebaseFirestore db;

    private static final int PERMISSION_REQUEST_CODE = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        downloadButton = root.findViewById(R.id.downloadButton);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new DownloadPdfTask().execute();
                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        });

        emergencyRecycleView = root.findViewById(R.id.rec_emergency);
        contactRecyclerView = root.findViewById(R.id.rec_contact);
        db = FirebaseFirestore.getInstance();

        contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        contactsModelList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getContext(), contactsModelList);
        contactRecyclerView.setAdapter(contactsAdapter);

        db.collection("EmergencyContacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContactsModel contactsModel = document.toObject(ContactsModel.class);
                                contactsModelList.add(contactsModel);
                                contactsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        emergencyRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        emergencyModelList = new ArrayList<>();
        emergencyAdapter = new EmergencyAdapter(getContext(), emergencyModelList);
        emergencyRecycleView.setAdapter(emergencyAdapter);

        db.collection("Sakit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EmergencyModel emergencyModel = document.toObject(EmergencyModel.class);
                                emergencyModelList.add(emergencyModel);
                                emergencyAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        return root;
    }

    private class DownloadPdfTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String pdfFileName = "Emergency-Contacts.pdf";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("pdf").child(pdfFileName);

            File file = new File(directory, pdfFileName);

            try {
                StreamDownloadTask.TaskSnapshot taskSnapshot = Tasks.await(storageRef.getStream());
                InputStream inputStream = taskSnapshot.getStream();
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                // After successfully downloading the PDF, notify the MediaStore about the new file
                scanFileToMediaStore(file);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                Toast.makeText(requireContext(), "Download successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error downloading", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scanFileToMediaStore(File file) {
        MediaScannerConnection.scanFile(
                requireContext(),
                new String[]{file.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // File has been scanned and added to the MediaStore
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new DownloadPdfTask().execute();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot download file.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
