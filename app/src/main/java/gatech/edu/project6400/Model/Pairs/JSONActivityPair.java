package gatech.edu.project6400.Model.Pairs;

import android.app.Activity;

import org.json.JSONObject;

import gatech.edu.project6400.View.Customer.ViewProfile;

/**
 * Created by Gene Hynson on 4/17/2016.
 */
public class JSONActivityPair {

    public Activity activity;
    public JSONObject result;

    public JSONActivityPair(Activity activity, JSONObject result) {
        this.activity = activity;
        this.result = result;
    }
}
