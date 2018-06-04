package gatech.edu.project6400.Model;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class Customer {

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String homePhone;
    public String workPhone;
    public String address;
    public List<Reservation> reservations;
    public String ID;
    public int numRentals = 0;

    public Customer(String firstName, String lastName, String email, String password, String homePhone, String workPhone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.homePhone = homePhone;
        this.workPhone = workPhone;
        this.address = address;
        reservations = new ArrayList<>();
        this.ID = email;
    }

    public Customer(String firstName, String lastName, String email, int numRentals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.numRentals = numRentals;
    }

    public Customer(JSONArray result) {
        try {
            this.email = result.getJSONObject(0).getString("Email");
            this.firstName = result.getJSONObject(0).getString("FirstName");
            this.lastName = result.getJSONObject(0).getString("LastName");
            this.homePhone = result.getJSONObject(0).getString("HomePhoneAreaCode");
            this.homePhone += result.getJSONObject(0).getString("HomePhoneLocalNumber");
            this.workPhone = result.getJSONObject(0).getString("WorkPhoneAreaCode");
            this.workPhone += result.getJSONObject(0).getString("WorkPhoneLocalNumber");
            this.address = result.getJSONObject(0).getString("Address");
            this.ID = email;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void parseReservations(JSONArray json_reservations) {
        reservations = new ArrayList<>();
        for (int i = 0; i < json_reservations.length(); i++) {
            try {
                Reservation r = new Reservation(json_reservations.getJSONObject(i));
                reservations.add(r);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
