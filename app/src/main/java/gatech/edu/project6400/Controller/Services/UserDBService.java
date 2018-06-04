package gatech.edu.project6400.Controller.Services;

import android.app.Activity;

import java.util.List;

import gatech.edu.project6400.Controller.Services.Superclasses.DBAccess;
import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Pairs.UrlActivityPair;
import gatech.edu.project6400.View.Customer.ViewProfile;
import gatech.edu.project6400.View.LoginActivity;

/**
 * Created by Gene Hynson on 4/11/2016.
 */
public class UserDBService extends DBAccess {

    public UserDBService() {
        populateDummyData();
    }

    public void registerCustomer(Customer customer, Activity activity) {
        customers.add(customer);

        PutCustomerProfileDataTask putCustomerProfileDataTask = new PutCustomerProfileDataTask();
        String url = "http://10.0.2.2/6400/create_profile.php?Email=" + customer.email +
                "&Password=" + customer.password + "&FirstName=" + customer.firstName +
                "&LastName=" + customer.lastName + "&HomePhoneAreaCode=" + customer.homePhone.substring(0,3) +
                "&HomePhoneLocalNumber=" + customer.homePhone.substring(3, customer.homePhone.length()) +
                "&WorkPhoneAreaCode=" + customer.workPhone.substring(0,3) + "&WorkPhoneLocalNumber=" +
                customer.workPhone.substring(3, customer.workPhone.length()) + "&Address=" + customer.address;
        putCustomerProfileDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void loginCustomer(String email, String password, LoginActivity login) {
        GetCustomerDataTask getCustomerDataTask = new GetCustomerDataTask();
        String url = "http://10.0.2.2/6400/login.php?Login=" + email + "&Password=" + password + "&UserType=Customer";
        getCustomerDataTask.execute(new UrlActivityPair(url, login));
    }

    public void loginClerk(String password, LoginActivity activity) {
        GetClerkDataTask getClerkDataTask = new GetClerkDataTask();
        String url = "http://10.0.2.2/6400/login.php?Login=" + password + "&Password=" + password + "&UserType=Clerk";
        getClerkDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void findCustomerProfile(String ID, Activity activity) {
        GetCustomerProfileDataTask getCustomerProfileDataTask = new GetCustomerProfileDataTask();
        String url = "http://10.0.2.2/6400/view_profile.php?Email=" + ID;
        getCustomerProfileDataTask.execute(new UrlActivityPair(url, activity));
    }

}
