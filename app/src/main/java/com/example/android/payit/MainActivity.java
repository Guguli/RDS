package com.example.android.payit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity  extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    //the textview in the interface where we have the price
    TextView textViewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the textview
        textViewPrice = findViewById(R.id.textViewPrice);


        //attaching a click listener to the button buy
        findViewById(R.id.buttonBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //calling the method to initialize the payment using paytm. Here using checksumhash created on localhost
            initializePaytmPayment("GLTX1bKmN3pmObCwoe6vBqhzXG1KkDYrYTt2GjyORVs4XAfvDmEAQbAVi0JF49uVTYfi8n94eNjj8enG2yw43rEEvE6F6U4G/uMrhEb5Eoo=");
            }
        });

    }
    //use this when using for production
    //PaytmPGService Service = PaytmPGService.getProductionService();


    private void initializePaytmPayment(String checksumHash) {
        //creating a hashmap and adding all the values required
        //        ToDo change the value according to requirements
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", "ORDER2209");
        paramMap.put("CUST_ID", "CUST2209");
        paramMap.put("CHANNEL_ID", Constants.CHANNEL_ID);
        // currently hardcoded the data of amount. Later will be taken from text value
        paramMap.put("TXN_AMOUNT", "167");
        paramMap.put("WEBSITE", Constants.WEBSITE);
        paramMap.put("CALLBACK_URL", Constants.CALLBACK_URL);
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", Constants.INDUSTRY_TYPE_ID);


        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        try{
            PaytmOrder order = new PaytmOrder(paramMap);

            //intializing the paytm service
            Service.initialize(order, null);

            //finally starting the payment transaction
            Service.startPaymentTransaction(this, true, true, this);


        }
        catch(Exception ex){
            Toast.makeText(this,"Some error occured. Please try again.",Toast.LENGTH_LONG).show();
            Log.d("Error","exc: "+ex.getMessage());
        }
    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Log.e("noteError","error1");
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Log.e("noteError","error2");
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("noteError","error3");
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("noteError","error4");
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("noteError","error5");
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("noteError","error6");
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }
}
