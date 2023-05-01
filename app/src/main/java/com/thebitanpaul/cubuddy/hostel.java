package com.thebitanpaul.cubuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class hostel extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> list;
    DatabaseReference databaseReference, databaseUsers;
    RecyclerContactAdapter adapter;
    FloatingActionButton btnOpenDialog;
    TextView tenlist;
    EditText tname, tchn, tshn, tphone;
    SearchView searchView;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    AutoCompleteTextView autoCompleteTextViewchostel, autoCompleteTextViewshostel;
    public String[] schostels = {"NC-1  ", "NC-2  ", "NC-3  ", "NC-4  ", "NC-5  ","NC-6  ","ZAKIR-A  ","ZAKIR-B  ","ZAKIR-C  ","ZAKIR-D  ","GOVIND-A  ","GOVIND-B  ","LC-A  ","LC-B  ","LC-C  ","LC-D  ","SHIVALIK  ","SUKHNA-A  ","SUKHNA-B","TAGORE-A  ","TAGORE-B  "};
    public String[] sshostels = {"NC-1  ", "NC-2  ", "NC-3  ", "NC-4  ", "NC-5  ","NC-6  ","ZAKIR-A  ","ZAKIR-B  ","ZAKIR-C  ","ZAKIR-D  ","GOVIND-A  ","GOVIND-B  ","LC-A  ","LC-B  ","LC-C  ","LC-D  ","SHIVALIK  ","SUKHNA-A  ","SUKHNA-B","TAGORE-A  ","TAGORE-B  "};
    public  String chostels;
    public  String shostels;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextViewfilter;
    public String[] filterlist= {"Name", "Current Hostel", "Desired Hostel", "Current Room","Desired Room", "Phone"};
    public  String filters;
    public String filtered;
    public String  Email;
    public String userEmail;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(hostel.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel);
        swipeRefreshLayout = findViewById(R.id.sv);

        ProgressDialog progressDialog = new ProgressDialog(hostel.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Finding...");
        progressDialog.setMessage("Swap your room faster...");
        progressDialog.show();

        //swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.startListening();
                recyclerView.setAdapter(adapter);


                swipeRefreshLayout.setRefreshing(false);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }


        databaseUsers = FirebaseDatabase.getInstance().getReference();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){

            Email = acct.getEmail();

        }




        //search
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
//        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FirebaseRecyclerOptions<User> options = filterList(newText);

                adapter = new RecyclerContactAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);

                return true;
            }
        });
        //search close

        //filter
        filtered = "name";
        autoCompleteTextViewfilter = findViewById(R.id.filters);
