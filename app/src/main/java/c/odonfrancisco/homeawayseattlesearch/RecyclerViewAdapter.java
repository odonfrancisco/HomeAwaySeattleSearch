package c.odonfrancisco.homeawayseattlesearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Place> mPlaceList;

    public RecyclerViewAdapter(Context mContext, ArrayList<Place> mPlaceList) {
        this.mContext = mContext;
        this.mPlaceList = mPlaceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder called");

        final Place currentPlace = mPlaceList.get(i);

//        Should I be calling glide here? Or is it better to save the bitmap to the
//                Place object? I have a feeling its better to save the bitmap to the
//                object but youre the god here Amit. LMK whats up
        Glide.with(mContext)
                .asBitmap()
                .load(currentPlace.getIcon())
                .into(viewHolder.icon);

        viewHolder.placeName.setText(currentPlace.getName());
        viewHolder.placeCategory.setText(currentPlace.getCategory());
        viewHolder.placeDistance.setText(currentPlace.getDistance());
        viewHolder.placeFavorited.setText("" + currentPlace.isFavorited());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, currentPlace.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PlaceDetailsActivity.class);
                Gson gson = new Gson();

                intent.putExtra("currentPlace", gson.toJson(currentPlace));

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(mPlaceList.size()));
        return mPlaceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView placeName;
        TextView placeCategory;
        TextView placeDistance;
        TextView placeFavorited;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.place_icon);

            placeName = itemView.findViewById(R.id.place_name);
            placeCategory = itemView.findViewById(R.id.place_category);
            placeDistance = itemView.findViewById(R.id.place_distance);
            placeFavorited = itemView.findViewById(R.id.place_favorited);

            parentLayout = itemView.findViewById(R.id.recyclerview_layout);
        }
    }
}
