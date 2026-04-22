package com.example.fourscreens.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.R;
import com.example.fourscreens.TicketDetailsActivity;
import com.example.fourscreens.data.entity.TicketListing;

import java.util.ArrayList;
import java.util.List;

public class TicketListingAdapter extends RecyclerView.Adapter<TicketListingAdapter.ViewHolder> {

    public interface OnTicketActionListener {
        void onEdit(TicketListing ticket);
        void onDelete(TicketListing ticket);
    }

    private List<TicketListing> listings = new ArrayList<>();
    private final OnTicketActionListener listener;

    public TicketListingAdapter(OnTicketActionListener listener) {
        this.listener = listener;
    }

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

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listings != null ? listings.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName;
        TextView tvDetails;
        TextView tvPrice;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}