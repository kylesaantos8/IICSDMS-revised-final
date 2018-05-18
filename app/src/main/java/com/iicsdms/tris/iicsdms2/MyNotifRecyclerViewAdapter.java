package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.iicsdms.tris.iicsdms2.NotificationFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyNotifRecyclerViewAdapter extends RecyclerView.Adapter<MyNotifRecyclerViewAdapter.ViewHolder> {

    private static final String test = "Test";
    List<Map<String,String>> file = new ArrayList<Map<String, String>>();
    ArrayList<NotifPojo> inbox = new ArrayList<>();
    Context ctx;
    FragmentManager fm;
    String linkSeenUpdate, logEmail;
    String mailPage = "via GBiz Mail";
    String title, fullName, desc, notifThread, notifSourceRecipient, notifTable;
    String [] docSplit;
    RetrieveData retrieveData;
//    SwipeRefreshLayout swipeRefreshLayout;

    private ClickListener listener;

    public interface ClickListener {
        public void onItemClicked(int position);
    }


    public MyNotifRecyclerViewAdapter(FragmentManager fm, ArrayList<NotifPojo> inbox, Context ctx, String linkSeenUpdate, String logEmail, ClickListener listener){
        this.inbox = inbox;
        this.ctx = ctx;
        this.fm = fm;
        this.linkSeenUpdate = linkSeenUpdate;
        this.logEmail = logEmail;
        this.listener = listener;
    }

//    public MyNotifRecyclerViewAdapter(FragmentManager fm, ArrayList<NotifPojo> inbox, Context ctx, String logEmail){
//        this.inbox = inbox;
//        this.ctx = ctx;
//        this.fm = fm;
//        this.logEmail = logEmail;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notif, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, ctx, inbox, fm, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        NotifPojo notif = inbox.get(position);
        holder.mItemDesc.setText(notif.getNotifDescription());
//        holder.mItemPage.setText(notif.getNotifPage());
        holder.mItemTime.setText(notif.getNotifTimeStamp());



        if(notif.getNotifFlag() != null)
        {

            if(notif.getNotifFlag().equals("Read")){
                holder.mItemAck.setImageResource(R.drawable.read);
            }
            else if(notif.getNotifFlag().equals("Unread")){
                holder.mItemAck.setImageResource(R.drawable.unread);
            }
        }
        else
        {
            holder.mItemAck.setImageResource(R.drawable.acknowledged);
        }

        if(notif.getNotifPage().equals("Mail Page"))
        {
            holder.mItemPage.setText(mailPage);
        }
    }

    @Override
    public int getItemCount() {return inbox.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mItemDesc;
        public TextView mItemPage;
        public TextView mItemTime;
        public ImageView mItemAck;
        ArrayList<NotifPojo> inbox;
        Context ctx;
        FragmentManager fm;
        private ClickListener listener;

//        ItemClickListener itemClickListener;
//
//        public interface ItemClickListener{
//            void onItemClick(int position);
//        }
//
//        public void setOnItemClickListener(ItemClickListener itemClickListener){
//            this.itemClickListener = itemClickListener;
//        }


        public ViewHolder(View view, Context ctx, ArrayList<NotifPojo> inbox, FragmentManager fm, ClickListener listener) {

            super(view);
            this.inbox = inbox;
            this.ctx = ctx;
            this.fm = fm;
            this.listener = listener;
            view.setOnClickListener(this);

            mItemDesc = (TextView) itemView.findViewById(R.id.itemDesc);
            mItemPage = (TextView) itemView.findViewById(R.id.itemPage);
            mItemTime = (TextView) itemView.findViewById(R.id.itemTime);
            mItemAck = (ImageView) itemView.findViewById(R.id.itemAttach);
        }

        int pos;

        @Override
        public void onClick(View v) {
            SeenStatus seenStatus = new SeenStatus();
            int position = getAdapterPosition();
            NotifPojo notifPojo = this.inbox.get(position);

            if (listener != null) {
                listener.onItemClicked(getPosition());
                pos = getPosition();
                Log.d("TAG", "Item clicked at position " + getPosition());
            }

            if(notifPojo.getNotifPage() != null)
            {

                if(notifPojo.getNotifPage().equals("Incoming Documents Page"))
                {
                    notifTable = "incoming_documents";
                    if(notifPojo.getNotifFlag() != null)
                    {
                        if(notifPojo.getNotifFlag().equals("Unread"))
                        {
                            String update = "Read";
                            seenStatus.seenUpdate(linkSeenUpdate, notifPojo.getNotifId(), update, logEmail);
                        }
                    }

                    desc = notifPojo.getNotifDescription();
                    if(desc.contains(ctx.getString(R.string.notif_inc_upload)))
                    {
                        desc = desc.replaceAll(ctx.getString(R.string.notif_inc_upload), "_");
                        docSplit = desc.split("_");

                        fullName = docSplit[0].trim();
                        title = docSplit[1].trim();
                    }
                    else if(desc.contains(ctx.getString(R.string.notif_note)))
                    {
                        desc = desc.replaceAll(ctx.getString(R.string.notif_note), "_");
                        docSplit = desc.split("_");

                        title = docSplit[0].trim();
                        fullName = docSplit[1].trim();
                    }
                    else if(desc.contains(ctx.getString(R.string.notif_done)))
                    {
                        desc = desc.replaceAll(ctx.getString(R.string.notif_done), "_");
                        docSplit = desc.split("_");

                        title = docSplit[0].trim();
                        fullName = docSplit[1].trim();
                    }
                    else if(desc.contains(ctx.getString(R.string.notif_inc_forward)))
                    {
                        desc = desc.replaceAll(ctx.getString(R.string.notif_inc_forward), "_");
                        docSplit = desc.split("_");

                        fullName = docSplit[0].trim();
                        title = docSplit[1].trim();
                    }

                    retrieveData = new RetrieveData();
                    file = retrieveData.getThreadInfo(v.getContext().getString(R.string.notif_thread_url), notifTable, title, fullName);
                    for (int i = 0; i < file.size(); i++) {
                        Map<String, String> fileMap = file.get(i);
                        notifThread = fileMap.get("thread");
                        notifSourceRecipient = fileMap.get("sourceRecipient");
                    }

                    Intent intent = new Intent(ctx, ViewThread.class);
                    intent.putExtra("FilePojoId", notifPojo.getNotifId());
                    intent.putExtra("FilePojoThread", notifThread);
                    intent.putExtra("FilePojoSourceRecipient", notifSourceRecipient);
                    ctx.startActivity(intent);
                }
                else if(notifPojo.getNotifPage().equals("Outgoing Documents Page"))
                {
                    notifTable = "outgoing_documents";
                    if(notifPojo.getNotifFlag() != null)
                    {
                        if(notifPojo.getNotifFlag().equals("Unread"))
                        {
                            String update = "Read";
                            seenStatus.seenUpdate(linkSeenUpdate, notifPojo.getNotifId(), update, logEmail);
                        }
                    }

                    desc = notifPojo.getNotifDescription();
                    if(desc.contains(ctx.getString(R.string.notif_out_upload)))
                    {
                        desc = desc.replaceAll(ctx.getString(R.string.notif_out_upload), "_");
                        docSplit = desc.split("_");

                        fullName = docSplit[0].trim();
                        title = docSplit[1].trim();
                    }

                    retrieveData = new RetrieveData();
                    file = retrieveData.getThreadInfo(v.getContext().getString(R.string.notif_thread_url), notifTable, title, fullName);
                    for (int i = 0; i < file.size(); i++) {
                        Map<String, String> fileMap = file.get(i);
                        notifThread = fileMap.get("thread");
                        notifSourceRecipient = fileMap.get("sourceRecipient");
                    }

                    Intent intent = new Intent(ctx, ViewThread.class);
                    intent.putExtra("FilePojoThread", notifThread);
                    intent.putExtra("FilePojoSourceRecipient", notifSourceRecipient);
                    ctx.startActivity(intent);
                }
                else if(notifPojo.getNotifPage().equals("Mail Page"))
                {
                    Toast.makeText(this.ctx, "Mail service is currently not yet supported. Please visit GMail App.", Toast.LENGTH_LONG).show();
                }
                else if(notifPojo.getNotifPage().equals("Calendar Page"))
                {
                    Toast.makeText(this.ctx, "Calendar service is currently not yet supported. Please visit web DMS.", Toast.LENGTH_LONG).show();
                }
                else if(notifPojo.getNotifPage().equals("Task Page"))
                {
                    Toast.makeText(this.ctx, "Task service is currently not yet supported. Please visit web DMS.", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
