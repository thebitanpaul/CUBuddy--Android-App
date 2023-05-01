package com.thebitanpaul.cubuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NotesFragment extends Fragment {

    WebView webView, webView1, webView2;
    WebViewClient webViewClient;
    EditText uid, concern, rno;
    TextView contact, reel, raise, hostel;
    ImageView AIML, BD, CC, IOT, CSE;
    AsyncTask mytask;
    public Session session;
    public String username;
    public String password;
    public String subject;
    public String messageToSend;

    public String[] blocks = {"D1 ", "D2 ", "D3 ", "D4 ", "D5 "};
    public String[] floors = {"1st floor  ", "2nd floor  ", "3rd floor  ", "4th floor  ", "5th floor  "};
    public String itemblock;
    public String itemfloor;
    AutoCompleteTextView autoCompleteTextViewblock, autoCompleteTextViewfloor;
    ArrayAdapter<String> adapterItems;

    SwipeRefreshLayout swipeRefreshLayout;


    public NotesFragment(){
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_notes, container, false);

        AIML = v.findViewById(R.id.AIML);
        BD = v.findViewById(R.id.BD);
        CC = v.findViewById(R.id.CC);
        IOT = v.findViewById(R.id.IOT);
        CSE = v.findViewById(R.id.CSE);
        contact = v.findViewById(R.id.contact);
        reel = v.findViewById(R.id.reel);
        uid = v.findViewById(R.id.uid);
        rno = v.findViewById(R.id.roomno);
        concern =v.findViewById(R.id.concern);
        raise = v.findViewById(R.id.raise);
        hostel = v.findViewById(R.id.hostel);
        autoCompleteTextViewblock = v.findViewById(R.id.sblock);
        autoCompleteTextViewfloor = v.findViewById(R.id.sfloor);
        swipeRefreshLayout = v.findViewById(R.id.sv);



        //selecting block
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item,blocks);
        autoCompleteTextViewblock.setAdapter(adapterItems);
        autoCompleteTextViewblock.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemblock = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "Block: "+itemblock, Toast.LENGTH_SHORT).show();
            }

        });
        //

        // selecting floors
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item,floors);
        autoCompleteTextViewfloor.setAdapter(adapterItems);
        autoCompleteTextViewfloor.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemfloor = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "Floor: "+itemfloor, Toast.LENGTH_SHORT).show();
            }

        });
        //


        //open cuims
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLink("https://uims.cuchd.in/uims/");
            }
        });

        //hosetel
        hostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), hostel.class));
                getActivity().finish();
            }
        });


        //swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        raise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                username = "cubuddyapp@gmail.com";
                password = "your_password_here";
                String messageToSendone = itemblock;
                String messageToSendtwo = itemfloor;
                String messageToSendthree = rno.getText().toString();
                String messageToSendfour = concern.getText().toString();

                StringBuilder all = new StringBuilder();
                        all.append(messageToSendone);
                        all.append(messageToSendtwo);
                        all.append(messageToSendthree+" is facing issue of:  ");
                        all.append(messageToSendfour);
                        messageToSend = all.toString();


                subject = uid.getText().toString();

                if (TextUtils.isEmpty(subject)){
                    uid.setError("Please enter your registered CU mail ID!");
                    return;
                }
                if (!subject.contains("@cuchd.in") && !subject.contains("@cumail.in")
                    && !subject.contains("@CUCHD.IN") && !subject.contains("@CUMAIL.IN")){
                    uid.setError("Please enter correct CU mail ID!");
                    return;
                }
                if (TextUtils.isEmpty(messageToSendone)){
                    autoCompleteTextViewblock.setError("Please select the block no.");
                    return;
                }
                if (TextUtils.isEmpty(messageToSendtwo)){
                    autoCompleteTextViewfloor.setError("Please select the floor!");
                    return;
                }

                if (TextUtils.isEmpty(messageToSendthree)){
                    rno.setError("Please enter your room no.!");
                    return;
                }
                if (messageToSendthree.length() < 3) {
                    rno.setError("Please enter valid room no.!");
                    return;
                }
                if (messageToSendthree.length() > 4) {
                    rno.setError("Please enter valid room no.!");
                    return;
                }

                if (TextUtils.isEmpty(messageToSendfour)){
                    concern.setError("Please enter your issue!");
                    return;
                }

                //Caling the Asynctask class to execute and send mail
                    mytask = new Mytask().execute();

            }
        });



        //stay nerd card
        webView = v.findViewById(R.id.wb);
        webView.loadUrl("https://sites.google.com/view/feelinglikestudyingtoday/");
        webView.setBackgroundColor(0);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });



        //INTERNET ERROR MSG
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/nerd.html");

            }
        });




        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.clearCache(true);





        //shottbyparth
        webView2 = v.findViewById(R.id.wb2);
        webView2.loadUrl("https://sites.google.com/view/shottbyparth");
        webView2.setBackgroundColor(0);

        //keep in app
        webView2.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });



        //INTERNET ERROR MSG
        webView2.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/partherror.html");

            }
        });




        WebSettings webSettings2 = webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
        webView2.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView2.getSettings().setAllowFileAccess(true);


        //magic happens compiler
        webView1 = v.findViewById(R.id.wb1);


        webView1.loadUrl("https://www.tutorialspoint.com/online_cpp_compiler.php");
        webView1.setBackgroundColor(0);

        //keep in app
        webView1.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

        });


        //back button
        webView1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == keyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        if(webView1 != null){
                            if(webView1.canGoBack()){
                                webView1.goBack();
                            }else {
                                getActivity().onBackPressed();
                            }

                        }
                    }
                }
                return true;
            }
        });




        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/compiler.html");

            }
        });

        //hiding web element with error message

        webView1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('north').style.display='none';})()");
            }
        });






        WebSettings webSettings1 = webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);





        webView1.setVerticalScrollBarEnabled(true);
        webView1.setHorizontalScrollBarEnabled(true);
        webView1.setScrollbarFadingEnabled(false);
        webView1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView1.requestFocus();








        //reel
        reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLink("https://sites.google.com/view/shottbyparth");

            }
        });







        //notes
        AIML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLink("Your_Google_Drive_Link_Here");

            }
        });

        BD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Content coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        CC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Content coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        IOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Content coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        CSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Content coming soon...", Toast.LENGTH_SHORT).show();
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLink("https://uims.cuchd.in/uims/");
            }
        });

        return v;
    }



    //asynctask class
    public class Mytask extends AsyncTask<String, String, String>
    {
        ProgressDialog dialogue;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogue = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
            dialogue.setTitle("Processing");
            dialogue.setMessage("Calm down, your issue is being raised...");
            dialogue.setIndeterminate(true);
            dialogue.setCancelable(false);
            dialogue.show();

        }
        @Override
        protected String doInBackground(String... ignore) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);


            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.socketFactory.port", "587");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });





                try {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse("Your_CUmail_here"));
                        message.setSubject(subject);
                        message.setText(messageToSend);
                        Transport.send(message);


                } catch (MessagingException e) {
//                    Log.e("EmailSendingError", e.getMessage());
//                    throw new RuntimeException(e);
                    return "";

                }


//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
            return "CUBuddy";
        }
        @Override
        protected void onPostExecute(String e) {


            if (e.length() >0) {
                if(dialogue.isShowing()){
                    dialogue.dismiss();
                }
                rno.getText().clear();
                concern.getText().clear();
                uid.getText().clear();
                autoCompleteTextViewblock.getText().clear();
                autoCompleteTextViewfloor.getText().clear();

                Toast.makeText(getActivity(), "Concern Raised Successfully", Toast.LENGTH_LONG).show();
            }else {
                if(dialogue.isShowing()){
                    dialogue.dismiss();
                }
                Toast.makeText(getActivity(), "Internet connection lost, please try again...", Toast.LENGTH_LONG).show();
            }

        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





    private void goLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }



}
