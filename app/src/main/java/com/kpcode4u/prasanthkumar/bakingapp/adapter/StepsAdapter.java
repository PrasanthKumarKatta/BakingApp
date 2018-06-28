package com.kpcode4u.prasanthkumar.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.List;

/**
 * Created by Prasanth kumar on 25/06/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsInfo> {

    private Context context;
    private List<Steps> stepsList;

    public StepsAdapter(Context context, List<Steps> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
    }

    @Override
    public StepsInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StepsInfo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StepsInfo extends RecyclerView.ViewHolder {

        public StepsInfo(View itemView) {
            super(itemView);

        }
    }
}
