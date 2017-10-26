package com.mathreya.majorminorscales;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mithileshathreya on 10/26/17.
 */

public class ScaleAdapter extends RecyclerView.Adapter<ScaleAdapter.MyViewHolder> {
    private Context context;
    private List<Scale> scaleList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, formula;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.scale_name);
            formula = (TextView) view.findViewById(R.id.scale_formula);
        }
    }

    public ScaleAdapter(Context context, List<Scale> scaleList) {
        this.context = context;
        this.scaleList = scaleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scale_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Scale scale = scaleList.get(position);
        holder.name.setText(scale.getName());
        holder.formula.setText(scale.getFormula());
    }

    @Override
    public int getItemCount() {
        return scaleList.size();
    }
}
