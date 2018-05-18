package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 22/04/2018.
 */

public class ThreadAdapter extends BaseAdapter {

    static final String TAG = "test";
    private List<FilePojo> threadList; //= new ArrayList<FilePojo>();
    private Context context;

    public ThreadAdapter(@NonNull Context context, List<FilePojo> filePojoList) {
        this.threadList = filePojoList;
        this.context = context;
    }

    public int getCount()
    {
        return this.threadList.size();
    }

    public FilePojo getItem(int index)
    {
        return this.threadList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        FilePojo documentThreadObj = threadList.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView title, upBy, upDate, category, fileName, desc;
        TextView refNo, actionReq, actionDue, status;


        if(threadList.get(position).getType().equals("Incoming"))
        {
            v = layoutInflater.inflate(R.layout.list_thread_left, parent, false);

            refNo = (TextView) v.findViewById(R.id.thr_ref_num);
            actionReq = (TextView) v.findViewById(R.id.thr_action_req);
            actionDue = (TextView) v.findViewById(R.id.thr_action_due);
            status = (TextView) v.findViewById(R.id.thr_status);

            refNo.setText(documentThreadObj.getReferenceNum());
            actionReq.setText(documentThreadObj.getActionReq());
            if(documentThreadObj.getActionDue().equals("null") || documentThreadObj.getActionDue() == null)
            {
                actionDue.setText("None");
            }
            else
            {
                actionDue.setText(documentThreadObj.getActionDue());
            }
            status.setText(documentThreadObj.getStatus());

        }
        else
        {
            v = layoutInflater.inflate(R.layout.list_thread_right, parent, false);

            upBy = (TextView) v.findViewById(R.id.thr_up_by);
            fileName = (TextView) v.findViewById(R.id.thr_file_name);
            desc = (TextView) v.findViewById(R.id.thr_desc);

            upBy.setText(documentThreadObj.getCreatedBy().concat(" (").concat(documentThreadObj.getEmail()).concat(")"));
            fileName.setText(documentThreadObj.getFileName());
            desc.setText(documentThreadObj.getDescription());
        }

        title = (TextView) v.findViewById(R.id.thr_title);
        upDate = (TextView) v.findViewById(R.id.thr_up_date);
        category = (TextView) v.findViewById(R.id.thr_cat);

        title.setText(documentThreadObj.getTitle());
        upDate.setText(documentThreadObj.getTimeCreated());
        category.setText(documentThreadObj.getCategory());


        return v;
    }
}
