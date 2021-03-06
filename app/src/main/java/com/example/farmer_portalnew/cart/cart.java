package com.example.farmer_portalnew.cart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farmer_portalnew.Adapter.Cartadapter;
import com.example.farmer_portalnew.Adapter.bidmycrop;
import com.example.farmer_portalnew.Classes.Cart;
import com.example.farmer_portalnew.PaymentMethod.creditcard;
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

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class cart extends AppCompatActivity {
    ProgressBar progressBarrecyclemycart;
    RecyclerView recyclerViewmycart;
    DatabaseReference myRef,search;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private List<Cart> mycartList = new ArrayList<Cart>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        search = database.getReference("users");
        progressBarrecyclemycart=findViewById(R.id.progressBarmycart);
        recyclerViewmycart=findViewById(R.id.recycleviewmycart);
        progressBarrecyclemycart.setVisibility(View.VISIBLE);
        recyclerViewmycart.setLayoutManager(new LinearLayoutManager(getContext()));
        myRef.child(Objects.requireNonNull(mAuth.getUid())).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datavalues:dataSnapshot.getChildren()){
                    Cart cart=datavalues.getValue(Cart.class);
                    mycartList.add(cart);
                }
                final Cartadapter Product_adapter = new Cartadapter(mycartList, new Cartadapter.OnItemClicked() {
                    @Override
                    public void onbuttonclicked(final int position) {
                         progressBarrecyclemycart.setVisibility(View.VISIBLE);
                         search.child(mAuth.getUid()).child("cart").orderByChild("cropid").equalTo(mycartList.get(position).getCropid()).addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 dataSnapshot.getRef().removeValue();

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
                        Toast.makeText(cart.this, "Removed Successfully", Toast.LENGTH_SHORT).show();
                         mycartList.remove(position);
                        progressBarrecyclemycart.setVisibility(View.GONE);

                    }
                });
                recyclerViewmycart.setAdapter(Product_adapter);
                recyclerViewmycart.setHasFixedSize(true);
                progressBarrecyclemycart.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.cartmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.checkout){
           startActivity(new Intent(cart.this, creditcard.class));
       }
        return super.onOptionsItemSelected(item);

    }
}
