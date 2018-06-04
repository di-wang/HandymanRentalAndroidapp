package gatech.edu.project6400.View.Clerk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import gatech.edu.project6400.Controller.Services.ReservationDBService;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

public class RentalReciptPickup extends AppCompatActivity {

    private Reservation reservation;

    private String extraClerkID = "ClerkID";
    private String ClerkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_recipt_pickup);
        setTitle("Handy Man Tool Rental");

        Button submit = (Button) findViewById(R.id.pickup_receipt_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPickup();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(extraClerkID) != null) {
            ClerkID = (String) getIntent().getExtras().get(extraClerkID);
            if (getIntent().getExtras().get("ReservationID") != null) {
                String json = getIntent().getExtras().get("JSON").toString();
                try {
                    JSONObject json_obj = new JSONObject(json);
                    reservation = new Reservation(json_obj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (reservation != null) {
                    TextView reservationNum = (TextView) findViewById(R.id.pickup_receipt_resid);
                    TextView cusName = (TextView) findViewById(R.id.pickup_receipt_cusname);
                    TextView clerkName = (TextView) findViewById(R.id.pickup_receipt_clerkname);
                    TextView cc = (TextView) findViewById(R.id.pickup_receipt_cc);
                    TextView startDate = (TextView) findViewById(R.id.pickup_receipt_startdate);
                    TextView endDate = (TextView) findViewById(R.id.pickup_receipt_enddate);
                    TextView deposit = (TextView) findViewById(R.id.pickup_receipt_deposit);
                    TextView cost = (TextView) findViewById(R.id.pickup_receipt_cost);
                    TextView total = (TextView) findViewById(R.id.pickup_receipt_total);

                    reservationNum.setText(reservation.ID);
                    clerkName.setText(reservation.getPickupClerk());
                    cc.setText(reservation.ccNum);
                    startDate.setText(reservation.startDate.toString());
                    endDate.setText(reservation.endDate.toString());
                    deposit.setText(String.valueOf(reservation.depositMade));
                    cost.setText(String.valueOf(reservation.estimatedCost));
                    total.setText(String.valueOf(reservation.estimatedCost - reservation.depositMade));
                    cusName.setText(reservation.customerName);
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

    private void submitPickup() {
        Intent intent = new Intent(this, PickUpReservation.class);
        intent.putExtra(extraClerkID, ClerkID);
        startActivity(intent);
        finish();
    }
}
