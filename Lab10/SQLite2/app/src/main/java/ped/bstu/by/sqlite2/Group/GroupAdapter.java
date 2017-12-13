package ped.bstu.by.sqlite2.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ped.bstu.by.sqlite2.R;

/**
 * Created by Egor on 12.11.2017.
 */

public class GroupAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Group> objects;

    public GroupAdapter(Context context, ArrayList<Group> groups) {
        ctx = context;
        objects = groups;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_group, parent, false);
        }
        if(getCount() != 0) {
            Group g = getGroup(position);
            ((TextView) view.findViewById(R.id.faculty)).setText(g.getFaculty().toString());
            ((TextView) view.findViewById(R.id.course)).setText(String.valueOf(g.getCourse()));
            ((TextView) view.findViewById(R.id.name)).setText(g.getName().toString());
            if(g.getHead() != null)
                ((TextView) view.findViewById(R.id.head)).setText(g.getHead().toString());
        } else {
            ((TextView) view.findViewById(R.id.faculty)).setText("");
            ((TextView) view.findViewById(R.id.course)).setText("");
            ((TextView) view.findViewById(R.id.name)).setText("");
            ((TextView) view.findViewById(R.id.head)).setText("");
        }

        return view;
    }

    Group getGroup(int position) {
        return ((Group) getItem(position));
    }
}
