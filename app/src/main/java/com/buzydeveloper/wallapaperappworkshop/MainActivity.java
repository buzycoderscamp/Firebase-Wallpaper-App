package com.buzydeveloper.wallapaperappworkshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Button upload, show;


    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upload = findViewById(R.id.upload);
        show= findViewById(R.id.show);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_CODE);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Wallpaper.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();//copied

            StorageReference filePath = mStorage.child("wallpaperView").child(uri.getLastPathSegment());//navigated
            //pasting
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Adding image to the database
                    String stringUrl = downloadUrl.toString();
                    addImageToDatabase(stringUrl );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Image not Selected",Toast.LENGTH_SHORT).show();
    }

    private void addImageToDatabase(String stringUrl) {
        ImageDetails imageDetails = new ImageDetails(stringUrl);
        mDatabase.push().setValue(imageDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Added to Database", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed adding in Database", Toast.LENGTH_SHORT).show();
                System.out.println(e);
            }
        });
    }
}
