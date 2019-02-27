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

        TextView lblDate = convertView.findViewById(R.id.lblDate);
        TextView lblEventTitle = convertView.findViewById(R.id.lblEventTitle);
        lblDate.setText(attendance.getCreated_at());
        if(attendance.getEvent_a_m() != null){
            lblEventTitle.setText(attendance.getEvent_a_m().getTitle());
        }else if(attendance.getEvent_p_m() != null){
            lblEventTitle.setText(attendance.getEvent_p_m().getTitle());
        }else{
            lblEventTitle.setText("Not Set");
        }

        return convertView;
    }

}
