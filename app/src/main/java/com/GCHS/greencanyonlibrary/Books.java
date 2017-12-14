package com.GCHS.greencanyonlibrary;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Caleb on 12/7/17.
 */

public class Books {

    private ArrayList<Book> booksArrayList = new ArrayList<Book>();

    public Books (String booksJSON) {
        JsonObject bookJ;
        try {
            bookJ = new Gson().fromJson(booksJSON, JsonObject.class);

            JsonArray booksArray = bookJ.getAsJsonArray("books");
            int amtBooks = booksArray.size();
            for (int i = 0; i < amtBooks; i++){
                String iURL = booksArray.get(i).getAsJsonObject().get("imageurl").getAsString();
                String title = booksArray.get(i).getAsJsonObject().get("title").getAsString();
                int checkedout = booksArray.get(i).getAsJsonObject().get("checkedout").getAsInt();
                String author = booksArray.get(i).getAsJsonObject().get("author").getAsString();
                booksArrayList.add(new Book(iURL, title, author, checkedout));
                Log.e(booksArrayList.get(i).getTitle(), "Books");
            }
        }catch(Exception e) {
            Log.e(e.getMessage(), "Books");
        }
    }

    public ArrayList<Book> getBooksArrayList () {
        return booksArrayList;
    }

    public ArrayList<Book> search(String searchquery, int typeofsearch) {
        ArrayList<Book> searchResults = new ArrayList<Book>();

        for (int i = 0; i < booksArrayList.size(); i++) {
            Book current = booksArrayList.get(i);
            String author = current.getAuthor();
            String title = current.getTitle();

            if (typeofsearch == 1) { //Author Search
                if (author.toLowerCase().contains(searchquery.toLowerCase()))
                    searchResults.add(current);
            }else { //Title Search
                if (title.toLowerCase().contains(searchquery.toLowerCase()))
                    searchResults.add(current);
            }
        }

        return searchResults;
    }
}
