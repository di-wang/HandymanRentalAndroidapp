package gatech.edu.project6400.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import gatech.edu.project6400.Model.Reservation;
import gatech.edu.project6400.Model.Superclasses.Tool;
import gatech.edu.project6400.R;

/**
 * Created by Gene Hynson on 4/13/2016.
 */
public class EightLineReservationAdapter extends ArrayAdapter<Reservation> {

    public List<Reservation> reservations;

    public EightLineReservationAdapter(Context context, List<Reservation> reservations) {
        super(context, 0, reservations);
        this.reservations = reservations;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eightlinereservationlayout, parent, false);
        }
        TextView resnum = (TextView) convertView.findViewById(R.id.res_resnum);
        TextView startdate = (TextView) convertView.findViewById(R.id.res_startdate);
        TextView enddate = (TextView) convertView.findViewById(R.id.res_enddate);
        TextView cost = (TextView) convertView.findViewById(R.id.res_cost);
        TextView deposit = (TextView) convertView.findViewById(R.id.res_deposit);
        TextView pickupclerk = (TextView) convertView.findViewById(R.id.res_pickup_clerk);
        TextView dropoffclerk = (TextView) convertView.findViewById(R.id.res_dropoff_clerk);

        ListView listview = (ListView) convertView.findViewById(R.id.res_listview);
        ThreeLineToolAdapter threeLineToolAdapter = new ThreeLineToolAdapter(getContext(), reservations.get(position).tools);
        listview.setAdapter(threeLineToolAdapter);

        Reservation res = reservations.get(position);
        resnum.setText(res.ID);
        startdate.setText(res.startDate.toString());
        enddate.setText(res.endDate.toString());
        cost.setText(String.valueOf(res.estimatedCost));
        deposit.setText(String.valueOf(res.depositMade));
        pickupclerk.setText(res.getPickupClerk());
        dropoffclerk.setText(res.getDropoffClerk());

        return convertView;
    }
}
