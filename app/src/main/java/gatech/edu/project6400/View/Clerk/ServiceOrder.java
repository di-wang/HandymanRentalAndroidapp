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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.LoginActivity;

public class ServiceOrder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CalendarView startdate;
    CalendarView enddate;

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order);
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

        Button submit = (Button) findViewById(R.id.service_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitServiceOrder();
            }
        });

        startdate = (CalendarView) findViewById(R.id.service_startdate);
        enddate = (CalendarView) findViewById(R.id.service_enddate);
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

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }

    }

    private void submitServiceOrder() {
        EditText toolNum = (EditText) findViewById(R.id.service_toolNum);
        startdate = (CalendarView) findViewById(R.id.service_startdate);
        enddate = (CalendarView) findViewById(R.id.service_enddate);
        EditText cost = (EditText) findViewById(R.id.service_cost);

        if (!toolNum.getText().toString().isEmpty() &&
                !cost.getText().toString().isEmpty()) {

            ToolDBService toolDBService = new ToolDBService();
            toolDBService.serviceTool(toolNum.getText().toString(), startdate.getDate(), enddate.getDate(),
                    cost.getText().toString(), ClerkID, this);

        } else {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getToolSuccess() {
        Button submit = (Button) findViewById(R.id.service_submit);
        submit.setEnabled(false);
        submit.setText("Submitted");
        Toast.makeText(this, "Service Order submitted!", Toast.LENGTH_SHORT).show();
    }

    public void getToolFailure() {
        Toast.makeText(this, "Service Order request failed!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.service_order, menu);
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
