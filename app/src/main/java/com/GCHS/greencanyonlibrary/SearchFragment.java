package com.GCHS.greencanyonlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements BookCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BooksSQLmanager lm;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mListener.onFragmentInteraction("Search");
    }

    private View thisRootView;
    private ArrayList<Book> allBooksArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        lm = new BooksSQLmanager(this);
        lm.getBooks(this.getContext());
        thisRootView = rootView;
        Button b = (Button)thisRootView.findViewById(R.id.searchButton);
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

    Books books = null;

    public void searchResults(){
        Spinner sp = (Spinner)thisRootView.findViewById(R.id.searchSpinner);
        int selection = sp.getSelectedItemPosition();
        EditText queryFeild = (EditText)thisRootView.findViewById(R.id.searchQueryText);

        if(selection >= 0) {
            ArrayList<Book> searchedBooks = books.search(queryFeild.getText().toString(), selection);
            ListView lv = (ListView) thisRootView.findViewById(R.id.bookView);
            lv.setAdapter(new BookAdapter(this.getContext(), searchedBooks));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Book selectedBook = (Book) parent.getItemAtPosition(position);

                    BookInfoFragment bif = new BookInfoFragment();
                    BookInfoFragment bif2 = bif.newInstance(selectedBook);

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

        allBooksArray = books.getBooksArrayList();

        ListView lv = (ListView) thisRootView.findViewById(R.id.bookView);
        lv.setAdapter(new BookAdapter(this.getContext(), allBooksArray));lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Book selectedBook = (Book) parent.getItemAtPosition(position);

                BookInfoFragment bif = new BookInfoFragment();
                BookInfoFragment bif2 = bif.newInstance(selectedBook);

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

interface BookCallback{
    void asyncResult(String r);
}