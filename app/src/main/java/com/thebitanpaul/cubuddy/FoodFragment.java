package com.thebitanpaul.cubuddy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FoodFragment extends Fragment {

    List<ModelClass>list;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    RecyclerView.LayoutManager layoutManager;
    ModelClass modelClass;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;

    private static String Json_Url = "https://script.google.com/macros/s/AKfycbydY7tPJdzzyi9gjHP3f2iQho6mPLP_j3fAVmWoC1E_iOiRqUvnf2F_1yOxBb8cMd1L/exec";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View v= inflater.inflate(R.layout.fragment_food, container, false);

        recyclerView = v.findViewById(R.id.recyclerviewId);
        swipeRefreshLayout = v.findViewById(R.id.sv);
        list = new ArrayList<>();





        LinearLayoutManager layoutManager=
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        GetData();


        return v;
    }







    private void GetData() {

        ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Fetching...");
        progressDialog.setMessage("latest job alerts are on the way...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Json_Url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {




                //notification
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap largeIcon = bitmapDrawable.getBitmap();

                NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    notification = new Notification.Builder(getActivity())
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.logo)
                            .setContentText("Job Alart")
                            .setSubText("You got a new Job Alart!")
                            .setChannelId(CHANNEL_ID)
                            .build();

                    nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH) );
                }else {
                    notification = new Notification.Builder(getActivity())
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.logo)
                            .setContentText("Job Alart")
                            .setSubText("You got a new Job Alart!")
                            .build();

                }
//                nm.notify(NOTIFICATION_ID, notification);

                //close









                for (int i = 0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        list.add(new ModelClass(
                            "" + jsonObject.getString("Subject").replace("Subject","Chandigarh University DCPD"),
                            "" + jsonObject.getString("Body").replace("Body","Job Alart")


                        ));
                        progressDialog.dismiss();


                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();

                    }

                    customAdapter = new CustomAdapter(getContext(), list);
                    recyclerView.setAdapter(customAdapter);
//                    customAdapter.notifyDataSetChanged();
//                    customAdapter.notifyItemChanged(0);



                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            GetData();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                    customAdapter.notifyItemChanged(0);
                    customAdapter.notifyDataSetChanged();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

        requestQueue.add(jsonArrayRequest);



    }





}