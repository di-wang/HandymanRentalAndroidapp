package gatech.edu.project6400.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

/**
 * Created by Gene Hynson on 4/12/2016.
 */
public class FiveLineToolAdapter extends ArrayAdapter<Tool> {

    public List<Tool> tools;

    public FiveLineToolAdapter(Context context, List<Tool> tools) {
        super(context, 0, tools);
        this.tools = tools;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fivelinetoollayout, parent, false);
        }
        TextView toolID = (TextView) convertView.findViewById(R.id.tab1_toolID);
        TextView abbr = (TextView) convertView.findViewById(R.id.tab1_abbr);
        TextView rentalprofit = (TextView) convertView.findViewById(R.id.tab1_rentalprofit);
        TextView cost = (TextView) convertView.findViewById(R.id.tab1_cost);
        TextView profit = (TextView) convertView.findViewById(R.id.tab1_profit);

        toolID.setText(tools.get(position).ID);
        abbr.setText(tools.get(position).abbriviatedDescription);
        double rentalProfitNum = tools.get(position).rentalProfit;
        rentalprofit.setText(String.valueOf(rentalProfitNum));
        double costNum = tools.get(position).totalCosts;
        cost.setText(String.valueOf(costNum));
        double profitNum = tools.get(position).rentalProfit;
        profit.setText(String.valueOf(profitNum));
        return convertView;
    }
}


