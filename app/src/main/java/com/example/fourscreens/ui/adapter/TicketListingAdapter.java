package com.example.fourscreens.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.R;
import com.example.fourscreens.data.entity.TicketListing;

import java.util.ArrayList;
import java.util.List;

public class TicketListingAdapter
        extends RecyclerView.Adapter<TicketListingAdapter.ViewHolder> {

    private List<TicketListing> listings = new ArrayList<>();

    // מקבל רשימה חדשה ומעדכן את המסך
    public void setListings(List<TicketListing> listings) {
        this.listings = listings;
        notifyDataSetChanged();
    }

    // יוצר View חדש לכל פריט
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket_listing, parent, false);
        return new ViewHolder(view);
    }

    // קושר נתונים לפריט
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketListing item = listings.get(position);

        holder.tvEventName.setText(item.eventName);
        holder.tvDetails.setText(item.eventDate + " • " + item.location);
        holder.tvPrice.setText("₪" + item.price);
    }

    // כמה פריטים יש
    @Override
    public int getItemCount() {
        return listings.size();
    }

    // מחזיק את ה-Views של פריט אחד
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName, tvDetails, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

