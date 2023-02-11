package com.stainley.lab.lab_stainley_c0868582_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.stainley.lab.lab_stainley_c0868582_android.R;
import com.stainley.lab.lab_stainley_c0868582_android.model.Place;
import com.stainley.lab.lab_stainley_c0868582_android.view.MapsActivity;

import java.util.List;

public class PlaceRecylerViewAdapter extends RecyclerView.Adapter<PlaceRecylerViewAdapter.PlaceViewHolder> {

    private List<Place> placeList;
    private Context context;

    public PlaceRecylerViewAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_place, parent, false);

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.localityTxt.setText("City: " + placeList.get(position).getLocality());
        holder.postalCodeTxt.setText("Postal Code: " + placeList.get(position).getPostalCode());
        holder.thoroughfareTxt.setText("Street: " + placeList.get(position).getThoroughfare());

        holder.cardView.setOnClickListener(view -> {
            Intent myPlaceIntent = new Intent(context, MapsActivity.class);
            myPlaceIntent.putExtra("my_place", placeList.get(position));
            context.startActivity(myPlaceIntent);
        });
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView localityTxt;
        private TextView postalCodeTxt;
        private TextView thoroughfareTxt;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            localityTxt = itemView.findViewById(R.id.localityTxt);
            postalCodeTxt = itemView.findViewById(R.id.postalCodeTxt);
            thoroughfareTxt = itemView.findViewById(R.id.thoroughfareTxt);
            cardView = itemView.findViewById(R.id.place_card);
        }
    }
}
