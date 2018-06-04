package gatech.edu.project6400.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gatech.edu.project6400.Controller.Services.ToolDBService;
import gatech.edu.project6400.Model.PowerTool;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

public class ViewToolDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tool_details);

        Button done = (Button) findViewById(R.id.details_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get("ToolID") != null) {
            String ToolID = (String) getIntent().getExtras().get("ToolID");
            ToolDBService toolDBService = new ToolDBService();
            toolDBService.findTool(ToolID, this);
        } else {
            Toast.makeText(this, "Navigation failure!", Toast.LENGTH_LONG).show();
            goBack();
        }

    }

    public void getToolDetailsSuccess(JSONObject result) {
        TextView abbr = (TextView) findViewById(R.id.details_abbr);
        TextView price = (TextView) findViewById(R.id.details_price);
        TextView rental = (TextView) findViewById(R.id.details_rental);
        TextView deposit = (TextView) findViewById(R.id.details_deposit);
        TextView desc = (TextView) findViewById(R.id.details_desc);
        TextView type = (TextView) findViewById(R.id.details_type);
        try {
            abbr.setText(result.getString("AbbrDescription"));
            price.setText(String.valueOf(result.getDouble("PurchasePrice")));
            rental.setText(String.valueOf(result.getDouble("DailyRentalPrice")));
            deposit.setText(String.valueOf(result.getDouble("Deposit")));
            desc.setText(result.getString("FullDescription"));
            type.setText(result.getString("ToolType").replaceAll("%20", " "));

            if (result.getString("ToolType").equals("Power%20Tool") || result.get("ToolType").equals("Power Tool")) {
                ListView accessories = (ListView) findViewById(R.id.details_listview);
                List<String> accessories_text = new ArrayList<>();
                try {
                    for (int i = 0; i < result.getJSONArray("Accessories").length(); i++) {
                        accessories_text.add(result.getJSONArray("Accessories").getString(i));
                    }
                } catch (Exception e) {
                    accessories_text.add(result.getString("Accessories"));
                }
                ArrayAdapter<String> accessory_adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, (ArrayList) accessories_text);
                accessories.setAdapter(accessory_adapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getToolDetailsFailure() {
        Toast.makeText(this, "Tool not found!", Toast.LENGTH_LONG).show();
        goBack();
    }

    private void goBack() {
        finish();
    }
}
