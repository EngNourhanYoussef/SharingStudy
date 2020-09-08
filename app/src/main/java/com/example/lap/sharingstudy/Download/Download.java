package com.example.lap.sharingstudy.Download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lap.sharingstudy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class Download extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;
    private DownloadAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int DOWNLOAD_LOADER_ID = 1;
    private ArrayList<DownloadURLs> downloadURLses = new ArrayList<>();
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.download_swipe_refresh_layout);
        l = (ListView) findViewById(R.id.files_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.download_empty_view);
        l.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connMgr.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(Network network) {FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference();
                        //ArrayList<DownloadURLs> downloadURLses = new ArrayList<>();
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    if (postSnapshot.getKey().equals("files")) {

                                        for (DataSnapshot childSnap : postSnapshot.getChildren()) {

                                            //System.out.println(child);
                                            String name = (String) childSnap.child("fileName").getValue();
                                            String branch = (String) childSnap.child("fileBranch").getValue();
                                            //String size = (String) childSnap.child("size").getValue();
                                            DownloadURLs d = new DownloadURLs(name, branch);
                                            downloadURLses.add(d);
                                        }
                                        Collections.reverse(downloadURLses);
                                        mAdapter = new DownloadAdapter(Download.this, downloadURLses);
                                        l.setAdapter(mAdapter);

                                    }


                                }

                                //System.out.println(downloadURLses.size());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message

                                // ...
                            }
                        });

                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                //refreshContent();
                                mAdapter.clear();

                                //reload data
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                            if (postSnapshot.getKey().equals("files")) {

                                                for (DataSnapshot childSnap : postSnapshot.getChildren()) {

                                                    //System.out.println(child);
                                                    String name = (String) childSnap.child("fileName").getValue();
                                                    String branch = (String) childSnap.child("fileBranch").getValue();
                                                    DownloadURLs d = new DownloadURLs(name, branch);
                                                    downloadURLses.add(d);

                                                }
                                                Collections.reverse(downloadURLses);
                                                mAdapter = new DownloadAdapter(Download.this, downloadURLses);
                                                l.setAdapter(mAdapter);

                                            }


                                        }

                                        //System.out.println(downloadURLses.size());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message

                                        // ...
                                    }
                                });

                                //
                                mSwipeRefreshLayout.setRefreshing(false);

                            }

                        });

                        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(Download.this, R.string.toast_downloading, Toast.LENGTH_LONG).show();
                                DownloadURLs d = mAdapter.getItem(position);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                // Create a storage reference from our app
                                StorageReference storageRef = storage.getReferenceFromUrl("gs://bonding-bad75.appspot.com");
                                StorageReference pathReference = storageRef.child(d.getUrl());
                                File localFile = null;
                                try {
                                    //TextView t = (TextView) findViewById(R.id.file_title);
                                    localFile =new File (getExternalFilesDir(DIRECTORY_DOWNLOADS), d.getUrl());
                                } catch (Exception e) {
                                    System.out.print(0);
                                }
                                pathReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                                    // Local temp file has been created

                                    Toast.makeText(Download.this, R.string.toast_downloaded,
                                            Toast.LENGTH_SHORT).show();

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }
                        });

                    }
                    @Override
                    public void onLost(Network network) {

                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }});


    }

}
