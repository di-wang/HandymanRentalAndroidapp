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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;
import gatech.edu.project6400.View.LoginActivity;
import gatech.edu.project6400.View.ViewToolDetails;

public class AvailabilityResults extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Tool> tools = new ArrayList<>();
    ToolDBService toolDBService = new ToolDBService();

    private String extraIDText = "CustomerID";
    private String CustomerID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Handy Man Tool Rental");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Button viewDetails = (Button) findViewById(R.id.avail_results_viewdetails);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findToolDetails();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);
            //get search information
            if (getIntent().getExtras().get("ToolType") != null &&
                    getIntent().getExtras().get("StartDate") != null &&
                    getIntent().getExtras().get("EndDate") != null) {
                String toolType = (String) getIntent().getExtras().get("ToolType");
                long startDate = (long) getIntent().getExtras().get("StartDate");
                long endDate = (long) getIntent().getExtras().get("EndDate");
                //search database for tools
                toolDBService.findAvailableTools_CheckAvailability(toolType, startDate, endDate, this);

            } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    public void getAvailableToolsSucess(JSONArray result) {
        List<Tool> updatedList = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            try {
                Tool tool = new Tool(result.getJSONObject(i).getString("AbbrDescription"));
                tool.ID = result.getJSONObject(i).getString("ToolID");
                tool.rentalPrice = result.getJSONObject(i).getDouble("DailyRentalPrice");
                tool.depositPrice = result.getJSONObject(i).getDouble("Deposit");
                updatedList.add(tool);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        tools = updatedList;
        ListView tool_list = (ListView) findViewById(R.id.avail_results_listview);
        ThreeLineToolAdapter adapter = new ThreeLineToolAdapter(this, tools);
        tool_list.setAdapter(adapter);
    }

    public void getAvailableToolsFailure() {
        Toast.makeText(this, "Could not find tools", Toast.LENGTH_SHORT).show();
    }

    private void findToolDetails() {
        EditText toolnum = (EditText) findViewById(R.id.avail_results_toolnum);
        if (!toolnum.getText().toString().isEmpty()) {
            Intent intent = new Intent(this, ViewToolDetails.class);
            intent.putExtra("ToolID", toolnum.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tool ID required!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.availability_results, menu);
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
