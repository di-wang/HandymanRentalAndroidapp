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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.LoginActivity;

public class SellTool extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_tool);
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

        Button submit = (Button) findViewById(R.id.sell_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellTool();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }

    }

    private void sellTool() {
        EditText toolNum = (EditText) findViewById(R.id.sell_toolNum);
        if (!toolNum.getText().toString().isEmpty()) {

            ToolDBService toolDBService = new ToolDBService();
            toolDBService.sellTool(toolNum.getText().toString(), ClerkID, this);

        } else {
            Toast.makeText(this, "Please enter tool number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getToolSuccess(JSONObject result) {
        TextView price = (TextView) findViewById(R.id.sell_price);
        try {
            price.setText(String.valueOf(result.getDouble("SalePrice")));
            Toast.makeText(this, "Tool sold!", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getToolFailure() {
        Toast.makeText(this, "Tool not found!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.sell_tool, menu);
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
