package com.wwp.QA.ProductionOperator;

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

public class ProductionoperatorAdapter extends RecyclerView.Adapter<ProductionoperatorAdapter.ProductionoperatorViewHolder> {

    Context context;
    ArrayList<ProductionoperatorArticles> articles;

    public ProductionoperatorAdapter(Context context, ArrayList<ProductionoperatorArticles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ProductionoperatorAdapter.ProductionoperatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.lineoperator_item, parent, false);

        return new ProductionoperatorAdapter.ProductionoperatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionoperatorAdapter.ProductionoperatorViewHolder holder, int position) {

        Log.e("POA", "position -> " + position);
        Log.e("POA", "articles.size() -> " + articles.size());
        Log.e("POA", "articles.get(position).getOperatorid() -> " + articles.get(position).getOperatorid());
        Log.e("POA", "holder is null -> " + (holder == null ? "null": ""));
        Log.e("POA", "holder.tvoperatorid is null -> " + (holder.tvoperatorid == null ? "null": ""));
        Log.e("POA", "holder.tvoperatorid.getText() -> " + holder.tvoperatorid.getText());

        // use model getters and setters to set the views into news_item.xml layout
        holder.tvoperatorid.setText(articles.get(position).getOperatorid().toString());
        holder.tvoperatorname.setText(articles.get(position).getOperatorname().toString());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class ProductionoperatorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvoperatorid;
        TextView tvoperatorname;

        public ProductionoperatorViewHolder(@NonNull View itemView) {

            super(itemView);

            // holds together all the views from news_item.xml
            tvoperatorid = itemView.findViewById(R.id.tvoperatorid);
            tvoperatorname = itemView.findViewById(R.id.tvoperatorname);

            itemView.setOnClickListener(this);

        }

        // to have onClick method here, the class must implements View.OnClickListener
        @Override
        public void onClick(View view) {

            ProductionoperatorArticles article = articles.get(getAdapterPosition());

            Log.e("PA","You clicked on -> " + article.getOperatorname());

            ((ProductionoperatorlistActivity) context).setResult(
                    Activity.RESULT_OK
                    , new Intent().putExtra("article", article)
            );

            ((ProductionoperatorlistActivity) context).finish();

        }
    }
}
