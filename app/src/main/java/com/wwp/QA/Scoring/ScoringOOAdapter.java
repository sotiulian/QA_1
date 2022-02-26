package com.wwp.QA.Scoring;

import android.app.Activity;
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

public class ScoringOOAdapter {

    Context context;
    ViewGroup parent;
    ScoringOOViewHolder scoringOOViewHolder;
    ArrayList<ScoringArticles> articles;


    public ScoringOOAdapter(Context context, ViewGroup parent, ArrayList<ScoringArticles> articles) {
        Log.e("SA","ScoringOOAdapter.constructor -> " + articles.size());
        this.context = context;
        this.parent = parent; // findViewById(R.id.vscoringoooperator)
        this.articles = articles;

        this.scoringOOViewHolder = this.createView();
    }

    // mime the method name from RecyclerView
    public void notifyDataSetChanged(){
        // use model getters and setters to set the views into news_item.xml layout
        // integer used with String.valueOf(
        Log.e("SA","ScoringOOAdapter.getActualpieces -> " + articles.get(0).getActualpieces());
        Log.e("SA","ScoringOOAdapter.getActualdefectsfoundpieces -> " + articles.get(0).getActualdefectsfoundpieces());
        Log.e("SA","ScoringOOAdapter.getActualprocent -> " + articles.get(0).getActualprocent());
        Log.e("SA","ScoringOOAdapter.getColor -> " + articles.get(0).getColor());

        scoringOOViewHolder.tvoototalpieces.setText(String.valueOf(articles.get(0).getActualpieces()));
        scoringOOViewHolder.tvoodeffectpieces.setText(String.valueOf(articles.get(0).getActualdefectsfoundpieces()));
        scoringOOViewHolder.tvooprocent.setText(String.format(Locale.US,"%d %%",articles.get(0).getActualprocent()));


        // **** here these two choices must be chose in the future from settings
        //
        // choice1: now we set the parent backcolor
        // ----------------------------------------------------
        scoringOOViewHolder.view.setBackgroundColor(Utils.parseColor(articles.get(0).getColor()));
        //
        // choice2: now we set the activity main view backcolor
        // ----------------------------------------------------
        //View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        //rootView.setBackgroundColor(Utils.parseColor(articles.get(0).getColor()));
        //
        // ****
    }

    @NonNull
    public ScoringOOViewHolder createView() {

        Log.e("SA","ScoringOOAdapter.onCreate -> ");

        /* -----*/
        View view = LayoutInflater.from(context).inflate(R.layout.scoringoperatoroperation_item, this.parent);
        /* -----*/

        return new ScoringOOViewHolder(view);
    }


    public class ScoringOOViewHolder {

        View view;

        TextView tvoototalpieces;
        TextView tvoodeffectpieces;
        TextView tvooprocent;

        public ScoringOOViewHolder(View view) {

            // holds together all the views from news_item.xml
            this.view = view;

            tvoototalpieces = view.findViewById(R.id.tvoototalpieces);
            tvoodeffectpieces = view.findViewById(R.id.tvoodeffectpieces);
            tvooprocent = view.findViewById(R.id.tvooprocent);
        }
    }
}
