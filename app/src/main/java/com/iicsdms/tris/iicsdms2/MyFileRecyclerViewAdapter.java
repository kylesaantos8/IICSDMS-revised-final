package com.iicsdms.tris.iicsdms2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.iicsdms.tris.iicsdms2.DocumentsFragment.OnListFragmentInteractionListener;
import com.iicsdms.tris.iicsdms2.dummy.DummyContent.DummyItem;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileRecyclerViewAdapter.ViewHolder> {

    final static String TEST = "test";
    ArrayList<FilePojo> documents = new ArrayList<>();
    Context ctx;
    FragmentManager fm;
    String table, logEmail;

    public MyFileRecyclerViewAdapter(FragmentManager fm, ArrayList<FilePojo> documents, Context ctx, String logEmail){
        this.documents = documents;
        this.ctx = ctx;
        this.fm = fm;
        this.logEmail = logEmail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_documents_file, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,ctx,documents,fm);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        FilePojo file = documents.get(position);
        holder.mFileTitle.setText(file.getTitle());
        holder.mFileCreatedBy.setText(file.getSourceRecipient());
        holder.mFileType.setText(file.getType());
        holder.mFileCategory.setText(file.getCategory());
//        holder.mFileTimeCreated.setText(file.getTimeCreated());
        holder.mFileDesc.setText(file.getDescription());
//        holder.mItemImage.setImageResource(FILE.getExtensionImg());

        if(file.getType().equals("Incoming")){
            holder.mFileToFrom.setText("From: ");
            holder.mFileStatusDesc.setText("Status: ");
            holder.mFileStatus.setText(file.getStatus());
        } else if(file.getType().equals("Outgoing")){
            holder.mFileToFrom.setText("To: ");
            holder.mFileStatusDesc.setText("");
        } else if(file.getType().equals("Personal")){
            holder.mFileToFrom.setText("By: ");
            holder.mFileCreatedBy.setText(file.getCreatedBy());
            holder.mFileStatusDesc.setText("");
        }

        if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".pdf")){
            holder.mItemImage.setImageResource(R.drawable.pdf);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".doc")){
            holder.mItemImage.setImageResource(R.drawable.doc);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".docx")){
            holder.mItemImage.setImageResource(R.drawable.docx);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".jpg")){
            holder.mItemImage.setImageResource(R.drawable.jpg);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".png")){
            holder.mItemImage.setImageResource(R.drawable.png);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".ppt")){
            holder.mItemImage.setImageResource(R.drawable.ppt);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".pptx")){
            holder.mItemImage.setImageResource(R.drawable.pptx);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".rar")){
            holder.mItemImage.setImageResource(R.drawable.rar);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".txt")){
            holder.mItemImage.setImageResource(R.drawable.txt);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".xls")){
            holder.mItemImage.setImageResource(R.drawable.xls);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".xlsx")){
            holder.mItemImage.setImageResource(R.drawable.xlsx);
        }
        else if(file.getFileName().substring(file.getFileName().indexOf(".")).equals(".zip")){
            holder.mItemImage.setImageResource(R.drawable.zip);
        }

    }

    @Override
    public int getItemCount() {return documents.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mFileTitle;
        TextView mFileToFrom;
        TextView mFileCreatedBy;
        TextView mFileCategory;
        TextView mFileType;
//        TextView mFileTimeCreated;
        TextView mFileStatusDesc;
        TextView mFileStatus;
        TextView mFileDesc;
        ImageView mItemImage;
        ArrayList<FilePojo> documents;
        Context ctx;
        FragmentManager fm;

        public ViewHolder(View view, Context ctx, ArrayList<FilePojo> documents, FragmentManager fm) {

            super(view);
            this.documents = documents;
            this.ctx = ctx;
            this.fm = fm;
            view.setOnClickListener(this);

            mFileTitle = (TextView) view.findViewById(R.id.itemTitle);
            mFileToFrom = (TextView) view.findViewById(R.id.itemToFrom);
            mFileCreatedBy = (TextView) view.findViewById(R.id.itemCreatedBy);
            mFileType = (TextView) view.findViewById(R.id.itemType);
            mFileCategory = (TextView) view.findViewById(R.id.itemCategory);
//            mFileTimeCreated = (TextView) view.findViewById(R.id.itemTimeCreated);
            mFileStatusDesc = (TextView) view.findViewById(R.id.itemStatusDesc);
            mFileStatus = (TextView) view.findViewById(R.id.itemStatus);
            mFileDesc = (TextView) view.findViewById(R.id.itemDesc);
            mItemImage = (ImageView) view.findViewById(R.id.itemImage);


        }
        DocumentOnClickDialogFragment documentOnClickDialogFragment;
        @Override
        public void onClick(View v){
            int position = getAdapterPosition();

            FilePojo file = documents.get(position);

            if(file.getType().equals("Personal"))
            {
                Bundle args = new Bundle();

                args.putString("FilePojoTitle", file.getTitle());
                args.putString("FilePojoCategory", file.getCategory());
                args.putString("FilePojoTimeCreated", file.getTimeCreated());
                args.putString("FilePojoFileName", file.getFileName());
                args.putString("FilePojoType", file.getType());
                args.putString("FilePojoDocId", file.getDocId());
                args.putString("FilePojoCreatedBy", file.getCreatedBy());
                args.putString("logEmail", logEmail);

                documentOnClickDialogFragment = new DocumentOnClickDialogFragment();
                documentOnClickDialogFragment.setArguments(args);
                documentOnClickDialogFragment.show(fm, "RecyclerViewAdapter");
            }
            else
            if(file.getType().equals("Incoming"))
            {
                if(file.getThread() != null)
                {
                    Intent intent = new Intent(ctx, ViewThread.class);

                    intent.putExtra("FilePojoTitle", file.getTitle());
                    intent.putExtra("FilePojoSourceRecipient", file.getSourceRecipient());
                    intent.putExtra("FilePojoCategory", file.getCategory());
                    intent.putExtra("FilePojoTimeCreated", file.getTimeCreated());
                    intent.putExtra("FilePojoActionReq", file.getActionReq());
                    intent.putExtra("FilePojoActionDue", file.getActionDue());
                    intent.putExtra("FilePojoStatus", file.getStatus());
                    intent.putExtra("FilePojoReferenceNum", file.getReferenceNum());
                    intent.putExtra("FilePojoFileName", file.getFileName());
                    intent.putExtra("FilePojoType", file.getType());
                    intent.putExtra("FilePojoThread", file.getThread());
                    intent.putExtra("FilePojoDocId", file.getDocId());
                    intent.putExtra("logEmail", logEmail);

                    ctx.startActivity(intent);
                }
                else
                {
                    Bundle args = new Bundle();

                    args.putString("FilePojoTitle",file.getTitle());
                    args.putString("FilePojoType",file.getType());
                    args.putString("FilePojoCategory",file.getCategory());
                    args.putString("FilePojoCreatedBy",file.getCreatedBy());
                    args.putString("FilePojoEmail",file.getEmail());
                    args.putString("FilePojoTimeCreated",file.getTimeCreated());
                    args.putString("FilePojoAcadYear",file.getAcademicYear());
                    args.putString("FilePojoFileName",file.getFileName());
                    args.putString("FilePojoDocId",file.getDocId());
                    args.putString("FilePojoFolderId",file.getFolderId());

                    args.putString("table", "archived_documents");
                    args.putString("logEmail", logEmail);

                    documentOnClickDialogFragment = new DocumentOnClickDialogFragment();
                    documentOnClickDialogFragment.setArguments(args);
                    documentOnClickDialogFragment.show(fm, "RecyclerViewAdapter");
                }
            }
            else
            if(file.getType().equals("Outgoing"))
            {
                if(file.getThread() != null)
                {
                    Intent intent = new Intent(ctx, ViewThread.class);

                    intent.putExtra("FilePojoTitle", file.getTitle());
                    intent.putExtra("FilePojoCategory", file.getCategory());
                    intent.putExtra("FilePojoTimeCreated", file.getTimeCreated());
                    intent.putExtra("FilePojoSourceRecipient", file.getSourceRecipient());
                    intent.putExtra("FilePojoFileName", file.getFileName());
                    intent.putExtra("FilePojoType", file.getType());
                    intent.putExtra("FilePojoThread", file.getThread());
                    intent.putExtra("FilePojoDocId", file.getDocId());
                    intent.putExtra("logEmail", logEmail);

                    ctx.startActivity(intent);
                }
                else
                {
                    Bundle args = new Bundle();

                    args.putString("FilePojoTitle",file.getTitle());
                    args.putString("FilePojoType",file.getType());
                    args.putString("FilePojoCategory",file.getCategory());
                    args.putString("FilePojoCreatedBy",file.getCreatedBy());
                    args.putString("FilePojoEmail",file.getEmail());
                    args.putString("FilePojoTimeCreated",file.getTimeCreated());
                    args.putString("FilePojoAcadYear",file.getAcademicYear());
                    args.putString("FilePojoFileName",file.getFileName());
                    args.putString("FilePojoDocId",file.getDocId());
                    args.putString("FilePojoFolderId",file.getFolderId());

                    args.putString("table", "archived_documents");
                    args.putString("logEmail", logEmail);

                    documentOnClickDialogFragment = new DocumentOnClickDialogFragment();
                    documentOnClickDialogFragment.setArguments(args);
                    documentOnClickDialogFragment.show(fm, "RecyclerViewAdapter");
                }
            }


        }
    }
}