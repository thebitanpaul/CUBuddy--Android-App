package com.thebitanpaul.cubuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelClass> mData;

    public CustomAdapter(Context mContext, List<ModelClass> mData){
        this.mContext = mContext;
        this.mData = mData;


    }

    //experiment
//    public void updateList (List<ModelClass> newData) {
//        this.mData.clear();
//        mData.addAll(newData);
//        notifyDataSetChanged();
//    }


    //close


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(mData.get(position).getTitle());
        holder.details.setText(mData.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, details;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.head);
            details = itemView.findViewById(R.id.desc);
//            imageView = itemView.findViewById(R.id.img1);



        }



    }



















}
