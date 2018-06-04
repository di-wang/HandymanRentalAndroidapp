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
public class ThreeLineToolAdapter extends ArrayAdapter<Tool> {

    public List<Tool> tools;

    public ThreeLineToolAdapter(Context context, List<Tool> tools) {
        super(context, 0, tools);
        this.tools = tools;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threelinetoollayout, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.threeline_title);
        TextView subheader = (TextView) convertView.findViewById(R.id.threeline_subtitle);
        TextView subsubheader = (TextView) convertView.findViewById(R.id.threeline_subsubtitle);
        header.setText(tools.get(position).abbriviatedDescription);
        subheader.setText(tools.get(position).toolType);
        subsubheader.setText(tools.get(position).ID);
        return convertView;
    }
}


