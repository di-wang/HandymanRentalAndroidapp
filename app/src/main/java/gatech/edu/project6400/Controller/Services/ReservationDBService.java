package gatech.edu.project6400.Controller.Services;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gatech.edu.project6400.Controller.Services.Superclasses.DBAccess;
import gatech.edu.project6400.Model.Pairs.UrlActivityPair;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/11/2016.
 */
public class ReservationDBService extends DBAccess {

    public ReservationDBService() {
        populateDummyData();
    }

    public void findReservation(String ID, Activity activity) {
        GetReservationDataTask getReservationDataTask = new GetReservationDataTask();
        String url = "http://10.0.2.2/6400/reservation_summary.php?ReservationNumber=" + ID;
        getReservationDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void createReservation(Reservation reservation, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PutReservationDataTask putReservationDataTask = new PutReservationDataTask();
        String url = "http://10.0.2.2/6400/insert_reservation.php?ReservationNumber=" + reservation.ID + "&StartDate=" +
                dateFormat.format(reservation.startDate) + "&EndDate=" + dateFormat.format(reservation.endDate) +
                "&CustomerLogin=" + reservation.customerID;
        for (Tool tool : reservation.tools) {
            url += "&Tools[]=" + tool.ID;
        }
        putReservationDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void completeDropOff(Reservation reservation, String ClerkID, Activity activity) {
        PutDropoffTask putDropoffTask = new PutDropoffTask();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dropoffDate = new Date();
        String url = "http://10.0.2.2/6400/drop_off.php?ReservationNumber=" + reservation.ID + "&DropoffDate=" +
                dateFormat.format(dropoffDate) + "&DropoffClerkLogin=" + ClerkID;
        putDropoffTask.execute(new UrlActivityPair(url, activity));
    }

    public void completePickUp(Reservation reservation, String ClerkID, String cc, String exp, Activity activity){
        PutPickupTask putPickupTask = new PutPickupTask();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date pickupDate = new Date();
        String url = "http://10.0.2.2/6400/pick_up.php?PickupClerkLogin=" + ClerkID + "&PickupDate=" +
                dateFormat.format(pickupDate) + "&CreditCardNumber=" + cc + "&CreditCardExpirationDate=" +
                exp + "&ReservationNumber=" + reservation.ID;
        putPickupTask.execute(new UrlActivityPair(url, activity));
    }

    public void findReservation_Pickup(String ID, Activity activity) {
        GetPickupReservationTask getReservationDataTask = new GetPickupReservationTask();
        String url = "http://10.0.2.2/6400/reservation_summary.php?ReservationNumber=" + ID;
        getReservationDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void findReservation_Dropoff(String ID, Activity activity) {
        GetDropoffReservationTask getReservationDataTask = new GetDropoffReservationTask();
        String url = "http://10.0.2.2/6400/reservation_summary.php?ReservationNumber=" + ID;
        getReservationDataTask.execute(new UrlActivityPair(url, activity));
    }
}
