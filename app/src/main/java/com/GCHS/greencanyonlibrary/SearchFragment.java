package com.GCHS.greencanyonlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

interface BookCallback{
    void asyncResult(String r);
}

public class SearchFragment extends Fragment implements BookCallback{

    private BooksSQLmanager lm;
    private View thisRootView;
    private Books books = null;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.onFragmentInteraction("Search");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        lm = new BooksSQLmanager(this);
        lm.getBooks(this.getContext());
        thisRootView = rootView;
        Button b = thisRootView.findViewById(R.id.searchButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResults();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentInteraction("Search");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void searchResults(){
        Spinner sp = thisRootView.findViewById(R.id.searchSpinner);
        int selection = sp.getSelectedItemPosition();
        EditText queryFeild = thisRootView.findViewById(R.id.searchQueryText);

        if(selection >= 0) {
            ArrayList<Book> searchedBooks = books.search(queryFeild.getText().toString(), selection);
            ListView lv = thisRootView.findViewById(R.id.bookView);
            lv.setAdapter(new BookAdapter(this.getContext(), searchedBooks));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Book selectedBook = (Book) parent.getItemAtPosition(position);

                    BookInfoFragment bif = new BookInfoFragment();
                    BookInfoFragment bif2 = BookInfoFragment.newInstance(selectedBook);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame, bif2).addToBackStack("fragBack");
                    ft.commit();
                    ((UserActivity) getActivity()).updateFragsSwitched();
                }
            });
        }else{
            Snackbar.make(thisRootView, "Please select what to search by.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void asyncResult(String r) {
        books = new Books(r);

        ArrayList<Book> allBooksArray = books.getBooksArrayList();

        ListView lv = thisRootView.findViewById(R.id.bookView);
        lv.setAdapter(new BookAdapter(this.getContext(), allBooksArray));lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Book selectedBook = (Book) parent.getItemAtPosition(position);
                BookInfoFragment bif2 = BookInfoFragment.newInstance(selectedBook);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, bif2).addToBackStack("fragBack");
                ft.commit();
                ((UserActivity)getActivity()).updateFragsSwitched();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }


}