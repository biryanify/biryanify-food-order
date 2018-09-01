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
        return new NoOrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noorder, container, false);
        return view;
    }

}
