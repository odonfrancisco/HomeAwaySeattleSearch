package c.odonfrancisco.homeawayseattlesearch;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlaceListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Place> mPlaceList;

    // Constructor
    // Why is it telling me access can be private even though I'm using it in another activity?

    //use a damn rv!
    public PlaceListAdapter(Context mContext, ArrayList<Place> mPlaceList){
        this.mContext = mContext;
        this.mPlaceList = mPlaceList;
    }

    @Override
    public int getCount() {
        return mPlaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlaceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // What is View.inflate doing?
        View v = View.inflate(mContext, R.layout.individual_list_item, null);
        TextView placeName = v.findViewById(R.id.place_name);
        TextView placeCategory = v.findViewById(R.id.place_category);
        TextView placeDistance = v.findViewById(R.id.place_distance);
        TextView placeFavorited = v.findViewById(R.id.place_favorited);
        ImageView placeIcon = v.findViewById(R.id.place_icon);

        Place currentPlace = mPlaceList.get(position);

        placeName.setText(currentPlace.getName());
        placeCategory.setText(currentPlace.getCategory());

        String message = mContext.getResources().getString(R.string.distance_from_center_seattle, currentPlace.getDistance());
        placeDistance.setText(message);
        placeIcon.setImageBitmap(currentPlace.getIcon());

        if(mPlaceList.get(position).isFavorited()){
            placeFavorited.setText("Your favorite!!");
        } else {
            placeFavorited.setText("Add to favorites");
        }

        // Save product id to tag
        v.setTag(mPlaceList.get(position).getId());

        return v;
    }
}
