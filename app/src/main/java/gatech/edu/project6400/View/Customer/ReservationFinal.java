package gatech.edu.project6400.View.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;

public class ReservationFinal extends AppCompatActivity {

    private String extraIDText = "CustomerID";
    private String CustomerID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_final);
        setTitle("Handy Man Tool Rental");

        Button done = (Button) findViewById(R.id.reservation_final_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishReservation();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraIDText) != null) {
            CustomerID = (String) getIntent().getExtras().get(extraIDText);

            if (getIntent().getExtras().get("ReservationID") != null) {
                ReservationDBService reservationDBService = new ReservationDBService();
                reservationDBService.findReservation((String) getIntent().getExtras().get("ReservationID"), this);
            } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    public void foundReservationSuccess(JSONObject result) {
        TextView resid = (TextView) findViewById(R.id.final_resid);
        ListView listView = (ListView) findViewById(R.id.final_listview);
        TextView startdate = (TextView) findViewById(R.id.final_startdate);
        TextView enddate = (TextView) findViewById(R.id.final_enddate);
        TextView cost = (TextView) findViewById(R.id.final_cost);
        TextView deposit = (TextView) findViewById(R.id.final_deposit);

        try {
            JSONObject reservation_json = result.getJSONArray("reservation").getJSONObject(0);
            JSONArray tools_json = result.getJSONArray("tool");
            List<Tool> tools = new ArrayList<>();
            for (int i = 0; i < tools_json.length(); i++) {
                String ID = tools_json.getJSONObject(i).getString("ToolID");
                String abbr = tools_json.getJSONObject(i).getString("AbbrDescription");
                tools.add(new Tool(abbr, ID));
            }
            resid.setText(reservation_json.getString("ReservationNumber"));
            ThreeLineToolAdapter adapter = new ThreeLineToolAdapter(this, tools);
            listView.setAdapter(adapter);

            startdate.setText(reservation_json.getString("StartDate"));
            enddate.setText(reservation_json.getString("EndDate"));
            cost.setText(String.valueOf(reservation_json.getString("TotalRentalPrice")));
            deposit.setText(String.valueOf(reservation_json.get("TotalDepositRequired")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void foundReservationFailure() {
        Toast.makeText(this, "Reservation complete but not found!", Toast.LENGTH_LONG).show();
    }

    private void finishReservation() {
        Intent intent = new Intent(this, ViewProfile.class);
        intent.putExtra(extraIDText, CustomerID);
        startActivity(intent);
        finish();
    }
}
