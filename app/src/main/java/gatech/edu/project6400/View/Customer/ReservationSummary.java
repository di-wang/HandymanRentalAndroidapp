package gatech.edu.project6400.View.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;

public class ReservationSummary extends AppCompatActivity {

    private Reservation reservation;
    private String extraIDText = "CustomerID";
    private String CustomerID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_summary);
        setTitle("Handy Man Tool Rental");

        Button reset = (Button) findViewById(R.id.reservation_sum_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetReservation();
            }
        });

        Button submit = (Button) findViewById(R.id.reservation_sum_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReservation();
            }
        });

        //get passed info
        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);
            //look customer up in database
            if (getIntent().getExtras().get("StartDate") != null &&
                    getIntent().getExtras().get("EndDate") != null &&
                    getIntent().getExtras().get("Cost") != null &&
                    getIntent().getExtras().get("Deposit") != null &&
                    getIntent().getExtras().get("ToolIDs") != null &&
                    getIntent().getExtras().get("ToolAbbrs") != null) {

                long startdate_var = (long) getIntent().getExtras().get("StartDate");
                long enddate_var = (long) getIntent().getExtras().get("EndDate");
                double cost_var = (double) getIntent().getExtras().get("Cost");
                double deposit_var = (double) getIntent().getExtras().get("Deposit");
                List<String> tools_var = (ArrayList<String>) getIntent().getExtras().get("ToolAbbrs");
                List<String> toolsID_var = (ArrayList<String>) getIntent().getExtras().get("ToolIDs");


                //look up tools in db using ID
                List<Tool> toolList = new ArrayList<>();
                for (String toolabbr : tools_var) {
                    toolList.add(new Tool(toolabbr, toolsID_var.get(tools_var.indexOf(toolabbr))));
                }

                //create the reservation object
                reservation = new Reservation(new Date(startdate_var), new Date(enddate_var), toolList, CustomerID);
                reservation.estimatedCost = cost_var;
                reservation.depositMade = deposit_var;

                //populate ui
                ListView tools = (ListView) findViewById(R.id.summary_listview);
                TextView startdate = (TextView) findViewById(R.id.summary_startdate);
                TextView enddate = (TextView) findViewById(R.id.summary_enddate);
                TextView cost = (TextView) findViewById(R.id.summary_cost);
                TextView deposit = (TextView) findViewById(R.id.summary_deposit);

                ThreeLineToolAdapter adapter = new ThreeLineToolAdapter(this, reservation.tools);
                tools.setAdapter(adapter);
                startdate.setText(reservation.startDate.toString());
                enddate.setText(reservation.endDate.toString());
                cost.setText(String.valueOf(reservation.estimatedCost));
                deposit.setText(String.valueOf(reservation.depositMade));
            } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    private void resetReservation() {
        Intent intent = new Intent(this, MakeReservation.class);
        intent.putExtra(extraIDText, CustomerID);
        startActivity(intent);
    }

    private void submitReservation() {
        ReservationDBService reservationDBService = new ReservationDBService();
        reservationDBService.createReservation(reservation, this);
    }

    public void reservationSuccess() {
        Intent intent = new Intent(this, ReservationFinal.class);
        intent.putExtra(extraIDText, CustomerID);
        intent.putExtra("ReservationID", reservation.ID);
        startActivity(intent);
    }

    public void reservationFailure() {
        Toast.makeText(this, "Can't complete reservation!", Toast.LENGTH_LONG).show();
        resetReservation();
    }
}
