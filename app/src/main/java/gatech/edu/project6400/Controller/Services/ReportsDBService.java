package gatech.edu.project6400.Controller.Services;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gatech.edu.project6400.Controller.Services.Superclasses.DBAccess;
import gatech.edu.project6400.Model.Pairs.UrlActivityPair;

/**
 * Created by Gene Hynson on 4/18/2016.
 */
public class ReportsDBService extends DBAccess {
    public ReportsDBService() {
        populateDummyData();
    }

    public void generateReport1(Date reportDate, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GetReportOneTask getReportOneTask = new GetReportOneTask();
        String url = "http://10.0.2.2/6400/generate_report1.php?ReportDate=" + dateFormat.format(reportDate);
        getReportOneTask.execute(new UrlActivityPair(url, activity));
    }

    public void generateReport2(Date startDate, Date endDate, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GetReportTwoTask getReportTwoTask = new GetReportTwoTask();
        String url = "http://10.0.2.2/6400/generate_report2.php?MonthStart=" + dateFormat.format(startDate) +
                "&MonthEnd=" + dateFormat.format(endDate);
        getReportTwoTask.execute(new UrlActivityPair(url, activity));
    }

    public void generateReport3(Date startDate, Date endDate, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GetReportThreeTask getReportThreeTask = new GetReportThreeTask();
        String url = "http://10.0.2.2/6400/generate_report3.php?MonthStart=" + dateFormat.format(startDate) +
                "&MonthEnd=" + dateFormat.format(endDate);
        getReportThreeTask.execute(new UrlActivityPair(url, activity));
    }
}
