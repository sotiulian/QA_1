package com.wwp.QA.RoomDatabase;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wwp.QA.Login.IntronameandpasswordActivity;
import com.wwp.QA.R;

import java.util.List;


public class LoginnamelistAdapter extends RecyclerView.Adapter<LoginnamelistAdapter.LoginViewHolder> {

    private Context mCtx;
    private List<LoginnameEntity> loginnameEntityList;

    public LoginnamelistAdapter(Context mCtx, List<LoginnameEntity> loginnameEntityList){
        this.mCtx = mCtx;
        this.loginnameEntityList = loginnameEntityList;
    }

    @NonNull
    @Override
    public LoginnamelistAdapter.LoginViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_login, parent, false);

        return new LoginnamelistAdapter.LoginViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginnamelistAdapter.LoginViewHolder holder, int position) {

        LoginnameEntity t = loginnameEntityList.get(position);

        holder.textViewProcessControllerName.setText(t.getLoginname());
    }


    @Override
    public int getItemCount() {
        return loginnameEntityList.size();
    }


    class LoginViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textViewStatus, textViewProcessControllerName;

        public LoginViewHolder(View itemView){

            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewProcessControllerName = itemView.findViewById(R.id.textViewProcessControllerName);
            textViewProcessControllerName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {

            // do update

            // to work you also have to instruct the constructor to call: itemView.setOnLongClickListener(this);

            LoginnameEntity loginnameEntity = loginnameEntityList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, UpdateLoginnameActivity.class);
            intent.putExtra("loginnameEntity", loginnameEntity);

            mCtx.startActivity(intent);

            return true;
        }

        @Override
        public void onClick(View view){

            // do login

            // https://newbedev.com/how-to-call-a-mainactivity-method-from-viewholder-in-recyclerview-adapter

            // to work you also have to instruct the constructor to call: itemView.setOnClickListener(this);

            LoginnameEntity loginnameEntity = loginnameEntityList.get(getAdapterPosition());

            // here i have to check if the picked loginname is having or not the login record into pms_log_qa_target
            // if picked loginname is not logged in than we have to present the Login/Password activity
            // if picked loginname is already have the logged record into pms_log_qa_target then


            Intent intent = new Intent(mCtx, IntronameandpasswordActivity.class);
            intent.putExtra("loginnameEntity", loginnameEntity);

            mCtx.startActivity(intent);


        }

    }



}
