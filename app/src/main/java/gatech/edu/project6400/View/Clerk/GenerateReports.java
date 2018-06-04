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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ReportsDBService;
import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.FiveLineToolAdapter;
import gatech.edu.project6400.View.Adapters.FourLineClerkAdapter;
import gatech.edu.project6400.View.Adapters.ThreeLineCustomerAdapter;
import gatech.edu.project6400.View.LoginActivity;

public class GenerateReports extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";
    private ReportsDBService reportsDBService = new ReportsDBService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reports);
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

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Report 1");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Report 2");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Report 3");
        tabHost.addTab(tabSpec);

        if (getIntent().getExtras() != null && getIntent().getExtras().get("ClerkID") != null) {
            ClerkID = (String) getIntent().getExtras().get("ClerkID");

            reportsDBService.generateReport1(new Date(), this);
        } else {
            Toast.makeText(this, "Navigation failure!", Toast.LENGTH_LONG);
        }

    }

    public void calculateReport1(JSONArray result) {
        List<Tool> tools = new ArrayList<>();
        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject tool = result.getJSONObject(i);
                Tool t = new Tool(tool.getString("AbbrDescription"), tool.getString("ToolID"));
                t.rentalProfit = tool.getDouble("RentalProfit");
                t.totalCosts = tool.getDouble("CostOfTool");
                t.totalProfit = tool.getDouble("TotalProfit");
                tools.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.tab1_listview);
        FiveLineToolAdapter fiveLineToolAdapter = new FiveLineToolAdapter(this, tools);
        listView.setAdapter(fiveLineToolAdapter);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        reportsDBService.generateReport2(c.getTime(), new Date(), this);
    }

    public void calculateReport2(JSONArray result) {
        List<Customer> customers = new ArrayList<>();
        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject customer = result.getJSONObject(i);
                String[] names = customer.getString("Name").split(" ");
                String email = customer.getString("Email");
                int rentals = customer.getInt("Rentals");
                Customer c = new Customer(names[0], names[1], email, rentals);
                customers.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.tab2_listview);
        ThreeLineCustomerAdapter threeLineCustomerAdapter = new ThreeLineCustomerAdapter(this, customers);
        listView.setAdapter(threeLineCustomerAdapter);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        reportsDBService.generateReport3(c.getTime(), new Date(), this);
    }

    public void calculateReport3(JSONArray result) {
        List<Clerk> clerks = new ArrayList<>();
        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject clerk = result.getJSONObject(i);
                String[] names = clerk.getString("Name").split(" ");
                int pickups = clerk.getInt("Pickups");
                int dropoffs = clerk.getInt("Dropoffs");
                int rentals = clerk.getInt("Total");
                Clerk c = new Clerk(names[0], names[1], pickups, dropoffs, rentals);
                clerks.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.tab3_listview);
        FourLineClerkAdapter fourLineClerkAdapter = new FourLineClerkAdapter(this, clerks);
        listView.setAdapter(fourLineClerkAdapter);

    }

    public void generateReportFailure() {
        Toast.makeText(this, "Failed to generate a report!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.generate_reports, menu);
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
