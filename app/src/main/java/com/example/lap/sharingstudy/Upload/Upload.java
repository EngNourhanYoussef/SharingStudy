package com.example.lap.sharingstudy.Upload;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lap.sharingstudy.FileDetails;
import com.example.lap.sharingstudy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URISyntaxException;

public class Upload extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private String branch = "Computer Science";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        Button b = findViewById(R.id.button2);

        //spinner
        final Spinner spinner = findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        b.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, FILE_SELECT_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {

                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);

                RelativeLayout r = findViewById(R.id.upload_content);
                r.setVisibility(View.GONE);

                // Get the Uri of the selected file
                Uri uri = data.getData();
                String path = null;
                try {
                    assert uri != null;
                    path = getPath(this, uri);
                } catch (URISyntaxException e) {
                    System.out.print(0);
                }
                System.out.println(path);

                assert path != null;
                File f = new File(path);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReferenceFromUrl("gs://bonding-bad75.appspot.com");

                Uri file = Uri.fromFile(f);
                EditText et =  findViewById(R.id.upload_filename);

                if (et.getText().toString().isEmpty()) {
                    Toast.makeText(Upload.this, R.string.toast_filename_req, Toast.LENGTH_SHORT).show();
                    loadingIndicator.setVisibility(View.GONE);
                    r.setVisibility(View.VISIBLE);
                } else {
                    StorageReference ref = storageRef.child(et.getText().toString());
                    UploadTask uploadTask = ref.putFile(file);

                    // Register observers to listen for when the logo is done or if it fails
                    uploadTask.addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                    }).addOnSuccessListener(taskSnapshot -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();
                        EditText et1 = findViewById(R.id.upload_filename);
                        StorageMetadata metadata = taskSnapshot.getMetadata();
                        FileDetails f1 = new FileDetails(et1.getText().toString(), branch);

                        myRef.child("files").push().setValue(f1);
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and logo URL.

                        StorageReference taskSnapshotStorage = taskSnapshot.getStorage();
                        Uri downloadUrl = taskSnapshotStorage.getDownloadUrl().getResult();
                        findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);

                        RelativeLayout r1 = findViewById(R.id.upload_content);
                        r1.setVisibility(View.VISIBLE);
                        Toast.makeText(Upload.this, R.string.toast_successfully_upload, Toast.LENGTH_LONG).show();
                    });
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
