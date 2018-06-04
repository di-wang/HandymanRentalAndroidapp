package gatech.edu.project6400.Controller.Services.Superclasses;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Exchanger;

import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.Model.ConstructionTool;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.HandTool;
import gatech.edu.project6400.Model.Pairs.JSONActivityPair;
import gatech.edu.project6400.Model.Pairs.UrlActivityPair;
import gatech.edu.project6400.Model.PowerTool;
import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.View.Clerk.AddTool;
import gatech.edu.project6400.View.Clerk.DropOffReservation;
import gatech.edu.project6400.View.Clerk.GenerateReports;
import gatech.edu.project6400.View.Clerk.PickUpReservation;
import gatech.edu.project6400.View.Clerk.RegisterClerk;
import gatech.edu.project6400.View.Clerk.SellTool;
import gatech.edu.project6400.View.Clerk.ServiceOrder;
import gatech.edu.project6400.View.Customer.AvailabilityResults;
import gatech.edu.project6400.View.Customer.MakeReservation;
import gatech.edu.project6400.View.Customer.RegisterActivity;
import gatech.edu.project6400.View.Customer.ReservationFinal;
import gatech.edu.project6400.View.Customer.ReservationSummary;
import gatech.edu.project6400.View.Customer.ViewProfile;
import gatech.edu.project6400.View.LoginActivity;
import gatech.edu.project6400.View.ViewToolDetails;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public abstract class DBAccess {
    //this class could perform the create_tables script

    public static List<Tool> tools = new ArrayList<>();
    public static List<Reservation> reservations = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();
    public static List<Clerk> clerks = new ArrayList<>();

    public void populateDummyData() {
        Customer c1 = new Customer("Joe", "Smith", "js@email.com", "123456", "777-777-7777", "666-666-6666", "123 Blah Ave. Atl GA 30332");
        Customer c2 = new Customer("Sandy", "Cathy", "sc@email.com", "654321", "777-777-7777", "666-666-6666", "123 Blah Ave. Atl GA 30332");
        Customer c3 = new Customer("Louis", "Matthews", "lm@email.com", "456123", "777-777-7777", "666-666-6666", "123 Blah Ave. Atl GA 30332");

        Tool t1 = new HandTool("Screwdriver", 5.45, 3.4, 1.2, "Phillips head screwdriver");
        Tool t2 = new PowerTool("Pressure washer", 500.45, 300.4, 100.2, "Powerful pressure washer.", new ArrayList<String>());
        Tool t3 = new ConstructionTool("Fork lift", 5000.45, 1000.4, 500.2, "Super strong forklift");

        Clerk clerk1 = new Clerk("Tom", "Frank", "234402");

        Reservation r1 = new Reservation(new Date(), new Date(), tools, c1.ID);
        Reservation r2 = new Reservation(new Date(), new Date(), tools, c2.ID);
        Reservation r3 = new Reservation(new Date(), new Date(), tools, c3.ID);
        r1.assignDropoffClerk(clerk1.firstName);
        r1.assignPickupClerk(clerk1.firstName);
        r2.assignDropoffClerk(clerk1.firstName);
        r2.assignPickupClerk(clerk1.firstName);
        r3.assignDropoffClerk(clerk1.firstName);
        r3.assignPickupClerk(clerk1.firstName);

        if (tools.isEmpty()) {
            tools.add(t1);
            tools.add(t2);
            tools.add(t3);
            customers.add(c1);
            customers.add(c2);
            customers.add(c3);
            reservations.add(r1);
            reservations.add(r2);
            reservations.add(r3);
            clerks.add(clerk1);
        }

    }

    public class GetCustomerDataTask extends AsyncTask<UrlActivityPair, Void, UrlActivityPair> {
        public UrlActivityPair doInBackground(UrlActivityPair... pairs) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(pairs[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    //System.out.print(current);
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            if (builder.toString().contains("1")) {
                return new UrlActivityPair("1", pairs[0].activity);
            } else {
                return new UrlActivityPair("0", pairs[0].activity);
            }
        }

        public void onPostExecute(UrlActivityPair pair) {
            if (pair.url.equals("1")) {
                ((LoginActivity) pair.activity).continueCustomerLogin();
            } else {
                ((LoginActivity) pair.activity).failedLogin();
            }
        }
    }

    public class GetClerkDataTask extends AsyncTask<UrlActivityPair, Void, UrlActivityPair> {
        public UrlActivityPair doInBackground(UrlActivityPair... pairs) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(pairs[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            if (builder.toString().contains("1")) {
                return new UrlActivityPair("1", pairs[0].activity);
            } else {
                return new UrlActivityPair("0", pairs[0].activity);
            }
        }

        public void onPostExecute(UrlActivityPair pair) {
            if (pair.url.equals("1")) {
                ((LoginActivity) pair.activity).continueClerkLogin();
            } else {
                ((LoginActivity) pair.activity).failedLogin();
            }
        }
    }

    public class GetCustomerProfileDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... pairs) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(pairs[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                if (result.getInt("success") == 1) {
                    return new JSONActivityPair(pairs[0].activity, result);
                } else {
                    return new JSONActivityPair(null, null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            ViewProfile profileActivity = (ViewProfile) result.activity;
            if (profileActivity != null) {
                profileActivity.continueLoadProfile(result.result);
            }
        }
    }

    public class PutCustomerProfileDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            RegisterActivity registerActivity = (RegisterActivity) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    registerActivity.registerSucessful();
                } else {
                    registerActivity.registerFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutReservationDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            ReservationSummary makeReservation = (ReservationSummary) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    makeReservation.reservationSuccess();
                } else {
                    makeReservation.reservationFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetReservationDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            ReservationFinal makeReservation = (ReservationFinal) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    makeReservation.foundReservationSuccess(result.result);
                } else {
                    makeReservation.foundReservationFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetToolAvailableDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            MakeReservation makeReservation = (MakeReservation) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    makeReservation.updateToolSelection(result.result.getJSONArray("tools"));
                } else {
                    makeReservation.updateToolsFailed();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetToolAvailableDataTask_CheckAvailability extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            AvailabilityResults makeReservation = (AvailabilityResults) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    makeReservation.getAvailableToolsSucess(result.result.getJSONArray("tools"));
                } else {
                    makeReservation.getAvailableToolsFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetToolDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            ViewToolDetails viewToolDetails = (ViewToolDetails) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    viewToolDetails.getToolDetailsSuccess(result.result.getJSONArray("tool").getJSONObject(0));
                } else {
                    viewToolDetails.getToolDetailsFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutToolDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            AddTool addTool = (AddTool) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.toolCreated();
                } else {
                    addTool.toolFailed();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutToolServiceDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            ServiceOrder addTool = (ServiceOrder) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.getToolSuccess();
                } else {
                    addTool.getToolFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutToolSaleDataTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            SellTool addTool = (SellTool) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.getToolSuccess(result.result);
                } else {
                    addTool.getToolFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetReportOneTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            GenerateReports addTool = (GenerateReports) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.calculateReport1(result.result.getJSONArray("tools"));
                } else {
                    addTool.generateReportFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetReportTwoTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            GenerateReports addTool = (GenerateReports) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.calculateReport2(result.result.getJSONArray("customers"));
                } else {
                    addTool.generateReportFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetReportThreeTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            GenerateReports addTool = (GenerateReports) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.calculateReport3(result.result.getJSONArray("clerks"));
                } else {
                    addTool.generateReportFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutDropoffTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            DropOffReservation addTool = (DropOffReservation) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    addTool.dropoffSuccess(result.result);
                } else {
                    addTool.dropoffFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class PutPickupTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            PickUpReservation pickUpReservation = (PickUpReservation) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    pickUpReservation.pickupSuccess(result.result);
                } else {
                    pickUpReservation.pickupFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetDropoffReservationTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            DropOffReservation pickUpReservation = (DropOffReservation) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    pickUpReservation.findReservationSuccess(result.result);
                } else {
                    pickUpReservation.findReservationFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class GetPickupReservationTask extends AsyncTask<UrlActivityPair, Void, JSONActivityPair> {
        public JSONActivityPair doInBackground(UrlActivityPair... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();
            try {
                url = new URL(urls[0].url);
                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    builder.append(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                JSONObject result = new JSONObject(builder.toString());
                return new JSONActivityPair(urls[0].activity, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new JSONActivityPair(null, null);
        }

        public void onPostExecute(JSONActivityPair result) {
            PickUpReservation pickUpReservation = (PickUpReservation) result.activity;
            try {
                if (result.result.getInt("success") == 1) {
                    pickUpReservation.findReservationSuccess(result.result);
                } else {
                    pickUpReservation.findResrvationFailure();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
