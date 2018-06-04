package gatech.edu.project6400.View.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.EightLineReservationAdapter;
import gatech.edu.project6400.View.LoginActivity;

public class ViewProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String extraIDText = "CustomerID";
    private String CustomerID = "";
    private List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Handy Man Tool Rental");

        //get user id from extras
        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);
            //look customer up in database
            UserDBService userDBService = new UserDBService();
            userDBService.findCustomerProfile(CustomerID, this);
        } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }

    }

    public void continueLoadProfile(JSONObject object) {
        Customer customer = null;
        try {
            customer = new Customer(object.getJSONArray("customer"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (customer != null) {
            //populate ui
            TextView email = (TextView) findViewById(R.id.profile_email);
            TextView name = (TextView) findViewById(R.id.profile_name);
            TextView work = (TextView) findViewById(R.id.profile_workphone);
            TextView home = (TextView) findViewById(R.id.profile_homephone);
            TextView address = (TextView) findViewById(R.id.profile_address);
            email.setText(customer.email);
            name.setText(customer.firstName + " " + customer.lastName);
            work.setText(customer.workPhone);
            home.setText(customer.homePhone);
            address.setText(customer.address);

            try {
                customer.parseReservations(object.getJSONArray("reservations"));
                reservations = customer.reservations;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (reservations != null) {
                ListView reservations_view = (ListView) findViewById(R.id.profile_listview);
                EightLineReservationAdapter reservationAdapter = new EightLineReservationAdapter(getBaseContext(), reservations);
                reservations_view.setAdapter(reservationAdapter);

            } else {
                Toast.makeText(this, "Failed to find reservations!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to find customer!", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_view_profile) {
            intent = new Intent(this, ViewProfile.class);
            intent.putExtra(extraIDText, CustomerID);

        } else if (id == R.id.nav_check_tool) {
            intent = new Intent(this, CheckAvailability.class);
            intent.putExtra(extraIDText, CustomerID);

        } else if (id == R.id.nav_make_reservation) {
            intent = new Intent(this, MakeReservation.class);
            intent.putExtra(extraIDText, CustomerID);
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
