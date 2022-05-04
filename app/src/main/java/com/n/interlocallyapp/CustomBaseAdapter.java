package com.n.interlocallyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<ShopCuisineProduct> listFruit;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, ArrayList<ShopCuisineProduct> fruitList) {

        this.context = ctx;
        this.listFruit = fruitList;
        inflater = LayoutInflater.from(ctx);


    }

    @Override
    public int getCount() {
        return listFruit.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.activity_custom_list_view,null);
        TextView nameText = (TextView) convertView.findViewById(R.id.itemName);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.itemDescription);
        ImageView prodImg = (ImageView) convertView.findViewById(R.id.imageIcon);
        ShopCuisineProduct product = listFruit.get(position);
        nameText.setText(product.name);
        descriptionText.setText(product.description);

       //Picasso.get().load(product.imagelink).into(prodImg);

        Picasso.get().load("gs://interlocally-c1c70.appspot.com/images/farofa.jpg").into(prodImg);


        return convertView;
    }
}
