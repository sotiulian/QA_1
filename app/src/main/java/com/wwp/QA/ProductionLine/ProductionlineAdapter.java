package com.wwp.QA.ProductionLine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wwp.QA.R;

import java.util.ArrayList;

public class ProductionlineAdapter  extends RecyclerView.Adapter<ProductionlineAdapter.ProductionlineViewHolder> {

    Context context;
    ArrayList<ProductionlineArticles> articles;

    public ProductionlineAdapter(Context context, ArrayList<ProductionlineArticles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ProductionlineAdapter.ProductionlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.linename_item, parent, false);
        return new ProductionlineAdapter.ProductionlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionlineAdapter.ProductionlineViewHolder holder, int position) {

        // use model getters and setters to set the views into news_item.xml layout
        holder.tvAreacode.setText(articles.get(position).getAreacode().toString());
        holder.tvAreaname.setText(articles.get(position).getAreaname().toString());
        holder.tvLinename.setText(articles.get(position).getLinename().toString());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class ProductionlineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvAreacode;
        TextView tvAreaname;
        TextView tvLinename;

        public ProductionlineViewHolder(@NonNull View itemView) {

            super(itemView);

            // holds together all the views from news_item.xml
            tvAreacode = itemView.findViewById(R.id.tvAreacode);
            tvAreaname = itemView.findViewById(R.id.tvAreaname);
            tvLinename = itemView.findViewById(R.id.tvLinename);

            itemView.setOnClickListener(this);

        }

        // to have onClick method here, the class must implements View.OnClickListener
        @Override
        public void onClick(View view) {

            ProductionlineArticles article = articles.get(getAdapterPosition());
            Log.e("PA","You clicked on -> " + article.getLinename());

            ((ProductionlinelistActivity) context).setResult( Activity.RESULT_OK
                                                            , new Intent().putExtra("article", article));
            ((ProductionlinelistActivity) context).finish();

        }
    }
}
