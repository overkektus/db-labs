package ped.bstu.by.sqlite2.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ped.bstu.by.sqlite2.Group.Group;
import ped.bstu.by.sqlite2.R;

/**
 * Created by Egor on 12.11.2017.
 */

public class StudentAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Student> objects;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        ctx = context;
        objects = students;
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
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Student g = getStudent(position);
        //((TextView) view.findViewById(R.id.faculty)).setText(g.getFaculty().toString());

        return view;
    }

    Student getStudent(int position) {
        return ((Student) getItem(position));
    }
}
