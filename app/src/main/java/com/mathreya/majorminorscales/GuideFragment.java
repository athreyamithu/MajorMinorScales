package com.mathreya.majorminorscales;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mithileshathreya on 10/24/17.
 */

public class GuideFragment extends Fragment {
    private RecyclerView recyclerView;
    private ScaleAdapter adapter;
    private List<Scale> scaleList;
    private String json;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.scale_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        scaleList = new ArrayList<>();
        adapter = new ScaleAdapter(getActivity(), scaleList);
        recyclerView.setAdapter(adapter);
        initList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initList() {
        try {
            readJSONStream();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("formulas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                Scale scale = new Scale(row.getString("name"), row.getString("formula"));
                scaleList.add(scale);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readJSONStream() {
        try {
            InputStream is = getActivity().getAssets().open("scaleformulas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
