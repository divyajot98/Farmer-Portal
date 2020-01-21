package com.example.farmer_portal.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmer_portal.Classes.Addproduct;
import com.example.farmer_portal.Classes.bidding;
import com.example.farmer_portal.R;

import java.util.ArrayList;
import java.util.List;

public class myBidding extends RecyclerView.Adapter<myBidding.Biddingholder> {

    public myBidding(List<Addproduct> mybiddinglist) {
        this.mybiddinglist = mybiddinglist;
    }

    private List<Addproduct> mybiddinglist=new ArrayList<>();
    @NonNull
    @Override
    public Biddingholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mybiddingitem,parent,false);
        return new myBidding.Biddingholder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Biddingholder holder, int position) {
        final Addproduct currentnote=mybiddinglist.get(position);
        holder.title.setText(currentnote.getName());
        holder.quantity.setText(String.valueOf(currentnote.getQuantity()));
        holder.category.setText(String.valueOf(currentnote.getCategory()));

        holder.Bid.setText(String.valueOf(currentnote.getBid()));

        holder.cropprice.setText(String.valueOf(currentnote.getCropPrice()));
    }

    @Override
    public int getItemCount() {
        return mybiddinglist.size();
    }

    class Biddingholder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView category;
        private TextView quantity,Bid,cropprice;

        public Biddingholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.producttitle1);
            category=itemView.findViewById(R.id.productcategory1);
            quantity=itemView.findViewById(R.id.productquantity1);

            Bid=itemView.findViewById(R.id.Bidprice);

            cropprice=itemView.findViewById(R.id.PriceOfCrop1);
        }
    }

}
