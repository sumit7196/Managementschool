package com.school.tyari.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.school.tyari.FilterProductHomework;
import com.school.tyari.R;
import com.school.tyari.models.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterHomework extends RecyclerView.Adapter<AdapterHomework.HolderProductHomework> implements Filterable {

    public ArrayList<ModelProduct> productsList,filterList;
    private Context context;
    private FilterProductHomework filter;

    public AdapterHomework(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;

    }

    @NonNull
    @Override
    public AdapterHomework.HolderProductHomework onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_homework,parent,false);
        return new AdapterHomework.HolderProductHomework(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHomework.HolderProductHomework holder, int position) {

        //get data
        final ModelProduct modelProduct = productsList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String originalPrice = modelProduct.getOriginalPrice();
        String productDescription = modelProduct.getProductDescription();
        String productTitle = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimestamp();
        String productIcon = modelProduct.getProductIcon();


        //set data
        holder.titleTv.setText(productTitle);
        holder.descriptionTv.setText(productDescription);


        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_shop_primary).into(holder.productIconIv);
        }
        catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.ic_shop_primary);
        }

        /*
        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add product to cart
                showQuantityDialog(modelProduct);
            }
        });

         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show product details
            }
        });

    }

    private double cost = 0;
    private double finalCost = 0;
    private int quantity = 0;

    private void showQuantityDialog(ModelProduct modelProduct) {
        //inflate layout for dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity,null);
        //init layout views
        ImageView productIv = view.findViewById(R.id.productIv);
        final TextView titleTv = view.findViewById(R.id.titleTv);
        TextView pQuantityTv = view.findViewById(R.id.pQuantityTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView priceDiscountedTv = view.findViewById(R.id.priceDiscountedTv);
        final TextView finalPriceTv = view.findViewById(R.id.finalPriceTv);
        ImageButton decrementBtn = view.findViewById(R.id.decrementBtn);
        ImageButton incrementBtn = view.findViewById(R.id.incrementBtn);
        final TextView quantityTv = view.findViewById(R.id.quantityTv);
        Button continueBtn = view.findViewById(R.id.continueBtn);

        //get data from model

        final String productId = modelProduct.getProductId();
        String title = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String description = modelProduct.getProductDescription();
        String discountNote = modelProduct.getDiscountNote();
        String image = modelProduct.getProductIcon();

        quantity = 1;

        /*
        //dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(view);

        titleTv.setText(""+title);
        pQuantityTv.setText(""+productQuantity);
        descriptionTv.setText(""+description);
        quantityTv.setText(""+quantity);
        priceDiscountedTv.setText(""+modelProduct.getDiscountPrice());
        finalPriceTv.setText(""+finalCost);


        final AlertDialog dialog = builder.create();
        dialog.show();

        //increase quantity of product
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCost = finalCost +cost;
                quantity++;

                finalPriceTv.setText("$"+finalCost);
                quantityTv.setText(""+quantity);
            }
        });
        //decrement quantity of product
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>1){
                    finalCost = finalCost - cost;
                    quantity--;

                    finalPriceTv.setText("$"+finalCost);
                    quantityTv.setText(""+quantity);
                }
            }
        });

         */
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleTv.getText().toString().trim();
                String totalPrice = finalPriceTv.getText().toString().trim().replace("$","");
                String quantity = quantityTv.getText().toString().trim();

                //add to db(SQLite)
              //  addToCart(productId,title,priceEach,totalPrice,quantity);
            //    dialog.dismiss();
            }
        });
    }





    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProductHomework(this,filterList);
        }
        return filter;
    }

    class HolderProductHomework extends RecyclerView.ViewHolder{


        //ui views
        private ImageView productIconIv;
        private TextView discountedNoteTv,titleTv,descriptionTv,addToCartTv;

        public HolderProductHomework(@NonNull View itemView) {
            super(itemView);

            //init ui views
            productIconIv = itemView.findViewById(R.id.productIconIv);
            discountedNoteTv = itemView.findViewById(R.id.discountedNoteTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            addToCartTv = itemView.findViewById(R.id.addToCartTv);


        }
    }
}
