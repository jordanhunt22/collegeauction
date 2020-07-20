package com.example.collegeauction.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.collegeauction.Activities.ListingDetailsActivity;
import com.example.collegeauction.Miscellaneous.DateManipulator;
import com.example.collegeauction.Miscellaneous.TimeFormatter;
import com.example.collegeauction.Models.Bid;
import com.example.collegeauction.Models.Listing;
import com.example.collegeauction.R;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.ViewHolder> {

    private Context context;
    private List<Listing> purchases;

    public PurchasesAdapter(Context context, List<Listing> purchases){
        this.context = context;
        this.purchases = purchases;
    }

    @NonNull
    @Override
    public PurchasesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase, parent, false);
        return new PurchasesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesAdapter.ViewHolder holder, int position) {
        Listing purchase = purchases.get(position);
        holder.bind(purchase);
    }

    @Override
    public int getItemCount() { return purchases.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Create variables for all of the views
        private TextView tvName;
        private TextView tvTime;
        private TextView tvBid;
        private ImageView ivImage;

        private Listing purchase;

        private Runnable updater;
        final Handler timerHandler = new Handler();
        private DateManipulator dateManipulator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Resolve all of the view elements
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvBid = itemView.findViewById(R.id.tvBid);
            ivImage = itemView.findViewById(R.id.ivImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    // Gets item position
                    int position = getAdapterPosition();
                    // Make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION){
                        // Get the listing at the position, this won't work if the class is static
                        purchase = purchases.get(position);

                        // Create a new intent
                        Intent intent = new Intent(context, ListingDetailsActivity.class);

                        // Serialize the Post using parceler, use its short name as a key
                        intent.putExtra(Listing.class.getSimpleName(), Parcels.wrap(purchase));

                        if (currentUser.getObjectId().equals(purchase.getUser().getObjectId())){
                            // Open the seller's detail view
                            Toast.makeText(context, "You are the seller!", Toast.LENGTH_SHORT).show();
                            return;
                            // I still need to make a seller detail view
                            // intent.putExtra("isSeller", true);
                        }
                        else{
                            // Open the buyer's detail view
                            Toast.makeText(context, "You already bought this item!", Toast.LENGTH_SHORT).show();
                            return;
                            // open the buyer's detail view
                            // Intent.putExtra("isSeller", false);
                        }
                        // Start the DetailsActivity
                        // context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(final Listing purchase) {

            // Bind the listing data to the view elements
            tvName.setText(purchase.getName());

            ParseFile image = purchase.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .transform(new CenterCrop())
                        .into(ivImage);
            } else {
                //ivImage.setImageResource(android.R.color.transparent);
                Glide.with(context)
                        .load(R.drawable.ic_launcher_background)
                        .transform(new CenterCrop())
                        .into(ivImage);
            }

            String timeStamp = TimeFormatter
                    .getTimeDifference(Objects
                            .requireNonNull(purchase.getDate("expiresAt"))
                            .toString());
            // tvTime.setText("Expired " + timeStamp + " ago");
            // Create instance of date manipulator
            if (purchase.getExpireTime() != null && !purchase.getBoolean("isSold")) {
                dateManipulator = new DateManipulator(purchase.getExpireTime());
                updater = new Runnable() {
                    @Override
                    public void run() {
                        String date = dateManipulator.getDate();
                        tvTime.setText(date);
                        timerHandler.postDelayed(updater,1000);
                    }
                };
                timerHandler.post(updater);
            }
            // If the listing is sold,
            else{
                tvTime.setText("Expired " + TimeFormatter
                        .getTimeDifference(purchase.getDate("expiresAt").toString()) + " ago");
            }
        }
    }

    // Clean all elements of the recyclerview
    public void clear() {
        purchases.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Listing> allPurchases) {
        purchases.addAll(allPurchases);
        notifyDataSetChanged();
    }
}

