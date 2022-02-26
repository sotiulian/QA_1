package com.wwp.QA.Machine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wwp.QA.R;

import java.util.ArrayList;

public class MachineAdapter extends RecyclerView.Adapter<com.wwp.QA.Machine.MachineAdapter.MachineViewHolder> {

    Context context;
    ArrayList<MachineArticles> articles;

    public MachineAdapter(Context context, ArrayList<MachineArticles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public com.wwp.QA.Machine.MachineAdapter.MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.machine_item, parent, false);
        return new com.wwp.QA.Machine.MachineAdapter.MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.wwp.QA.Machine.MachineAdapter.MachineViewHolder holder, int position) {

        // use model getters and setters to set the views into news_item.xml layout

        holder.tvmachine_machineid.setText(articles.get(position).getMachineid());
        holder.tvmachine_areaname.setText(articles.get(position).getAreaname());
        holder.tvmachine_corderid.setText(articles.get(position).getCorderid());
        holder.tvmachine_stylename.setText(articles.get(position).getStylename());
        holder.tvmachine_bundleid.setText(String.valueOf(articles.get(position).getBundleordernumericid())); // integer used with String.valueOf(
        holder.tvmachine_operationname.setText(articles.get(position).getOperationname());
        holder.tvmachine_size.setText(articles.get(position).getSize());
        holder.tvmachine_color.setText(articles.get(position).getColour());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class MachineViewHolder extends RecyclerView.ViewHolder{

        TextView tvmachine_machineid;
        TextView tvmachine_areaname;
        TextView tvmachine_corderid;
        TextView tvmachine_stylename;
        TextView tvmachine_bundleid;
        TextView tvmachine_operationname;
        TextView tvmachine_size;
        TextView tvmachine_color;

        public MachineViewHolder(@NonNull View itemView) {

            super(itemView);

            // holds together all the views from news_item.xml

            tvmachine_machineid = itemView.findViewById(R.id.tvmachine_machineid);
            tvmachine_areaname = itemView.findViewById(R.id.tvmachine_areaname);
            tvmachine_corderid = itemView.findViewById(R.id.tvmachine_corderid);
            tvmachine_stylename = itemView.findViewById(R.id.tvmachine_stylename);
            tvmachine_bundleid = itemView.findViewById(R.id.tvmachine_bundleid);
            tvmachine_operationname = itemView.findViewById(R.id.tvmachine_operationname);
            tvmachine_size = itemView.findViewById(R.id.tvmachine_size);
            tvmachine_color = itemView.findViewById(R.id.tvmachine_color);

        }

    }
}
