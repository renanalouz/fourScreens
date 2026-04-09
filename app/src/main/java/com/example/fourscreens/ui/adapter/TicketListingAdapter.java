package com.example.fourscreens.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.R;
import com.example.fourscreens.TicketDetailsActivity;
import com.example.fourscreens.data.entity.TicketListing;

import java.util.ArrayList;
import java.util.List;

public class TicketListingAdapter extends RecyclerView.Adapter<TicketListingAdapter.ViewHolder> {

    private List<TicketListing> listings = new ArrayList<>();

    public void setListings(List<TicketListing> listings) {
        this.listings = listings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketListing item = listings.get(position);

        holder.tvEventName.setText(item.getEventName());
        holder.tvDetails.setText(item.getEventDate() + " • " + item.getLocation());
        holder.tvPrice.setText("₪" + item.getPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TicketDetailsActivity.class);
            intent.putExtra("ticket", item);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName;
        TextView tvDetails;
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}