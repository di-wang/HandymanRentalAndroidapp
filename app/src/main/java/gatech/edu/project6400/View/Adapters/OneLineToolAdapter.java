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
 * Created by Gene Hynson on 4/14/2016.
 */
public class OneLineToolAdapter extends ArrayAdapter<Tool> {

    public List<Tool> tools;

    public OneLineToolAdapter(Context context, List<Tool> tools) {
        super(context, 0, tools);
        this.tools = tools;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.onelinetoollayout, parent, false);
        }
        TextView num = (TextView) convertView.findViewById(R.id.oneline_num);
        TextView header = (TextView) convertView.findViewById(R.id.oneline_description);
        header.setText(tools.get(position).abbriviatedDescription);
        num.setText(position+1 + ".");
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.onelinetoollayout, parent, false);
        }
        TextView num = (TextView) convertView.findViewById(R.id.oneline_num);
        TextView header = (TextView) convertView.findViewById(R.id.oneline_description);
        header.setText(tools.get(position).abbriviatedDescription);
        num.setText(position+1 + ".");
        return convertView;
    }
}
