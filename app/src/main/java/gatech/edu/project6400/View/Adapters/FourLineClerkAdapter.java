package gatech.edu.project6400.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

/**
 * Created by Gene Hynson on 4/12/2016.
 */
public class FourLineClerkAdapter extends ArrayAdapter<Clerk> {

    public List<Clerk> clerks;

    public FourLineClerkAdapter(Context context, List<Clerk> clerks) {
        super(context, 0, clerks);
        this.clerks = clerks;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fourlineclerklayout, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.tab3_name);
        TextView subheader = (TextView) convertView.findViewById(R.id.tab3_pickups);
        TextView subsubheader = (TextView) convertView.findViewById(R.id.tab3_dropoffs);
        TextView total = (TextView) convertView.findViewById(R.id.tab3_total);

        header.setText(clerks.get(position).firstName + " " + clerks.get(position).lastName);
        subheader.setText(String.valueOf(clerks.get(position).numPickups));
        subsubheader.setText(String.valueOf(clerks.get(position).numDropoffs));
        total.setText(String.valueOf(clerks.get(position).totalRentals));
        return convertView;
    }
}


