package gatech.edu.project6400.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class Reservation {

    public Date startDate;
    public Date endDate;
    public List<Tool> tools;
    public double estimatedCost = 0;
    public double depositMade = 0;
    public String ID;
    public String customerID;
    public String ccNum;
    public String ccExp;
    private String pickupClerk;
    private String dropOffClerk;
    public String customerName;


    public Reservation(Date startDate, Date endDate, List<Tool> tools, String customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tools = tools;
        this.customerID = customer;
        Random random = new Random();
        long num = random.nextLong();
        if (num < 0) {
            num = num*-1;
        }
        ID = String.valueOf(num);
        ID = ID.substring(0,5);
    }

    public Reservation(JSONObject reservation) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ID = reservation.getString("ReservationNumber");
            tools = new ArrayList<>();
            try {
                //if there is more than one tool...
                JSONArray tools_json = reservation.getJSONArray("Tools");
                for (int j = 0; j < tools_json.length(); j++) {
                    tools.add(new Tool(tools_json.getString(j)));
                }
            } catch (Exception ex) {
                //if there is only one tool...
                try {
                    tools.add(new Tool(reservation.getString("Tools")));
                } catch (Exception e) {
                    //if there are no tools...
                    e.printStackTrace();
                }
            }
            try {
                String startdate_text = reservation.getString("StartDate");
                String enddate_text = reservation.getString("EndDate");
                startDate = formatter.parse(startdate_text);
                endDate = formatter.parse(enddate_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                estimatedCost = reservation.getDouble("RentalPrice");
                depositMade = reservation.getDouble("Deposit");
            } catch (Exception e) {
                try {
                    estimatedCost = reservation.getDouble("TotalRentalPrice");
                    depositMade = reservation.getDouble("TotalDepositRequired");
                } catch (Exception ex) {
                    try {
                        depositMade = reservation.getDouble("DepositHeld");
                        estimatedCost = reservation.getDouble("EstimateRental");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            try {
                pickupClerk = reservation.getString("PickupClerk");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                dropOffClerk = reservation.getString("DropoffClerk");
            } catch (Exception eex) {
                eex.printStackTrace();
            }
            try {
                customerName = reservation.getString("Customer");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ccNum = reservation.getString("CreditCardNumber");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void assignDropoffClerk(String clerk) {
        this.dropOffClerk = clerk;
    }

    public void assignPickupClerk(String clerk) {
        this.pickupClerk = clerk;
    }

    public String getDropoffClerk() {
        return this.dropOffClerk;
    }

    public String getPickupClerk() {
        return this.pickupClerk;
    }
}
