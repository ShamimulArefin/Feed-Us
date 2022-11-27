package com.codealap.feedus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Donation> list;

    public MyAdapter(Context context, ArrayList<Donation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Donation donation = list.get(position);
        holder.name.setText(donation.getName());
        holder.city.setText(donation.getCity());
        holder.date.setText(donation.getDate());
        holder.person.setText(donation.getPerson());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),View_detiles.class);
                intent.putExtra("name",donation.getName());
                intent.putExtra("city",donation.getCity());
                intent.putExtra("date",donation.getDate());
                intent.putExtra("person",donation.getPerson());

                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, city,date,person;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.showdonarUserName);
            city = itemView.findViewById(R.id.showdonationAria);
            date = itemView.findViewById(R.id.showdonationDate);
            person = itemView.findViewById(R.id.showdonationAmount);
        }
    }
}
