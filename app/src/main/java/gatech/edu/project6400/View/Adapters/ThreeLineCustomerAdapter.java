package gatech.edu.project6400.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

/**
 * Created by Gene Hynson on 4/12/2016.
 */
public class ThreeLineCustomerAdapter extends ArrayAdapter<Customer> {

    public List<Customer> customers;

    public ThreeLineCustomerAdapter(Context context, List<Customer> customers) {
        super(context, 0, customers);
        this.customers = customers;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threelinecustomerlayout, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.tab2_name);
        TextView subheader = (TextView) convertView.findViewById(R.id.tab2_email);
        TextView subsubheader = (TextView) convertView.findViewById(R.id.tab2_num);

        header.setText(customers.get(position).firstName + " " + customers.get(position).lastName);
        subheader.setText(customers.get(position).email);
        subsubheader.setText(String.valueOf(customers.get(position).numRentals));
        return convertView;
    }
}


