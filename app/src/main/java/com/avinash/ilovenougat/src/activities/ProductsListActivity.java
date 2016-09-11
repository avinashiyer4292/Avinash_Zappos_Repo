package com.avinash.ilovenougat.src.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.avinash.ilovenougat.R;
import com.avinash.ilovenougat.src.MainApplicationController;
import com.avinash.ilovenougat.src.adapters.ProductsRecyclerViewAdapter;
import com.avinash.ilovenougat.src.utils.AppConstants;
import com.avinash.ilovenougat.src.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsListActivity extends AppCompatActivity implements ProductsRecyclerViewAdapter.ProductClickListener,
                                                                        View.OnClickListener
{
    private static final String TAG = "PRODUCTS LIST ACTIVITY";
    private String product=null;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private ProgressDialog mProgressDialog;
    private TextView mTextView, zapposPrice, sixpmPrice, comparedPrice;
    private NetworkImageView zapposImage, sixpmImage;
    private AlertDialog mAlertDialog;
    private AlertDialog.Builder alertBuilder;
    private ProductsRecyclerViewAdapter mAdapter;
    private ProductsRecyclerViewAdapter.ProductClickListener mProductClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            product = bundle.getString("product");
        }


        Log.d(TAG, "Product: "+product);
        String zapposURL = AppConstants.zappos_api_prefix +  product  +  AppConstants.zappos_api_suffix;
        Log.d(TAG, "Zappos URL: "+zapposURL);
        mProgressDialog = new ProgressDialog(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mTextView = (TextView)findViewById(R.id.errorTextView);
        startSearchRequest(zapposURL);

    }


    @Override
    public void onClick(View view) {

    }
    private void startSearchRequest(String zapposURL) {
        mProgressDialog.setMessage("Searching for products...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        performRequest(zapposURL);

    }

    private void performRequest(String zapposURL) {
        Log.d(TAG, "In perform request method, url still: "+zapposURL);

        JsonObjectRequest jar = new JsonObjectRequest(zapposURL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                if(mProgressDialog!=null)
                    mProgressDialog.dismiss();
                mProgressDialog=null;

                parseResponse(response);
                //Toast.makeText(getApplicationContext(), "Response success", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Response: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null)
                    mProgressDialog.dismiss();
                mProgressDialog=null;

                /*mRecyclerView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("No products found!");*/

                Log.d("Volley error", error.toString() + " ----------- " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error in response!", Toast.LENGTH_SHORT).show();
            }
        });

        MainApplicationController.getInstance().addToRequestQueue(jar);
    }

    private void parseResponse(JSONObject response){
        //String originalTerm,totalResultCount,term,statusCode=null;
        int currentResultCount = 0;
        ArrayList<Product> products = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            currentResultCount= Integer.parseInt(response.getString("currentResultCount"));
            if(currentResultCount==0){

                    mTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mTextView.setText("No products found for your search term!");

            }
            else{
                jsonArray = response.getJSONArray("results");
                if(mRecyclerView.getVisibility()==View.GONE) {
                    mTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    products.add(getProductFromJsonObject(jsonObj));
                }
                initialiseProductAdapter(products);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Product getProductFromJsonObject(JSONObject jsonObj){
        Product p=null;
        try {
            String brandName= jsonObj.getString("brandName");
            String thumbnailImageUrl= jsonObj.getString("thumbnailImageUrl");
            String productId= jsonObj.getString("productId");
            String originalPrice= jsonObj.getString("originalPrice");
            String styleId= jsonObj.getString("styleId");
            String colorId= jsonObj.getString("colorId");
            String price= jsonObj.getString("price");
            String percentOff= jsonObj.getString("percentOff");
            String productUrl= jsonObj.getString("productUrl");
            String productName= jsonObj.getString("productName");
            p =  new Product(brandName,thumbnailImageUrl,productId,originalPrice,styleId,
                    colorId,price,percentOff,productUrl,productName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }

    private void initialiseProductAdapter(ArrayList<Product> products){
        mGridLayoutManager = new GridLayoutManager(ProductsListActivity.this,2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new ProductsRecyclerViewAdapter(ProductsListActivity.this, products,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onProductClicked(Product p) {
        String sixpmURL = AppConstants.sixpm_api_prefix +p.getBrandName() +AppConstants.sixpm_api_suffix;
        startComparisonRequest(sixpmURL,p);

    }

    private void startComparisonRequest(String sixpmURL, Product p) {
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(ProductsListActivity.this);
            mProgressDialog.setMessage("Looking for product in 6PM...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        performComparisonRequest(sixpmURL,p);
    }

    private void performComparisonRequest(String sixpmURL,Product p) {
        final Product product = p;
        JsonObjectRequest jar = new JsonObjectRequest(sixpmURL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                if(mProgressDialog!=null)
                    mProgressDialog.dismiss();
                mProgressDialog=null;

                parseComparisonResponse(response,product);
                //Log.d(TAG,"Response: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressDialog!=null)
                    mProgressDialog.dismiss();
                mProgressDialog=null;

                Log.d("Volley error", error.toString() + " ----------- " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error in response!", Toast.LENGTH_SHORT).show();
            }
        });

        MainApplicationController.getInstance().addToRequestQueue(jar);

    }

    private void parseComparisonResponse(JSONObject response, Product p) {
        Log.d(TAG, "In product cpmparsion parse function");
        JSONArray jsonArray=null;
        int count=0, length=0;
        try {
            jsonArray = response.getJSONArray("results");
            length = response.getJSONArray("results").length();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObj = jsonArray.getJSONObject(i);
                if(p.getProductName().equals(jObj.getString("productName"))
                        && p.getProductId().equals(jObj.getString("productId"))){
                    Log.d(TAG,"Match found!");
                    Product p2 = getProductFromJsonObject(jObj);
                    compareProducts(p,p2);

                }
                else
                    {
                        //Log.d(TAG, "Match not found!");
                        count++;
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Non matches count:"+count);
        if(count==length)
            Toast.makeText(getApplicationContext(),"No matches found on 6PM!",Toast.LENGTH_SHORT).show();
    }

    private void compareProducts(Product p1, Product p2){
        ImageLoader imageLoader = MainApplicationController.getInstance().getImageLoader();
        Log.d(TAG, "In product comparison function");
        alertBuilder = new AlertDialog.Builder(ProductsListActivity.this);
        mAlertDialog = alertBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.compare_alert_layout,null);
        zapposImage = (NetworkImageView)v.findViewById(R.id.zapposImage);
        sixpmImage = (NetworkImageView)v.findViewById(R.id.sixpmImage);
        zapposPrice = (TextView)v.findViewById(R.id.zapposPrice);
        sixpmPrice = (TextView)v.findViewById(R.id.sixpmPrice);
        comparedPrice = (TextView)v.findViewById(R.id.finalComparedPrice);

        zapposImage.setImageUrl(p1.getThumbnailImageUrl(),imageLoader);
        sixpmImage.setImageUrl(p2.getThumbnailImageUrl(),imageLoader);

        String zPrice = p1.getPrice();
        String sPrice = p2.getPrice();

        String finalComparison = cheaperPrice(zPrice, sPrice);

        zapposPrice.setText(zPrice);
        sixpmPrice.setText(sPrice);

        if(finalComparison.equals(zPrice)){
            Log.d(TAG,"Cheaper price is: "+zPrice);
            zapposPrice.setTextColor(getResources().getColor(R.color.primaryBackgroundColor));
            comparedPrice.setText("Zappos is cheaper");
        }
        else if(finalComparison.equals(sPrice)){
            sixpmPrice.setTextColor(getResources().getColor(R.color.primaryBackgroundColor));
            Log.d(TAG,"Cheaper price is: "+sPrice);
            comparedPrice.setText("6PM is cheaper");
        }
        else{
            comparedPrice.setText("Prices are same");
        }


        alertBuilder.setView(v);
        alertBuilder.setCancelable(true);
        alertBuilder.show();

    }

    private String cheaperPrice(String price1, String price2){
        String p1 = price1.substring(1);
        String p2  = price2.substring(1);
        float pr1 = Float.parseFloat(p1);
        float pr2 = Float.parseFloat(p2);
        if(pr1<pr2){
            return price1;
        }
        else if(pr1>pr2) {
            return price2;
        }
        else return "0";
    }


    @Override
    public void onProductShared(Product p) {
        String[] addresses={};
        String subject = "Zappos Product Link";
        String body = "Install the 'ILoveNougat' app to open the following link."+"\n"+"http://zappos.ly/share/"+p.getProductId()+"&"+p.getStyleId()+"&"+p.getColorId();
        composeEmail(p,addresses,subject,body);
    }

    public void composeEmail(Product p,String[] addresses, String subject, String body) {
        saveProductInfo(p);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(intent);

    }

    private void saveProductInfo(Product p){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        if(editor!=null){
            editor.clear();
        }
        editor.putString("brand-name",p.getBrandName());
        editor.putString("product-name",p.getProductName());
        editor.putString("price",p.getPrice());
        editor.putString("original-price",p.getOriginalPrice());
        editor.putString("discount",p.getPercentOff());
        editor.putString("image-url",p.getThumbnailImageUrl());
        editor.putString("link",p.getProductUrl());
        editor.commit();
    }


}
