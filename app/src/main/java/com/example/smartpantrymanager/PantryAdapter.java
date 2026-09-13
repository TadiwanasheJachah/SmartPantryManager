package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems;
    private final OnPantryItemActionListener listener;

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    public PantryAdapter(
            List<PantryItem> pantryItems,
            OnPantryItemActionListener listener) {

        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.tvItemName.setText(item.getName());

        String quantity =
                formatQuantity(item.getQuantity()) + " " + item.getUnit();

        holder.tvItemQuantity.setText(quantity);

        String expiryDate = item.getExpiryDate();

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            holder.tvItemExpiry.setVisibility(View.GONE);
        } else {
            holder.tvItemExpiry.setVisibility(View.VISIBLE);
            holder.tvItemExpiry.setText("Expires: " + expiryDate);
        }

        holder.btnEditItem.setOnClickListener(
                view -> listener.onEdit(item)
        );

        holder.btnDeleteItem.setOnClickListener(
                view -> listener.onDelete(item)
        );
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public void setPantryItems(List<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
        notifyDataSetChanged();
    }

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemExpiry;
        Button btnEditItem;
        Button btnDeleteItem;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnEditItem = itemView.findViewById(R.id.btnEditItem);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
