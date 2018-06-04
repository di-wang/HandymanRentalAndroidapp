package gatech.edu.project6400.View.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.OneLineToolAdapter;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;
import gatech.edu.project6400.View.LoginActivity;

public class MakeReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CalendarView startdate;
    private CalendarView enddate;
    private List<Tool> selectedTools = new ArrayList<>();
    private ToolDBService toolDBService = new ToolDBService();
    private Spinner spinner;
    private ListView listView;
    private ThreeLineToolAdapter adapter;

    private String extraIDText = "CustomerID";
    private String CustomerID = "";

    private double cost;
    private double deposit;
    private boolean isTruthfulSelection = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);
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

        Button continue_button = (Button) findViewById(R.id.reservation_continue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReservation();
            }
        });

        Button calctotal = (Button) findViewById(R.id.reservation_calctotal);
        calctotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotal();
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.reservation_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioChecked();
            }
        });

        startdate = (CalendarView) findViewById(R.id.reservation_startdate);
        enddate = (CalendarView) findViewById(R.id.reservation_enddate);
        Date date = new Date();
        Date date2 = new Date();
        startdate.setDate(date.getTime());
        enddate.setDate(date2.getTime());
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

        spinner = (Spinner) findViewById(R.id.reservation_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                checkSelectedItem();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);
            //look customer up in database
            if (!CustomerID.isEmpty()) {
                listView = (ListView) findViewById(R.id.reservation_listview);

                adapter = new ThreeLineToolAdapter(this, selectedTools);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to find customer!", Toast.LENGTH_SHORT).show();
            }
        } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    private void getSelectedItem() {
        if (isTruthfulSelection) {
            Tool selectedTool = (Tool) spinner.getSelectedItem();
            selectedTools.add(selectedTool);
            adapter.notifyDataSetChanged();
            listView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, listView.getHeight() + 200));
        } else {
            isTruthfulSelection = true;
        }

    }

    private void checkSelectedItem() {
        if (isTruthfulSelection) {
            if (!selectedTools.contains(spinner.getSelectedItem())) {
                selectedTools.add((Tool)spinner.getSelectedItem());
                adapter.notifyDataSetChanged();
                listView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, listView.getHeight() + 200));
            }
        } else {
            isTruthfulSelection = true;
        }
    }

    private void radioChecked() {
        RadioButton hand_button = (RadioButton) findViewById(R.id.reservation_hand);
        RadioButton power_button = (RadioButton) findViewById(R.id.reservation_power);

        if (hand_button.isChecked()) {
            toolDBService.findAvailableTools("Hand Tool", startdate.getDate(), enddate.getDate(), this);
        } else if (power_button.isChecked()) {
            toolDBService.findAvailableTools("Construction Tool", startdate.getDate(), enddate.getDate(), this);
        } else {
            toolDBService.findAvailableTools("Power Tool", startdate.getDate(), enddate.getDate(), this);
        }
    }

    public void updateToolSelection(JSONArray result) {
        List<Tool> updatedList = new ArrayList<>();
        isTruthfulSelection = false;
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
        Spinner spinner = (Spinner) findViewById(R.id.reservation_spinner);
        OneLineToolAdapter oneLineToolAdapter = new OneLineToolAdapter(this, updatedList);
        spinner.setAdapter(oneLineToolAdapter);
    }

    public void updateToolsFailed() {
        Toast.makeText(this, "No tools found", Toast.LENGTH_SHORT).show();
    }

    private void calculateTotal() {
        Button calctotal = (Button) findViewById(R.id.reservation_calctotal);
        cost = 0;
        deposit = 0;
        for (Tool tool : selectedTools) {
            cost += tool.rentalPrice;
            deposit += tool.depositPrice;
        }
        calctotal.setText("$" + cost);
    }

    private void continueReservation() {
        calculateTotal();
        ArrayList<String> toolabbrs = new ArrayList<>();
        ArrayList<String> toolaIDs = new ArrayList<>();
        for (Tool tool : selectedTools) {
            toolabbrs.add(tool.abbriviatedDescription);
            toolaIDs.add(tool.ID);
        }
        Intent intent = new Intent(this, ReservationSummary.class);
        intent.putExtra(extraIDText, CustomerID);
        intent.putExtra("StartDate", startdate.getDate());
        intent.putExtra("EndDate", enddate.getDate());
        intent.putExtra("Cost", cost);
        intent.putExtra("Deposit", deposit);
        intent.putStringArrayListExtra("ToolIDs", toolaIDs);
        intent.putStringArrayListExtra("ToolAbbrs", toolabbrs);
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
        getMenuInflater().inflate(R.menu.make_reservation, menu);
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
