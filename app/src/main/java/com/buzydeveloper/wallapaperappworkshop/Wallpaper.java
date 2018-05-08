package com.buzydeveloper.wallapaperappworkshop;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Wallpaper extends AppCompatActivity {
    public DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Images");

        imageView = (ImageView)findViewById(R.id.image);

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);


        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ImageDetails,Wallpaper.ImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageDetails, Wallpaper.ImageViewHolder>(
                ImageDetails.class,
                R.layout.row,
                Wallpaper.ImageViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(Wallpaper.ImageViewHolder viewHolder, final ImageDetails model, int position) {

                viewHolder.setImage(getApplicationContext(),model.getUrl());

            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public LinearLayout linearLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);
        }

        public void setImage (Context ctx, String Url){
            ImageView post_image = mView.findViewById(R.id.image);
            Picasso.with(ctx).load(Url).into(post_image);
        }


    }
}
