package doordash.eric.com.myapplication.singlerestaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import doordash.eric.com.myapplication.DoordashLiteApplication;
import doordash.eric.com.myapplication.R;
import doordash.eric.com.myapplication.common.BaseActivity;

public class SingleRestaurantActivity extends BaseActivity implements SingleRestaurantView {

    public static final String EXTRA_RESTAURANT_ID = "EXTRA_RESTAURANT_ID";

    private String restaurantId;

    @BindView(R.id.single_restaurant_food_description)
    TextView foodDescription;

    @BindView(R.id.single_restaurant_image)
    ImageView restaurantImage;

    @BindView(R.id.single_restaurant_name)
    TextView name;

    @BindView(R.id.single_restaurant_status)
    TextView status;

    @Inject
    SingleRestaurantPresenter presenter;

    public static Intent startSingleRestaurant(Context context, String id) {
        Intent intent = new Intent(context, SingleRestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant_activity);
        restaurantId = getIntent().getStringExtra(EXTRA_RESTAURANT_ID);

        ButterKnife.bind(this);

        ((DoordashLiteApplication) getApplication()).component().inject(this);

        presenter.bindView(this, restaurantId);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResumed(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewPaused();
    }

    @Override
    public void setRestaurantName(String restaurantName) {
        name.setText(getString(R.string.single_restaurant_name, restaurantName));
    }

    @Override
    public void setStatus(String restaurantStatus) {
        status.setText(getString(R.string.single_restaurant_status, restaurantStatus));
    }

    @Override
    public void setFoodDescription(String restaurantFoodDescription) {
        foodDescription.setText(getString(R.string.single_restaurant_food_description, restaurantFoodDescription));
    }

    @Override
    public void setRestaurantImage(String url) {
        Glide.with(this)
                .load(url)
                .into(restaurantImage);
    }
}
