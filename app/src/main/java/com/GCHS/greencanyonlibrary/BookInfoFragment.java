package com.GCHS.greencanyonlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Book mParam1;

    private OnFragmentInteractionListener mListener;

    public BookInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookInfoFragment newInstance(Book selected) {
        BookInfoFragment fragment = new BookInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, selected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);
        if(mParam1 != null) {

            ImageView cover = (ImageView)rootView.findViewById(R.id.coverView2);
            //TextView title = (TextView)rootView.findViewById(R.id.titleView);
            TextView author = (TextView)rootView.findViewById(R.id.authorView);

            Picasso.with(this.getContext()).load(mParam1.getImageURL()).into(cover, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });

            //title.setText(mParam1.getTitle());


            mListener.onFragmentInteraction(mParam1.getTitle());
        }
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
        mListener.onFragmentInteraction(mParam1.getTitle());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String s);
    }
}