//        int autoSizeMinTextSize = 2;
//        int autoSizeMaxTextSize = 5;
//        int autoSizeStepGranularity = 2;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            autoCompleteTextViewfilter.setAutoSizeTextTypeUniformWithConfiguration(
//                    autoSizeMinTextSize,
//                    autoSizeMaxTextSize,
//                    autoSizeStepGranularity,
//                    TypedValue.COMPLEX_UNIT_SP //specify the units for text sizes
//            );
//        }


        adapterItems = new ArrayAdapter<String>(getApplicationContext(), R.layout.filter_list_adapter,filterlist);
        autoCompleteTextViewfilter.setAdapter(adapterItems);
        autoCompleteTextViewfilter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                filters = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "Block: "+itemblock, Toast.LENGTH_SHORT).show();
                if(filters.contains("Current Hostel")){
                    filtered = "chn";
                }if(filters.contains("Desired Hostel")){
                    filtered = "shn";
                }if(filters.contains("Current Room")){
                    filtered = "croom";
                }if(filters.contains("Desired Room")){
                    filtered = "droom";
                }if(filters.contains("Name")){
                    filtered = "name";
                }if(filters.contains("Phone")){
                    filtered = "phone";
                }
            }

        });




        btnOpenDialog = findViewById(R.id.bod);
        recyclerView = findViewById(R.id.recyclerContact);


        databaseReference = FirebaseDatabase.getInstance().getReference("HostelSwappingDatabase");
        list = new ArrayList<>();


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("HostelSwappingDatabase"), User.class)
                        .build();


        adapter = new RecyclerContactAdapter(options);

        recyclerView.setAdapter(adapter);


        //make new items visible on top of recycler view
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); //removing duplicate items in recycler view

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);

                    list.add(user);

                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(hostel.this);
                dialog.setContentView(R.layout.add_update_lay);



                tname = dialog.findViewById(R.id.name);
                tchn = dialog.findViewById(R.id.chn);
                tshn = dialog.findViewById(R.id.shn);
                tphone = dialog.findViewById(R.id.phone);
                autoCompleteTextViewchostel = dialog.findViewById(R.id.schostel);
                autoCompleteTextViewshostel = dialog.findViewById(R.id.sshostel);



                //selecting current hostel
                adapterItems = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item,schostels);
                autoCompleteTextViewchostel.setAdapter(adapterItems);
                autoCompleteTextViewchostel.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        chostels = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "Block: "+itemblock, Toast.LENGTH_SHORT).show();
                    }

                });


                //selecting desired hostel
                adapterItems = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item,sshostels);
                autoCompleteTextViewshostel.setAdapter(adapterItems);
                autoCompleteTextViewshostel.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        shostels = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "Block: "+itemblock, Toast.LENGTH_SHORT).show();
                    }

                });
                //

                tenlist = dialog.findViewById(R.id.enlist);

                databaseUsers = FirebaseDatabase.getInstance().getReference();

                tenlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String Name = tname.getText().toString().toUpperCase().trim();
                        String curhostel = chostels;
                        String chostel = tchn.getText().toString().trim();


                        String dishostel = shostels;
                        String shostel = tshn.getText().toString().trim();


                        String phone = tphone.getText().toString().trim();




                        if (TextUtils.isEmpty(Name)) {
                            tname.setError("Please enter your name!");
                            return;
                        }
                        if (Name.length() > 10) {
                            tname.setError("Name can be of 10 characters!");
                            return;
                        }
                        if (TextUtils.isEmpty(curhostel)){
                            autoCompleteTextViewchostel.setError("Please select your hostel!");
                            return;
                        }

                        if (TextUtils.isEmpty(chostel)) {
                            tchn.setError("Please enter your room no.!");
                            return;
                        }
                        if (chostel.length() < 3) {
                            tchn.setError("Please enter valid room no.!");
                            return;
                        }
                        if (chostel.length() > 4) {
                            tchn.setError("Please enter valid room no.!");
                            return;
                        }
                        if (TextUtils.isEmpty(dishostel)){
                            autoCompleteTextViewshostel.setError("Please select desired hostel!");
                            return;
                        }
                        if (TextUtils.isEmpty(shostel)) {
                            tshn.setError("Please enter desired hostel room no.!");
                            return;
                        }
                        if (shostel.length() < 3) {
                            tshn.setError("Please enter valid room no.!");
                            return;
                        }
                        if (shostel.length() > 4) {
                            tshn.setError("Please enter valid room no.!");
                            return;
                        }
                        if (TextUtils.isEmpty(phone)) {
                            tphone.setError("Please enter your contact number!");
                            return;
                        }
                        if (phone.length() < 10) {
                            tphone.setError("Phone must be of 10 characters");
                            return;
                        }
                        if (phone.length() > 10) {
                            tphone.setError("Phone must be of 10 characters");
                            return;
                        }

                        String id = databaseUsers.push().getKey();

                        User user = new User(Name, curhostel,chostel, dishostel, shostel, phone, Email, userEmail);
                        databaseUsers.child("HostelSwappingDatabase").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(hostel.this, "Get ready to swap your room!", Toast.LENGTH_SHORT).show();

                                    recyclerView.scrollToPosition(list.size() - 1);
                                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                }
                            }
                        });

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //search

    public FirebaseRecyclerOptions<User>  filterList(String text) {

        String textone = text.toUpperCase().trim();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(databaseReference.orderByChild(filtered).startAt(textone).endAt(textone+ "~"), User.class)
                        .build();

        return options;

//        adapter = new RecyclerContactAdapter(options);
//        adapter.startListening();
//        recyclerView.setAdapter(adapter);










//        ArrayList<User> filteredList = new ArrayList<>();
//        for (User user : list){
//            if(user.getName().toLowerCase().contains(text.toLowerCase())||
//                    user.getChn().toLowerCase().contains(text.toLowerCase()) ||
//                    user.getShn().toLowerCase().contains(text.toLowerCase()) ||
//                    user.getPhone().toLowerCase().contains(text.toLowerCase()))
//            {
//                filteredList.add(user);
//            }
//
//        }
//
//        if(filteredList.isEmpty()){
//            Toast.makeText(this, "Sorry, Can't find any matching data :(", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            adapter.setFilteredList(filteredList);
//
//
//            }


    }

    //search close

}















