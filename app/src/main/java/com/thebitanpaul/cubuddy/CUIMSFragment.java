package com.thebitanpaul.cubuddy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;


public class CUIMSFragment extends Fragment {

    public WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_cuims, container, false);

        webView = v.findViewById(R.id.web);
        swipeRefreshLayout = v.findViewById(R.id.sv);

        //CREATE PROGRESS DIALOGE
        progressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("CUBuddy keeps you logged in...");
        progressDialog.show();

        webView.loadUrl("https://lms.cuchd.in/login/index.php");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);



            webView.setWebViewClient(new WebViewClient() {

                //dismiss progress dialog
                @Override
                public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

                }

                //keep in app
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }

                //no internet error
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    view.loadUrl("file:///android_asset/weberror.html");

                }
            });


        //swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl("https://lms.cuchd.in/login/index.php");
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });




        //back button
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == keyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        if(webView != null){
                            if(webView.canGoBack()){
                                webView.goBack();
                            }else {
                                getActivity().onBackPressed();
                            }

                        }
                    }
                }
                return true;
            }
        });


     return v;}



}