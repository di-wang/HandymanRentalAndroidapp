package gatech.edu.project6400.View.Clerk;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;
import gatech.edu.project6400.View.Adapters.ThreeLineToolAdapter;

public class RentalReciptDropoff extends AppCompatActivity {

    private ThreeLineToolAdapter toolAdapter;
    private Reservation reservation;

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_recipt_dropoff);
        setTitle("Handy Man Tool Rental");

        Button submit = (Button) findViewById(R.id.dropoff_receipt_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDropOff();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
            if (getIntent().getExtras().get("ReservationID") != null) {
                String json = getIntent().getExtras().get("JSON").toString();
                try {
                    reservation = new Reservation(new JSONObject(json));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (reservation != null) {

                    TextView reservationNum = (TextView) findViewById(R.id.dropoff_receipt_resid);
                    TextView cusName = (TextView) findViewById(R.id.dropoff_receipt_cusname);
                    TextView clerkName = (TextView) findViewById(R.id.dropoff_receipt_clerkname);
                    TextView cc = (TextView) findViewById(R.id.dropoff_receipt_cc);
                    TextView startDate = (TextView) findViewById(R.id.dropoff_receipt_startdate);
                    TextView endDate = (TextView) findViewById(R.id.dropoff_receipt_enddate);
                    TextView deposit = (TextView) findViewById(R.id.dropoff_receipt_deposit);
                    TextView cost = (TextView) findViewById(R.id.dropoff_receipt_cost);

                    reservationNum.setText(reservation.ID);
                    clerkName.setText(reservation.getPickupClerk());
                    cc.setText(reservation.ccNum);
                    startDate.setText(reservation.startDate.toString());
                    endDate.setText(reservation.endDate.toString());
                    deposit.setText(String.valueOf(reservation.depositMade));
                    cost.setText(String.valueOf(reservation.estimatedCost));
                    cusName.setText(reservation.customerName);
                    if (reservation.tools.size() > 0) {
                        toolAdapter = new ThreeLineToolAdapter(this, (ArrayList) reservation.tools);
                        ListView tool_listivew = (ListView) findViewById(R.id.dropoff_listview);
                        tool_listivew.setAdapter(toolAdapter);
                    }
                } else {
                    Toast.makeText(this, "Failed to find reservation!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Navigation error!", Toast.LENGTH_LONG).show();
        }
    }

    private void submitDropOff() {
        EditText signature = (EditText) findViewById(R.id.dropoff_receipt_signature);
        if (!signature.getText().toString().isEmpty()) {
            Intent intent = new Intent(this, DropOffReservation.class);
            intent.putExtra(extraClerkID, ClerkID);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please sign!", Toast.LENGTH_SHORT).show();
        }
    }
}
