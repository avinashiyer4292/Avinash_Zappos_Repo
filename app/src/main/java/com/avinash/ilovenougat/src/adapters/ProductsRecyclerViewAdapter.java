package com.avinash.ilovenougat.src.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.avinash.ilovenougat.R;
import com.avinash.ilovenougat.src.MainApplicationController;
import com.avinash.ilovenougat.src.model.Product;

import java.util.ArrayList;

/**
 * Created by Dell on 9/9/2016.
 */
public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.RecyclerViewHolder>
{

    private ArrayList<Product> products;
    private Context context;
    private ImageLoader imageLoader=null;
    private ProductClickListener mListener;

    public ProductsRecyclerViewAdapter(Context context,ArrayList<Product> products,ProductClickListener mListener ){
        this.products = products;
        this.context = context;
        this.mListener = mListener;
        imageLoader= MainApplicationController.getInstance().getImageLoader();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout,null);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //holder.brandName.setText(products.get(position).getBrandName());
        holder.productName.setText(products.get(position).getProductName());
        holder.currentPrice.setText(products.get(position).getPrice());
        holder.originalPrice.setText(products.get(position).getOriginalPrice());
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.percentOff.setText(products.get(position).getPercentOff()+" off");
        holder.thumbnailImage.setImageUrl(products.get(position).getThumbnailImageUrl(),imageLoader);
        holder.mShare.setId(position);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private NetworkImageView thumbnailImage;
        private TextView productName,currentPrice, originalPrice, percentOff;
        private ImageView mShare;
        public RecyclerViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            productName = (TextView)view.findViewById(R.id.productName);
            currentPrice = (TextView)view.findViewById(R.id.currentPrice);
            originalPrice = (TextView)view.findViewById(R.id.originalPrice);
            percentOff = (TextView)view.findViewById(R.id.percentOff);
            thumbnailImage = (NetworkImageView)view.findViewById(R.id.productImage);
            mShare = (ImageView)view.findViewById(R.id.shareProductImageView);
            mShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId()==this.getLayoutPosition()) {
                Log.d("adapter","sharing....");
                Product p = products.get(this.getLayoutPosition());
                mListener.onProductShared(p);
            } else {
                Product p = products.get(this.getLayoutPosition());
                mListener.onProductClicked(p);
            }
        }


            }
    public interface ProductClickListener{
        void onProductClicked(Product p);
        void onProductShared(Product p);
    }

}
