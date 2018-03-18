package doordash.eric.com.myapplication.common;

import android.app.Activity;
import android.widget.Toast;

import doordash.eric.com.myapplication.R;

/**
 * Created by Eric on 3/17/2018.
 */

public abstract class BaseActivity extends Activity {
    public void showNoConnectivity() {
        Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_LONG).show();

    }

    public void errorOccurredWhenRefreshing() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }
}
