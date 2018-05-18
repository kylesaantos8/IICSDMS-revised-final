package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tris on 27/02/2018.
 */

public class MyInviteRecyclerViewAdapter extends RecyclerView.Adapter<MyInviteRecyclerViewAdapter.ViewHolder> {

    ArrayList<InvitePojo> invitations = new ArrayList<>();
    Context ctx;
    FragmentManager fm;
    String email;

    public MyInviteRecyclerViewAdapter(FragmentManager fm, ArrayList<InvitePojo> inv, Context ctx, String logEmail){
        this.invitations = inv;
        this.ctx = ctx;
        this.fm = fm;
        this.email = logEmail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_invitations_invite, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,ctx, invitations, fm);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        InvitePojo invite = invitations.get(position);
        holder.mEventTitle.setText(invite.getEventTitle());
        holder.mEventVenue.setText(invite.getEventLocation());
        holder.mStartTime.setText(invite.getEventStart());
        holder.mEndTime.setText(invite.getEventEnd());
        holder.mEventDetails.setText(invite.getEventDescription());
        holder.mEventTime.setText(invite.getEventDateResponse());
//        holder.mEventStatus.setImageResource(INVI.getEventStatus());
        if(invite.getEventStatus().equals("Accepted")){
            holder.mEventStatus.setImageResource(R.drawable.accepted);
        }
        else if(invite.getEventStatus().equals("Declined")){
            holder.mEventStatus.setImageResource(R.drawable.declined);
        }
        else if(invite.getEventStatus().equals("No Response")){
            holder.mEventStatus.setImageResource(R.drawable.noresponse);
        }
    }

    @Override
    public int getItemCount() {return invitations.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mEventTitle;
        public TextView mEventVenue;
        public TextView mStartTime;
        public TextView mEndTime;
        public TextView mEventDetails;
        public TextView mEventResponse;
        public TextView mEventTime;
        public ImageView mEventStatus;
        ArrayList<InvitePojo> invitations;
        Context ctx;
        FragmentManager fm;

        public ViewHolder(View view, Context ctx, ArrayList<InvitePojo> invitations, FragmentManager fm){

            super(view);
            this.invitations = invitations;
            this.ctx = ctx;
            this.fm = fm;
            view.setOnClickListener(this);

            mEventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            mEventVenue = (TextView) itemView.findViewById(R.id.eventVenue);
            mStartTime = (TextView) itemView.findViewById(R.id.startTime);
            mEndTime = (TextView) itemView.findViewById(R.id.endTime);
            mEventDetails = (TextView) itemView.findViewById(R.id.eventDetails);
            //mEventResponse = (TextView) itemView.findViewById(R.id.eventResponse);
            mEventTime = (TextView) itemView.findViewById(R.id.eventTime);
            mEventStatus = (ImageView) itemView.findViewById(R.id.eventStatus);
//            itemView.setOnClickListener(this);
        }

        public void onClick(View view){
            int position = getAdapterPosition();
            InvitePojo invitePojo = this.invitations.get(position);
            Intent intent = new Intent(this.ctx, ViewInviteActivity.class);

//            Bundle args = new Bundle();
//            args.putString("inviteTitleArgs", invitePojo.getEventTitle());

            intent.putExtra("logEmail", email);

            intent.putExtra("InvitePojo", invitePojo);
            intent.putExtra("inviteId", invitePojo.getEventId());
            intent.putExtra("inviteTitle", invitePojo.getEventTitle());
            intent.putExtra("inviteLoc", invitePojo.getEventLocation());
            intent.putExtra("inviteAllDay", invitePojo.getEventAllDay());
            intent.putExtra("inviteStart", invitePojo.getEventStart());
            intent.putExtra("inviteEnd", invitePojo.getEventEnd());
            intent.putExtra("inviteDescription", invitePojo.getEventDescription());
            intent.putExtra("inviteCreatedBy", invitePojo.getEventCreatedBy());
            intent.putExtra("inviteStatus", invitePojo.getEventStatus());
            intent.putExtra("inviteResponse", invitePojo.getEventResponse());
            intent.putExtra("inviteDateResponse", invitePojo.getEventDateResponse());
            this.ctx.startActivity(intent);
        }

    }
}
