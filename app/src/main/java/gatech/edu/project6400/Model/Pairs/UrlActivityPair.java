package gatech.edu.project6400.Model.Pairs;

import android.app.Activity;

import gatech.edu.project6400.View.LoginActivity;

/**
 * Created by Gene Hynson on 4/17/2016.
 */
public class UrlActivityPair {

    public String url;
    public Activity activity;

    public UrlActivityPair(String url, Activity activity) {
        this.url = url;
        this.activity = activity;
    }
}
