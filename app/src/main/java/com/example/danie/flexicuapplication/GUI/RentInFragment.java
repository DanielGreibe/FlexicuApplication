package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;
public class RentInFragment extends Fragment {
    ImageView filter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rent_in, container, false);
        filter = v.findViewById(R.id.filterMenu);
        filter.setOnClickListener((View)->{
            Intent filtermenu = new Intent(getContext(), FiltersRentIn.class);
            Bundle bndlanimation =
                    ActivityOptions.makeCustomAnimation(getContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
            startActivity(filtermenu, bndlanimation);
        });






        return v;
    }
}
