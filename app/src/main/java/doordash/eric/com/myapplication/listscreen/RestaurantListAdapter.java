package doordash.eric.com.myapplication.listscreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import doordash.eric.com.myapplication.R;
import doordash.eric.com.myapplication.network.Restaurant;

/**
 * Created by Eric on 3/16/2018.
 */

class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{
    private final ArrayList<Restaurant> data;

    private final ListScreenPresenter listScreenPresenter;

    public RestaurantListAdapter(List<Restaurant> initialData, ListScreenPresenter listScreenPresenter) {
        if (initialData == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = new ArrayList<>(initialData);
        }

        this.listScreenPresenter = listScreenPresenter;
    }

    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_row_item, parent, false);
        ViewHolder vh = new ViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        listScreenPresenter.bindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void replaceAll(List<Restaurant> restaurantList) {
        if (restaurantList == null) {
            return;
        }
        data.clear();
        data.addAll(restaurantList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.restaurant_name)
        TextView name;

        @BindView(R.id.restaurant_image)
        ImageView restaurantImage;

        @BindView(R.id.restaurant_food_type)
        TextView foodType;

        @BindView(R.id.restaurant_status)
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setName(String restaurantName) {
            name.setText(restaurantName);
        }

        public void setRestaurantImage(String restaurantImageUrl) {
            Glide.with(itemView.getContext())
                    .load(restaurantImageUrl)
                    .into(restaurantImage);
        }

        public void setFoodType(String foodDescription) {
            foodType.setText(foodDescription);
        }

        public void setStatus(String restaurantStatus) {
            status.setText(restaurantStatus);
        }

        public void setOnclickListener(View.OnClickListener onclickListener) {
            itemView.setOnClickListener(onclickListener);
        }
    }
}
