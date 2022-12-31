package com.example.aplikasikalkulator;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;



public class AdapterRecycleView extends RecyclerView.Adapter<AdapterRecycleView.ViewHolder> {

    private int revIndex;
    ArrayList<item> listRecycle = new ArrayList<item>();
    SharedPreferences pref;
    Context context;
    Gson gson = new GsonBuilder().create();
    ArrayList<item> sementara;
    View Rootview;
    RecyclerView inirec;




    public AdapterRecycleView(ArrayList<item> list, Context context) {
        this.listRecycle = list;
        this.context = context;
        pref = context.getSharedPreferences(context.getString(R.string.shared_pref), Context.MODE_PRIVATE);


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // gawe ngentukke activity sing arep di recycle . . .

        Context konteks = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(konteks);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.activity_recycle,parent, false));


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        revIndex = getItemCount() - (position+1);
        //Log.i(TAG, "ini nilai" + revIndex);
        item iniItem = listRecycle.get(revIndex);
        String iniString = iniItem.keString();
        // iki di outputke ....
        holder.iniHistory.setText(iniString);

        Rootview = holder.itemView;//
        // dibawah ini adalah onclick listener buat tiap item yang adal di dalam recycle view
        //Tapi cara dibawah ini adalah cara yang tidak efisien, ada cara yg lebih efisien, tapi karena agak ruwet dan dikejar deadline pakai ini dulu saja.. :v
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //Log.i(TAG, "onLongClick: " + (getItemCount() - (position+1)));
                String tamp = pref.getString(context.getString(R.string.data_simpanan), "[]");
                sementara = gson.fromJson(tamp, new TypeToken<ArrayList<item>>(){}.getType());
                if (sementara == null) sementara = new ArrayList<item>();

                sementara.remove(getItemCount() - (position+1));

                tamp = gson.toJson(sementara);
                pref.edit().putString(context.getString(R.string.data_simpanan), tamp).apply();


                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);


                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return listRecycle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView iniHistory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iniHistory = (TextView) itemView.findViewById(R.id.iniHistory);

        }



    }


}
