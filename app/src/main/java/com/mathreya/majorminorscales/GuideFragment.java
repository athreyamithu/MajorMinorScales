package com.mathreya.majorminorscales;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mithileshathreya on 10/24/17.
 */

public class GuideFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
