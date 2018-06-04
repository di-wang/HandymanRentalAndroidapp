package gatech.edu.project6400.Controller.Services;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gatech.edu.project6400.Controller.Services.Superclasses.DBAccess;
import gatech.edu.project6400.Model.Pairs.UrlActivityPair;
import gatech.edu.project6400.Model.PowerTool;
import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/11/2016.
 */
public class ToolDBService extends DBAccess {

    public ToolDBService() {
        populateDummyData();
    }

    public void addTool(Tool tool, String clerkID, Activity activity) {
        PutToolDataTask putToolDataTask = new PutToolDataTask();
        String url = "http://10.0.2.2/6400/add_tool.php?ToolID=" + tool.ID + "&ToolType=" +
                tool.toolType.replaceAll(" ", "%20") + "&AbbrDescription=" + tool.abbriviatedDescription + "&FullDescription=" +
                tool.fullDescription +"&PurchasePrice=" + tool.purchasePrice + "&DailyRentalPrice=" + tool.rentalPrice +
                "&Deposit=" + tool.depositPrice + "&AddClerkLogin=" + clerkID;
        if (tool.toolType.equals("Power Tool")) {
            for (String accessory : ((PowerTool)tool).accessories ) {
                url += "&Accessories[]=" + accessory;
            }
        } else {
            url += "&Accessories[]=";
        }
        putToolDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void serviceTool(String toolID, long startdate, long enddate, String cost, String ClerkID, Activity activity) {
        PutToolServiceDataTask putToolServiceDataTask = new PutToolServiceDataTask();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(startdate);
        Date endDate = new Date(enddate);
        String url = "http://10.0.2.2/6400/hold_tool_for_repair.php?ToolID=" + toolID + "&EstimateRepairCost=" + cost +
                "&StartDate=" + dateFormat.format(startDate) + "&EndDate=" + dateFormat.format(endDate) +
                "&HoldClerkLogin=" + ClerkID;
        putToolServiceDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void sellTool(String toolID, String ClerkID, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date saleDate = new Date();
        PutToolSaleDataTask putToolSaleDataTask = new PutToolSaleDataTask();
        String url = "http://10.0.2.2/6400/sell_tool.php?ToolID=" + toolID + "&SellClerkLogin=" +
                ClerkID + "&SaleDate=" + dateFormat.format(saleDate);
        putToolSaleDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void findTool(String ID, Activity activity) {
        GetToolDataTask getToolDataTask = new GetToolDataTask();
        String url = "http://10.0.2.2/6400/view_tool_detail.php?ToolID=" + ID;
        getToolDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void findAvailableTools(String tooltype, long startdate, long enddate, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(startdate);
        Date endDate = new Date(enddate);
        GetToolAvailableDataTask getToolAvailableDataTask = new GetToolAvailableDataTask();
        String url = "http://10.0.2.2/6400/check_availability.php?ToolType=" + tooltype.replaceAll(" ", "%20") + "&StartDate=" + dateFormat.format(startDate) +
                "&EndDate=" + dateFormat.format(endDate);
        getToolAvailableDataTask.execute(new UrlActivityPair(url, activity));
    }

    public void findAvailableTools_CheckAvailability(String tooltype, long startdate, long enddate, Activity activity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(startdate);
        Date endDate = new Date(enddate);
        GetToolAvailableDataTask_CheckAvailability getToolAvailableDataTask = new GetToolAvailableDataTask_CheckAvailability();
        String url = "http://10.0.2.2/6400/check_availability.php?ToolType=" + tooltype.replaceAll(" ", "%20") + "&StartDate=" + dateFormat.format(startDate) +
                "&EndDate=" + dateFormat.format(endDate);
        getToolAvailableDataTask.execute(new UrlActivityPair(url, activity));
    }

}
