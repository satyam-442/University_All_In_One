package com.example.sonoflordshiva.bnnaio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class MyReciever extends BroadcastReceiver
{
    ConnectivityManager connectivityManager ;
    NetworkInfo networkInfo;
    AlertDialog.Builder builder;
    private AlertDialog alertdailog;

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected())
            {
                if(alertdailog != null)
                {
                    alertdailog.dismiss();
                }
            }
            else
            {
                builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.customalertlayout,null);
                builder.setView(view);
                builder.setCancelable(false);

                builder.create();
                alertdailog = builder.show();

                view.findViewById(R.id.btnCloseApp).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertdailog.dismiss();
                    }
                });

                /*
                view.findViewById(R.id.btnCloseApp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                */

            }
        }
    }
}
