package com.example.jsonsqldatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    // PATCH: Because RecyclerView.Adapter in its current form doesn't natively support
    // cursors, we "wrap" a CursorAdapter that will do all teh job for us
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private ViewHolder holder;

    public MyRecyclerAdapter(Context context, Cursor c) {
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                View v = LayoutInflater.from(context)
                        .inflate(R.layout.single_costumer_item, parent, false);
                return v;
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                // Binding operations
                final String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                final String address = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));
                final String code = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                final String phone = cursor.getString(cursor.getColumnIndexOrThrow("PHONE"));

                holder.tvName.setText(name);
                holder.tvDate.setText(address);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //showing popup menu

                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.dialog_layout, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        // final EditText userInput =  promptsView
                        //  .findViewById(R.id.editTextDialogUserInput);

                        TextView namex = promptsView.findViewById(R.id.dialog_name);
                        TextView addressx = promptsView.findViewById(R.id.dialog_address);
                        TextView phonex = promptsView.findViewById(R.id.dialog_phone);
                        TextView idx = promptsView.findViewById(R.id.dialog_id);

                        namex.setText(name);
                        addressx.setText(address);
                        phonex.setText(phone);
                        idx.setText(code);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)

                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    }
                });

            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvDate;

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            tvName = (TextView) itemView.findViewById(R.id.single_item_name);
            tvDate = (TextView) itemView.findViewById(R.id.singel_item_address);
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
holder.setIsRecyclable(false);

        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        holder = new ViewHolder(v);
        return holder;
    }
}
