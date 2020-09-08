package com.example.lap.sharingstudy.Upload;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lap.sharingstudy.Book.BookDetails;
import com.example.lap.sharingstudy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ShareBook extends AppCompatActivity {
    private String branch = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_book_details);
        final Spinner spinner = findViewById(R.id.branch_upload_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final EditText bookNameET = findViewById(R.id.book_name);
        final EditText quantityET = findViewById(R.id.quantity);
        final EditText ownerNameET =  findViewById(R.id.owner_name);
        final EditText hostelNameET = findViewById(R.id.hostel_name);
        final EditText roomNoET =  findViewById(R.id.room_number);
        final EditText contactNumberET = findViewById(R.id.contact_number);
        Button submitBook =  findViewById(R.id.bookDetails_submit);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBook.setOnClickListener(v -> {
            String bookName = bookNameET.getText().toString();
            String quantity = quantityET.getText().toString();
            String owner = ownerNameET.getText().toString();
            String hostelName = hostelNameET.getText().toString();
            String roomNo = roomNoET.getText().toString();
            String contactNo = contactNumberET.getText().toString();

            if (bookName.isEmpty() || quantity.isEmpty() || owner.isEmpty() ||
                    hostelName.isEmpty() || roomNo.isEmpty()) {
                Toast.makeText(ShareBook.this, R.string.toast_empty_fields, Toast.LENGTH_SHORT).show();
            } else {
                BookDetails b = new BookDetails(bookName, quantity, owner, hostelName, roomNo, contactNo, branch);
                myRef.child("books").push().setValue(b);
                Toast.makeText(ShareBook.this, R.string.toast_submitted, Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

}
