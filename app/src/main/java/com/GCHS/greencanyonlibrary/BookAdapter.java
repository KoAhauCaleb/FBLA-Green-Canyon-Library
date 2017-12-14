package com.GCHS.greencanyonlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class BookAdapter extends ArrayAdapter<Book>{
    Context context;
    ArrayList<Book> objects;

    public BookAdapter(Context c, ArrayList<Book> o){
        super(c, 0, o);
        context = c;
        objects = o;
    }

    public View getView(int position, View view, ViewGroup parent){
        final BookCell bookCell = new BookCell();

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.book_item, parent,false);

        bookCell.cover = view.findViewById(R.id.coverView);
        bookCell.title = view.findViewById(R.id.titleView);
        bookCell.loading = view.findViewById(R.id.imageLoadingBar);
        bookCell.author = view.findViewById(R.id.authorView);
        bookCell.checkedOut = view.findViewById(R.id.checkedOutView);

        Book b = getItem(position);

        final Callback mImageCallback = new Callback() {
            @Override
            public void onSuccess() {
                bookCell.loading.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
            }
        };

        Picasso.with(context).load(b.getImageURL()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                float aspectRatio = source.getWidth() /
                        (float) source.getHeight();
                int width = 180;
                int height = Math.round(width / aspectRatio);

                Bitmap bm = Bitmap.createScaledBitmap(
                        source, width, height, false);

                source.recycle();

                return Bitmap.createBitmap(bm, 0,0,180, 270);
            }

            @Override
            public String key() {
                return "retainAspectRatioResizedCropped";
            }
        }).into(bookCell.cover, mImageCallback);
        bookCell.title.setText(b.getTitle());
        bookCell.author.setText(b.getAuthor());
        if(b.isCheckedOut() == true) {
            Bitmap bmp = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.cheched_out_banner)).getBitmap();
            bookCell.checkedOut.setImageBitmap(bmp);
        }
        return view;
    }

    private static class BookCell{
        TextView title;
        TextView author;
        ImageView cover;
        ImageView checkedOut;
        ProgressBar loading;
    }
}


