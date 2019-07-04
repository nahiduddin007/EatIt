package com.practise.eatit.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.practise.eatit.R;
import com.practise.eatit.interfaces.ItemClickListener;
import com.practise.eatit.model.Order;
import com.practise.eatit.utils.Common;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,
        View.OnCreateContextMenuListener {

    public TextView cartNameTV, priceTV;
    public ImageView cartCountIV;

    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartNameTV = itemView.findViewById(R.id.cart_item_name);
        priceTV = itemView.findViewById(R.id.cart_item_price);
        cartCountIV = itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0, 0, getAdapterPosition(), Common.DELETE);
    }
}

public class CartAdapter  extends RecyclerView.Adapter<CartViewHolder>{

    List<Order> orderList;
    private Context context;

    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+orderList.get(position).getQuantity(), Color.RED);
        holder.cartCountIV.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat frm = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt((orderList.get(position).getPrice()))*(Integer.parseInt(orderList.get(position).getQuantity())));
        holder.priceTV.setText(""+price);
        holder.cartNameTV.setText(orderList.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
