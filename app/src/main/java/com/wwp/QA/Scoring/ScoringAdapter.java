package com.wwp.QA.Scoring;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wwp.QA.R;
import com.wwp.QA.Utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class ScoringAdapter {

    Context context;
    ViewGroup parent;
    ScoringViewHolder scoringViewHolder;
    ArrayList<ScoringArticles> articles;


    public ScoringAdapter(Context context, ViewGroup parent, ArrayList<ScoringArticles> articles) {
        Log.e("SA","ScoringAdapter.constructor -> " + articles.size());
        this.context = context;
        this.parent = parent; // findViewById(R.id.vscoringoperator)
        this.articles = articles;

        this.scoringViewHolder = this.createView();
    }

    // mime the method name from RecyclerView
    public void notifyDataSetChanged(){
        // use model getters and setters to set the views into news_item.xml layout
        // integer used with String.valueOf(
        Log.e("SA","ScoringAdapter.getActualpieces -> " + articles.get(1).getActualpieces());
        Log.e("SA","ScoringAdapter.getActualdefectsfoundpieces -> " + articles.get(1).getActualdefectsfoundpieces());
        Log.e("SA","ScoringAdapter.getActualprocent -> " + articles.get(1).getActualprocent());
        Log.e("SA","ScoringAdapter.getColor -> " + articles.get(1).getColor());

        scoringViewHolder.tvtotalpieces.setText(String.valueOf(articles.get(1).getActualpieces()));
        scoringViewHolder.tvdeffectpieces.setText(String.valueOf(articles.get(1).getActualdefectsfoundpieces()));
        scoringViewHolder.tvprocent.setText(String.format(Locale.US,"%d %%",articles.get(1).getActualprocent()));


        // now we set the parent backcolor
        scoringViewHolder.view.setBackgroundColor(Utils.parseColor(articles.get(1).getColor()));

    }

    @NonNull
    public ScoringViewHolder createView() {

        Log.e("SA","ScoringAdapter.onCreate -> ");

        /* -----*/
        View view = LayoutInflater.from(context).inflate(R.layout.scoringoperator_item, this.parent); // who, where
        /* -----*/

        return new ScoringViewHolder(view);
    }


    public class ScoringViewHolder {

        View view;

        TextView tvtotalpieces;
        TextView tvdeffectpieces;
        TextView tvprocent;

        public ScoringViewHolder(View view) {

            // holds together all the views from news_item.xml
            this.view = view;

            tvtotalpieces = view.findViewById(R.id.tvtotalpieces);
            tvdeffectpieces = view.findViewById(R.id.tvdeffectpieces);
            tvprocent = view.findViewById(R.id.tvprocent);
        }
    }
}
