package gatech.edu.project6400.View.Clerk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;
import gatech.edu.project6400.View.LoginActivity;
import gatech.edu.project6400.View.ViewToolDetails;

public class PickUpReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ReservationDBService reservationDBService = new ReservationDBService();
    private ThreeLineToolAdapter toolAdapter;
    private Reservation reservation;

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_reservation);
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

        Button viewDetails = (Button) findViewById(R.id.pickup_view_details);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewToolDetails();
            }
        });

        Button complete = (Button) findViewById(R.id.pickup_complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completePickup();
            }
        });

        Button search = (Button) findViewById(R.id.pickup_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchReservation();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
            if (ClerkID.isEmpty()) {
                Toast.makeText(this, "Failed to find clerk!", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    private void searchReservation() {
        EditText resnum = (EditText) findViewById(R.id.pickup_resid);
        reservationDBService.findReservation_Pickup(resnum.getText().toString(), this);
    }

    private void completePickup() {
        if (reservation != null) {
            EditText cc = (EditText) findViewById(R.id.pickup_cc);
            EditText exp = (EditText) findViewById(R.id.pickup_exp);
            reservationDBService.completePickUp(reservation, ClerkID, cc.getText().toString(), exp.getText().toString(), this);
        } else {
            Toast.makeText(this, "Please find a reservation.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewToolDetails() {
        EditText toolNum = (EditText) findViewById(R.id.pickup_toolNum);
        if (!toolNum.getText().toString().isEmpty()) {
            Intent intent = new Intent(this, ViewToolDetails.class);
            intent.putExtra("ToolID", toolNum.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please type a tool number.", Toast.LENGTH_SHORT).show();
        }
    }

    public void findReservationSuccess(JSONObject result) {
        try {
            reservation = new Reservation(result.getJSONArray("reservation").getJSONObject(0));
            JSONArray tools = result.getJSONArray("tool");
            for (int i = 0; i < tools.length(); i++) {
                Tool tool = new Tool(tools.getJSONObject(i).getString("AbbrDescription"), tools.getJSONObject(i).getString("ToolID"));
                reservation.tools.add(tool);
            }
            TextView deposit = (TextView) findViewById(R.id.pickup_deposit);
            TextView cost = (TextView) findViewById(R.id.pickup_cost);
            deposit.setText(String.valueOf(reservation.depositMade));
            cost.setText(String.valueOf(reservation.estimatedCost));
            toolAdapter = new ThreeLineToolAdapter(this, (ArrayList) reservation.tools);
            ListView tool_listivew = (ListView) findViewById(R.id.pickup_listview);
            tool_listivew.setAdapter(toolAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findResrvationFailure() {
        Toast.makeText(this, "No reservation was found.", Toast.LENGTH_SHORT).show();
    }

    public void pickupSuccess(JSONObject result) {
        Intent intent = new Intent(this, RentalReciptPickup.class);
        intent.putExtra(extraClerkID, ClerkID);
        intent.putExtra("ReservationID", reservation.ID);
        intent.putExtra("JSON", result.toString());
        startActivity(intent);
    }

    public void pickupFailure() {
        Toast.makeText(this, "Completing Pickup failed!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.pick_up_reservation, menu);
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
        if (id == R.id.nav_pickup) {
            intent = new Intent(this, PickUpReservation.class);
            intent.putExtra(extraClerkID, ClerkID);
        } else if (id == R.id.nav_dropoff) {
            intent = new Intent(this, DropOffReservation.class);
            intent.putExtra(extraClerkID, ClerkID);
        } else if (id == R.id.nav_service_order) {
            intent = new Intent(this, ServiceOrder.class);
            intent.putExtra(extraClerkID, ClerkID);
        } else if (id == R.id.nav_add_tool) {
            intent = new Intent(this, AddTool.class);
            intent.putExtra(extraClerkID, ClerkID);
        } else if (id == R.id.nav_sell_tool) {
            intent = new Intent(this, SellTool.class);
            intent.putExtra(extraClerkID, ClerkID);
        } else if (id == R.id.nav_reports) {
            intent = new Intent(this, GenerateReports.class);
            intent.putExtra(extraClerkID, ClerkID);
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
