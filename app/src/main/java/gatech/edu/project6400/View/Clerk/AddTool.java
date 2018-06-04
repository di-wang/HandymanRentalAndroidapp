package gatech.edu.project6400.View.Clerk;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Model.ConstructionTool;
import gatech.edu.project6400.Model.HandTool;
import gatech.edu.project6400.Model.PowerTool;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.LoginActivity;

public class AddTool extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<String> accessories = new ArrayList<>();
    ArrayAdapter<String> accessory_adapter;

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tool);
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

        Button accessory = (Button) findViewById(R.id.new_tool_accessory);
        accessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccessory();
            }
        });

        Button newTool = (Button) findViewById(R.id.new_tool_submit);
        newTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewTool();
            }
        });

        accessory_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, (ArrayList) accessories);
        ListView accessory_listivew = (ListView) findViewById(R.id.new_tool_accessories);
        accessory_listivew.setAdapter(accessory_adapter);

        RadioGroup group = (RadioGroup) findViewById(R.id.new_tool_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonSelectionChanged();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    private void radioButtonSelectionChanged() {
        RadioButton power = (RadioButton) findViewById(R.id.new_tool_power);
        TextView accessorytext = (TextView) findViewById(R.id.new_tool_accessory_label);
        EditText accessory = (EditText) findViewById(R.id.new_tool_accessory_text);
        Button accessory_add = (Button) findViewById(R.id.new_tool_accessory);
        if (power.isChecked()) {
            accessory.setVisibility(View.VISIBLE);
            accessory_add.setVisibility(View.VISIBLE);
            accessorytext.setVisibility(View.VISIBLE);
        } else {
            accessory.setVisibility(View.GONE);
            accessory_add.setVisibility(View.GONE);
            accessorytext.setVisibility(View.GONE);
        }
    }

    private void submitNewTool() {
        //create new tool object from fields
        Tool newTool = null;
        RadioButton handTool = (RadioButton) findViewById(R.id.new_tool_hand);
        RadioButton constructionTool = (RadioButton) findViewById(R.id.new_tool_construction);
        EditText abbrdesc = (EditText) findViewById(R.id.new_tool_abbrdesc);
        EditText purchasePrice = (EditText) findViewById(R.id.new_tool_purchaseprice);
        EditText rentalPrice = (EditText) findViewById(R.id.new_tool_rentalprice);
        EditText depositPrice = (EditText) findViewById(R.id.new_tool_deposit);
        EditText fulldesc = (EditText) findViewById(R.id.new_tool_fulldesc);

        if (!abbrdesc.getText().toString().isEmpty() &&
                !purchasePrice.getText().toString().isEmpty() &&
                !depositPrice.getText().toString().isEmpty() &&
                !rentalPrice.getText().toString().isEmpty() &&
                !fulldesc.getText().toString().isEmpty()) {
            String abbrDescString = abbrdesc.getText().toString();
            double purchasePriceDouble = Double.parseDouble(purchasePrice.getText().toString());
            double rentalPriceDouble = Double.parseDouble(rentalPrice.getText().toString());
            double depositPriceDouble = Double.parseDouble(depositPrice.getText().toString());
            String fulldescString = fulldesc.getText().toString();
            if (handTool.isChecked()) {
                newTool = new HandTool(abbrDescString, purchasePriceDouble, rentalPriceDouble, depositPriceDouble, fulldescString);
            } else if (constructionTool.isChecked()) {
                newTool = new ConstructionTool(abbrDescString, purchasePriceDouble, rentalPriceDouble, depositPriceDouble, fulldescString);
            } else {
                newTool = new PowerTool(abbrDescString, purchasePriceDouble, rentalPriceDouble, depositPriceDouble, fulldescString, accessories);
            }
            ToolDBService toolDBService = new ToolDBService();
            toolDBService.addTool(newTool, ClerkID, this);

        } else {
            Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
        }
    }

    public void toolCreated() {
        Toast.makeText(this, "Tool Created!", Toast.LENGTH_SHORT).show();
    }

    public void toolFailed() {
        Toast.makeText(this, "Tool could not be created!", Toast.LENGTH_SHORT).show();
    }

    private void addAccessory() {
        EditText newAccessory = (EditText) findViewById(R.id.new_tool_accessory_text);
        if (!newAccessory.getText().toString().isEmpty()) {
            //don't need to write to db until "submit" is clicked
            accessories.add(newAccessory.getText().toString());
            accessory_adapter.notifyDataSetChanged();
            newAccessory.setText("");
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
        getMenuInflater().inflate(R.menu.add_tool, menu);
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
