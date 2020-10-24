package com.king.myacceleratorfirebasedatabaseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ImageViewHolder> {
    private Context mContext;
    private List<User> mUploads;
    private OnItemClickListener mListener;

    public CustomAdapter(Context mContext, List<User> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_layout,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final User uploadCurrent = mUploads.get(position);

        holder.mTvName.setText(uploadCurrent.getName());
        holder.mTvPhone.setText(uploadCurrent.getPhone());
        holder.mTvMail.setText(uploadCurrent.getEmail());
        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("DELETING USER");
                builder.setMessage("Are you sure you want to delete?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+uploadCurrent.getTime());
                        ref.removeValue();
                        message("SUCCESS","Record deleted successfully");
                    }
                });
                builder.create().show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView mTvName, mTvPhone,mTvMail, mTvDelete;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvMail = itemView.findViewById(R.id.tv_mail);
            mTvDelete = itemView.findViewById(R.id.tv_delete);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                //Get the position of the clicked item
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
        // Handle Menu Items
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1,"Verify code");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                //Get the position of the clicked item
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onVerifyClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);

        void onVerifyClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setSearchOperation(List<User> newList){
        mUploads = new ArrayList<>();
        mUploads.addAll(newList);
        notifyDataSetChanged();
    }

    public void message(String title, String message){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}