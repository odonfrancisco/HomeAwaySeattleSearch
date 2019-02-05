package c.odonfrancisco.homeawayseattlesearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        Place currentPlace = mPlaceList.get(i);

        viewHolder.icon.setImageBitmap(currentPlace.getIcon());
        viewHolder.placeName.setText(currentPlace.getName());
        viewHolder.placeCategory.setText(currentPlace.getCategory());
        viewHolder.placeDistance.setText(currentPlace.getDistance());
        viewHolder.placeFavorited.setText("" + currentPlace.isFavorited());

    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView placeName;
        TextView placeCategory;
        TextView placeDistance;
        TextView placeFavorited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.place_icon);

            placeName = itemView.findViewById(R.id.place_name);
            placeCategory = itemView.findViewById(R.id.place_category);
            placeDistance = itemView.findViewById(R.id.place_distance);
            placeFavorited = itemView.findViewById(R.id.place_favorited);
        }
    }
}
