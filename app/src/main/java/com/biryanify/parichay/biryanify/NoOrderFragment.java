package com.biryanify.parichay.biryanify;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoOrderFragment extends Fragment{

    public NoOrderFragment() {
    }

    public static NoOrderFragment newInstance() {
        NoOrderFragment noOrderFragment = new NoOrderFragment();
        return noOrderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.noorder_fragment, container, false);
        return view;
    }

}
