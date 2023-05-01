package com.thebitanpaul.cubuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecyclerContactAdapter extends FirebaseRecyclerAdapter<User, RecyclerContactAdapter.ViewHolder> {
    ArrayList<User> list;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;
    FirebaseUser user;
    DatabaseReference databaseReference;

    public String personEmail;
    public String email;
    public String uemail;
    public String curhos;
    public String curroom;
    public String deshos;
    public String desroom;
    public String userEmail;
    public String strchn;
    public String strshn;


    //search
    public void setFilteredList(ArrayList<User> filteredList){
        list = filteredList;

        notifyDataSetChanged();
    }
    //search close



    public RecyclerContactAdapter(@NonNull FirebaseRecyclerOptions<User> options){
        super(options);


//        this.list = list;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }


        acct = GoogleSignIn.getLastSignedInAccount(parent.getContext());
        if(acct!=null){
            personEmail = acct.getEmail();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("HostelSwappingDatabase");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                email = (String) snapshot.child("email").getValue();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull User model) {

        email = model.getEmail();
        uemail = model.getUserEmail();

        curhos = model.getChn();
        curroom = model.getCroom();

        deshos = model.getShn();
        desroom = model.getDroom();


        StringBuilder all = new StringBuilder();
           all.append(curhos);
           all.append(curroom);
           strchn = all.toString();

           StringBuilder alll = new StringBuilder();
           alll.append(deshos);
           alll.append(desroom);
           strshn = alll.toString();


        holder.name.setText(model.getName());
        holder.chn.setText(strchn);
        holder.shn.setText(strshn);
        holder.phone.setText(model.getPhone());


        if(acct!=null){
            if (personEmail.equals(email)) {
            holder.hostel_optn.setVisibility(View.VISIBLE);
            holder.cau.setVisibility(View.VISIBLE);
        }
            else {
                holder.hostel_optn.setVisibility(View.GONE);
                holder.cau.setVisibility(View.GONE);
            }
        }


        if(user!=null){
            if (userEmail.equals(uemail) ) {
                holder.hostel_optn.setVisibility(View.VISIBLE);
                holder.cau.setVisibility(View.VISIBLE);
            }
            else {
                holder.hostel_optn.setVisibility(View.GONE);
                holder.cau.setVisibility(View.GONE);
            }
        }


        holder.hostel_optn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext(), R.style.AlertDialogStyle);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be restored");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference()
                                .child("HostelSwappingDatabase")
                                .child(getRef(position).getKey()).removeValue();


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        holder.setIsRecyclable(false);

    }


//    @Override
//    public int getItemCount(){
//        return list.size();
//    }


//    @Override
//    public long getItemId(int position) {
//
//        return position;
//    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }




    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, chn, shn, phone, cau;
        ImageView hostel_optn;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            chn = itemView.findViewById(R.id.chn);
            shn = itemView.findViewById(R.id.shn);
            phone = itemView.findViewById(R.id.phone);

            cau = itemView.findViewById(R.id.cau);
            hostel_optn = itemView.findViewById(R.id.hostel_optn);



        }


    }

}

