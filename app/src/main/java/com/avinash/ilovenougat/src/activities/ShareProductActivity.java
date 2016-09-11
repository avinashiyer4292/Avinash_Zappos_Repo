package com.avinash.ilovenougat.src.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.avinash.ilovenougat.R;
import com.avinash.ilovenougat.src.MainApplicationController;

public class ShareProductActivity extends AppCompatActivity {

    private NetworkImageView sImage;
    private TextView sProductName, sPrice, sOriginalPrice, sDiscount,sLink;
    private String  product, price, orgPrice, discount,imageUrl,link;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sImage = (NetworkImageView)findViewById(R.id.sharedProductImage);
        sProductName = (TextView)findViewById(R.id.sharedProdName);
        sPrice = (TextView)findViewById(R.id.sharedProductCurrentPrice);
        sOriginalPrice = (TextView)findViewById(R.id.sharedProductOriginalPrice);
        sDiscount = (TextView)findViewById(R.id.sharedProductDiscount);
        sLink = (TextView)findViewById(R.id.sharedProductLink);
        imageLoader = MainApplicationController.getInstance().getImageLoader();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs!=null){

            product = prefs.getString("product-name",null);
            price = prefs.getString("price",null);
            orgPrice = prefs.getString("original-price",null);
            discount = prefs.getString("discount",null);
            imageUrl = prefs.getString("image-url",null);
            link = prefs.getString("link",null);
        }

        sImage.setImageUrl(imageUrl,imageLoader);
        sProductName.setText(product);
        sPrice.setText(price);
        sOriginalPrice.setText(orgPrice);
        sOriginalPrice.setPaintFlags(sOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sDiscount.setText(discount+" off");
        sLink.setText("Visit Zappos");
        sLink.setMovementMethod(LinkMovementMethod.getInstance());

        sLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

    }

}
