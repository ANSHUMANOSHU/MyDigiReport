package com.student.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.dashboard.entity.TwoAttributes;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private ArrayList<TwoAttributes> attributes = new ArrayList<>();
    private Context context;

    public TableAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.table_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0){
            holder.column_1.setTextColor(Color.BLACK);
            holder.column_2.setTextColor(Color.BLACK);
        }
        holder.column_1.setText(attributes.get(position).column1);
        holder.column_2.setText(attributes.get(position).column2);
    }

    @Override
    public int getItemCount() {
        return attributes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView column_1,column_2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            column_1 = itemView.findViewById(R.id.column1);
            column_2 = itemView.findViewById(R.id.column2);
        }
    }

    public void setAttributes(ArrayList<TwoAttributes> attributes) {
        this.attributes = attributes;
        notifyDataSetChanged();
    }
}
