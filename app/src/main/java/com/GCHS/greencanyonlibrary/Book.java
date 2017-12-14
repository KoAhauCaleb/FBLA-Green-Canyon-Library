package com.GCHS.greencanyonlibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Caleb on 12/7/17.
 */

public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String imageURL;
    private String title;
    private String author;
    private boolean checkedOut;

    public Book(String iURL, String t, String a, int co)
    {
        imageURL = iURL;
        title = t;
        author = a;
        checkedOut = co == 1;
    }

    protected Book(Parcel in) {
        imageURL = in.readString();
        title = in.readString();
        author = in.readString();
        checkedOut = in.readByte() != 0;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageURL);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeByte((byte) (checkedOut ? 1 : 0));
    }
}
