package dev.kevin.app.attendance.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.kevin.app.attendance.R;
import dev.kevin.app.attendance.models.Attendance;

public class AttendanceRowAdapter extends ArrayAdapter<Attendance> {

    public AttendanceRowAdapter(@NonNull Context context, ArrayList<Attendance> attendances) {
        super(context, 0, attendances);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Attendance attendance = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_row, parent,false);
        }

        String type;

        if(attendance.getType() == 1){
            type = "Time IN";
        }else{
            type = "Time OUT";
        }

        String session;

        if(attendance.getSession().toUpperCase().equals("AM")){
            session = "Morning";
        }else if(attendance.getSession().toUpperCase().equals("PM")){
            session = "Afternoon";
        }else{
            session = "Business Meeting";
        }

        TextView lblDate = convertView.findViewById(R.id.lblDate);
        TextView lblEventTitle = convertView.findViewById(R.id.lblEventTitle);
        lblDate.setText(attendance.getCreated_at());
        lblEventTitle.setText(attendance.getEvnt().getTitle() + " - Day " + attendance.getDay() + " " + session + "\n" + type);
        return convertView;
    }

}
