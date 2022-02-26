package com.wwp.QA.RoomDatabase;

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

import java.util.List;

public class SysadminlistAdapter extends RecyclerView.Adapter<SysadminlistAdapter.SysadminViewHolder> {

    private Context mCtx;
    private List<SysadminEntity> sysadminEntityList;

    public SysadminlistAdapter(Context mCtx, List<SysadminEntity> sysadminEntityList){
        this.mCtx = mCtx;
        this.sysadminEntityList = sysadminEntityList;
    }

    @NonNull
    @Override
    public SysadminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_sysadmin, parent, false);

        return new SysadminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SysadminViewHolder holder, int position) {

        SysadminEntity t = sysadminEntityList.get(position);

        holder.textViewTask.setText(t.getWebaddress());
        holder.textViewDesc.setText(t.getDesc());

        if (t.isActive())
            holder.textViewStatus.setText("Active");
        else
            holder.textViewStatus.setText("Not active");
    }


    @Override
    public int getItemCount() {
        return sysadminEntityList.size();
    }


    class SysadminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView textViewStatus, textViewTask, textViewDesc, textViewFinishBy;

        public SysadminViewHolder(View itemView){

            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTask = itemView.findViewById(R.id.textViewWebaddress);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {

            // do update

            // to work you also have to instruct the constructor to call: itemView.setOnClickListener(this);

            SysadminEntity sysadminEntity = sysadminEntityList.get(getAdapterPosition());

            Log.e("SA","You long clicked on -> " + sysadminEntity.getWebaddress());

            Intent intent = new Intent(mCtx, UpdateSysadminActivity.class);
            intent.putExtra("sysadminEntity", sysadminEntity);

            mCtx.startActivity(intent);

            return false;
        }

        @Override
        public void onClick(View view) {

            // to work you also have to instruct the constructor to call: itemView.setOnLongClickListener(this);

            SysadminEntity sysadminEntity = sysadminEntityList.get(getAdapterPosition());
            Log.e("SA","You clicked on -> " + sysadminEntity.getWebaddress());

            // do nothing ...

        }
    }




}
