package com.practise.eatit.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.eatit.R;
import com.practise.eatit.interfaces.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderIdTV, orderStatusTV, orderPhoneTV, orderAddressTV;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderIdTV = itemView.findViewById(R.id.order_item_id);
        orderStatusTV = itemView.findViewById(R.id.order_status);
        orderPhoneTV = itemView.findViewById(R.id.order_phone);
        orderAddressTV = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition(), false);
    }
}
