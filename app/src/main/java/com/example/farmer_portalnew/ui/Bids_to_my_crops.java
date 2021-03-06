package com.example.farmer_portalnew.ui;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.farmer_portalnew.Adapter.bidmycrop;
import com.example.farmer_portalnew.Classes.bidding;
import com.example.farmer_portalnew.Classes.farmerclass;
import com.example.farmer_portalnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bids_to_my_crops extends Fragment {

    ProgressBar progressBarrecyclebidmycrop;
    RecyclerView recyclerViewbidmycrop;

    private List<farmerclass> biddingList=new ArrayList<farmerclass>();
    public Bids_to_my_crops() {
        // Required empty public constructor

    }
    DatabaseReference myRef,productref,user;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        productref = database.getReference("users");
        progressBarrecyclebidmycrop = (ProgressBar) view.findViewById(R.id.progressBarbidmycrop);
        progressBarrecyclebidmycrop.setVisibility(View.VISIBLE);
        recyclerViewbidmycrop = view.findViewById(R.id.recycleviewbidmycrop);
        recyclerViewbidmycrop.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef.child(Objects.requireNonNull(mAuth.getUid())).child("bids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bidid = null;
                String buyerid = null;
                for(DataSnapshot dataValues:dataSnapshot.getChildren()){
                    if(!dataValues.hasChild("actualPrice")) {
                        bidding mybidding = dataValues.getValue(bidding.class);
                            bidid=mybidding.getBidId();
                             buyerid=mybidding.getBuyerId();
                    }
                }
                if(buyerid!=null&&bidid!=null) {
                    final String finalBuyerid = buyerid;
                    productref.child(buyerid).child("bids").orderByKey().equalTo(bidid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datavalues : dataSnapshot.getChildren()) {

                                farmerclass addproduct = datavalues.getValue(farmerclass.class);
                                addproduct.setBuyerid(finalBuyerid);
                                biddingList.add(addproduct);
                            }

                            recyclerViewbidmycrop.setLayoutManager(new LinearLayoutManager(getContext()));
                            bidmycrop Product_adapter = new bidmycrop(biddingList);
                            recyclerViewbidmycrop.setAdapter(Product_adapter);
                            recyclerViewbidmycrop.setHasFixedSize(true);
                            progressBarrecyclebidmycrop.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                progressBarrecyclebidmycrop.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bids_to_my_crops, container, false);
    }

    }
