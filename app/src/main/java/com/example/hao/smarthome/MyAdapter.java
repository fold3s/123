package com.example.hao.smarthome;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.bluetooth.BluetoothClass;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<String> mDataset;
    public ArrayList<Integer> mIconset;
    public static int a;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView img;

        public ViewHolder(View v) {
            super(v);
            img=(ImageView)v.findViewById(R.id.icon);
            txtHeader = (TextView) v.findViewById(R.id.device);
            txtFooter = (TextView) v.findViewById(R.id.s_time);
        }
    }

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset, ArrayList<Integer> myIconset) {
            mDataset = myDataset;
            mIconset = myIconset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Calendar c= Calendar.getInstance();
        final String name = mDataset.get(position);
        holder.img.setImageResource(mIconset.get(position));
        holder.txtHeader.setText(mDataset.get(position));
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });

        holder.txtFooter.setText(c.getTime().toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
