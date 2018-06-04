package gatech.edu.project6400.View.Customer;

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
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.LoginActivity;

public class CheckAvailability extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CalendarView startdate;
    CalendarView enddate;
    Customer customer;

    private String extraIDText = "CustomerID";
    private String CustomerID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability);
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

        Button search = (Button) findViewById(R.id.check_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);
            //look customer up in database
            startdate = (CalendarView) findViewById(R.id.check_startdate);
            enddate = (CalendarView) findViewById(R.id.check_enddate);
            Date date = new Date();
            startdate.setDate(date.getTime());
            enddate.setDate(date.getTime());
            startdate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                    startdate.setDate(date.getTime());
                }
            });
            enddate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                    enddate.setDate(date.getTime());
                }
            });
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }

    }

    private void search() {
        RadioButton powerTools = (RadioButton) findViewById(R.id.check_powertools);
        RadioButton constructionTools = (RadioButton) findViewById(R.id.check_construction);
        String toolType;
        if (powerTools.isChecked()) {
            toolType = "Power Tool";
        } else if (constructionTools.isChecked()) {
            toolType = "Construction Tool";
        } else {
            toolType = "Hand Tool";
        }
        Intent intent = new Intent(this, AvailabilityResults.class);
        intent.putExtra(extraIDText, CustomerID);
        intent.putExtra("ToolType", toolType);
        intent.putExtra("StartDate", startdate.getDate());
        intent.putExtra("EndDate", enddate.getDate());
        startActivity(intent);
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
        getMenuInflater().inflate(R.menu.check_availability, menu);
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
