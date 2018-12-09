package com.example.jung_jaejin.myproject.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jung_jaejin.myproject.R;


public class Fragment_two extends Fragment {


    public Fragment_two() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate){
        return inflater.inflate(R.layout.fragment_fragment_two,container, false);
    }

}
