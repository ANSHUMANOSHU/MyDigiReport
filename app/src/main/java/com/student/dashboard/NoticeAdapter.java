package com.student.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.ArrayList;

import com.student.dashboard.entity.Notice;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notice> notices;

    public NoticeAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Notice> notices) {
        this.notices = notices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false);
        return new NoticeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeAdapter.MyViewHolder holder, final int position) {
        holder.heading.setText(notices.get(position).getHead());
        holder.stamp.setText(new Date(notices.get(position).getStamp()).toLocaleString());
        holder.heading.setSelected(true);
        holder.stamp.setSelected(true);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(notices.get(position).head);
                builder.setMessage(notices.get(position).body + "\n\nFrom : " + notices.get(position).from);
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notices == null ? 0 : notices.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView heading, stamp;
        LinearLayout linearLayout;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            stamp = itemView.findViewById(R.id.stamp);
            linearLayout = itemView.findViewById(R.id.notice_item);
        }
    }
}

