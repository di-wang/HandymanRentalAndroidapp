package gatech.edu.project6400.Model;

import java.util.Random;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class Clerk {

    public String firstName;
    public String lastName;
    public String password;
    public String ID;
    public int numPickups = 0;
    public int numDropoffs = 0;
    public int totalRentals = 0;

    public Clerk(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.ID = password;
    }

    public Clerk(String firstName, String lastName, int numPickups, int numDropoffs, int totalRentals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numPickups = numPickups;
        this.numDropoffs = numDropoffs;
        this.totalRentals = totalRentals;
    }
}
