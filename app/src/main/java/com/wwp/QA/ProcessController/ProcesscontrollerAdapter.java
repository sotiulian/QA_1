package com.wwp.QA.ProcessController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wwp.QA.R;

import java.util.ArrayList;
import java.util.Locale;

public class ProcesscontrollerAdapter extends RecyclerView.Adapter<ProcesscontrollerAdapter.ProcesscontrollerViewHolder> {

    Context context;
    ArrayList<ProcesscontrollerArticles> articles;
    ProcesscontrollerClickOnArticle processcontrollerClickOnArticle;

    public ProcesscontrollerAdapter(Context context, ArrayList<ProcesscontrollerArticles> articles) {
        this.context = context;
        this.articles = articles;
        this.processcontrollerClickOnArticle = (ProcesscontrollerClickOnArticle)context;
    }

    @NonNull
    @Override
    public ProcesscontrollerAdapter.ProcesscontrollerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.processcontroller_item, parent, false);
        return new  ProcesscontrollerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcesscontrollerAdapter.ProcesscontrollerViewHolder holder, int position) {

        // use model getters and setters to set the views into news_item.xml layout
        holder.tvpmsoperatorname.setText(articles.get(position).getPmsoperatorname());
        holder.tvtimestampcreate.setText(articles.get(position).getTimestampcreate());
        holder.tvstylename.setText(articles.get(position).getStylename());
        holder.tvqapiecespercheck.setText(String.valueOf(articles.get(position).getQapiecespercheck()));
        holder.tvqaacceptablelevelofdefects.setText(String.format(Locale.US,"%d %%", articles.get(position).getQaacceptablelevelofdefectspercheck()));// String.valueOf(articles.get(position).getQaacceptablelevelofdefectspercheck())+ "\u0025");
        holder.tvwhichx.setText(String.valueOf(articles.get(position).getWhichx()));
        holder.tvqadailychecks.setText(String.valueOf(articles.get(position).getQadailychecks()));

        holder.tvqaoperatorname.setText(articles.get(position).getQaoperatorname());
        holder.tvtimestampactual.setText(articles.get(position).getTimestampcurrentactual());
        holder.tvbundleordernumericid.setText(String.valueOf(articles.get(position).getBundleordernumericid()));
        holder.tvcorderid.setText(articles.get(position).getCorderid());
        holder.tvoperationcode.setText(articles.get(position).getOperationcode());
        holder.tvoperationname.setText(articles.get(position).getOperationname());
        holder.tvsize.setText(articles.get(position).getSize());
        holder.tvcolour.setText(articles.get(position).getColour());
        holder.tvactualdefectsfoundpieces.setText(String.valueOf(articles.get(position).getActualdefectsfoundpieces()));
        holder.tvactualdefectsfoundprocent.setText(String.format(Locale.US, "%d %%", articles.get(position).getActualdefectsfoundprocent()));
        holder.tvbalance.setText(articles.get(position).getBalance()==null
                                 ? ""
                                 : articles.get(position).getBalance() == 1
                                   ? "ACCEPTED"
                                   : "REJECTED"
                                );
        holder.tvwhichxactual.setText(String.valueOf(articles.get(position).getWhichxactual()));

        if (articles.get(position).getBalance()!=null)
            switch (articles.get(position).getBalance()) {
                case 1:
                    holder.tvbalance.setTextColor( ContextCompat.getColor(context, R.color.colorAccent));
                    break;
                default :
                    holder.tvbalance.setTextColor( ContextCompat.getColor(context, R.color.colorRed));
            }

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }




    public class ProcesscontrollerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // TARGET
        TextView tvpmsoperatorname;
        TextView tvtimestampcreate;
        TextView tvstylename;
        TextView tvqapiecespercheck;
        TextView tvqaacceptablelevelofdefects;
        TextView tvwhichx;
        TextView tvqadailychecks;

        // ACTUAL
        TextView tvqaoperatorname;
        TextView tvtimestampactual;
        TextView tvbundleordernumericid;
        TextView tvcorderid;
        TextView tvoperationcode;
        TextView tvoperationname;
        TextView tvsize;
        TextView tvcolour;
        TextView tvactualdefectsfoundpieces;
        TextView tvactualdefectsfoundprocent;
        TextView tvbalance;
        TextView tvwhichxactual;

        public ProcesscontrollerViewHolder(@NonNull View itemView) {

            super(itemView);

            // holds together all the views from news_item.xml
            tvpmsoperatorname = itemView.findViewById(R.id.tvpmsoperatorname);
            tvtimestampcreate = itemView.findViewById(R.id.tvtimestampcreate);
            tvstylename = itemView.findViewById(R.id.tvstylename);
            tvqapiecespercheck = itemView.findViewById(R.id.tvqapiecespercheck);
            tvqaacceptablelevelofdefects = itemView.findViewById(R.id.tvqaacceptablelevelofdefects);
            tvwhichx = itemView.findViewById(R.id.tvwhichx_target);
            tvqadailychecks = itemView.findViewById(R.id.tvqadailychecks);

            tvqaoperatorname = itemView.findViewById(R.id.tvqaoperatorname);
            tvtimestampactual = itemView.findViewById(R.id.tvtimestampactual);
            tvbundleordernumericid = itemView.findViewById(R.id.tvbundleordernumericid);
            tvcorderid = itemView.findViewById(R.id.tvcorderid);
            tvoperationcode = itemView.findViewById(R.id.tvoperationcode);
            tvoperationname = itemView.findViewById(R.id.tvoperationname);
            tvsize = itemView.findViewById(R.id.tvsize);
            tvcolour = itemView.findViewById(R.id.tvcolour);
            tvactualdefectsfoundpieces = itemView.findViewById(R.id.tvactualdefectsfoundpieces);
            tvactualdefectsfoundprocent = itemView.findViewById(R.id.tvactualdefectsfoundprocent);
            tvbalance = itemView.findViewById(R.id.tvbalance);
            tvwhichxactual = itemView.findViewById(R.id.tvwhichx_actual);

            itemView.setOnClickListener(this);

        }

        // to have onClick method here, the class must implements View.OnClickListener
        @Override
        public void onClick(View view) {

            ProcesscontrollerArticles article = articles.get(getAdapterPosition());

            // clickOnArticle is an Interface that works different by each implementation of it in different Activities: Tasks and Defects
            processcontrollerClickOnArticle.clickOnArticle(article);

        }
    }
}
