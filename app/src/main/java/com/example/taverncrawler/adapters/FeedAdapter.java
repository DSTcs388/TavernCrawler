package com.example.taverncrawler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taverncrawler.R;
import com.example.taverncrawler.fragments.FeedFragment;
import com.example.taverncrawler.viewmodels.RouteViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Route;

/**
 * In charge of populating the RecyclerView and updating the LiveData with bars the user has selected
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private HashMap<String, List<Double>> bars;
    private LifecycleOwner owner;
    private HashMap<String, List<Double>> selectedBars;
    private final Context context;
    public static final String TAG = "FeedAdapter";
    public TextView tvName;
    public TextView tvLat;
    public TextView tvLong;
    public Button btnAdd;
    private RouteViewModel routeViewModel;

    public FeedAdapter (Context context, HashMap<String, List<Double>> bars, RouteViewModel routeViewModel, LifecycleOwner owner) {
        this.context = context;
        this.bars = bars;
        this.routeViewModel = routeViewModel;
        this.owner = owner;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> barNames = new ArrayList<>(bars.keySet());
        List<List<Double>> barCoords = new ArrayList<>(bars.values());
        String barName = barNames.get(position);
        Double barLat = barCoords.get(position).get(0);
        Double barLong = barCoords.get(position).get(1);
        holder.bind(barName, barLat, barLong);

    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLat = (TextView) itemView.findViewById(R.id.tvLat);
            tvLong = (TextView) itemView.findViewById(R.id.tvLong);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
        }
        public void bind (String barName, Double barLat, Double barLong)
        {
            tvName.setText(barName);
            tvLat.setText(String.valueOf(barLat));
            tvLong.setText(String.valueOf(barLong));
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Double> list = new ArrayList<>();
                    list.add(barLat);
                    list.add(barLong);
                    String name = barName;
                    //Update LiveData object
                    if(routeViewModel.getSelectedBars().getValue() == null) {
                        routeViewModel.setSelectedBars(new HashMap<>());
                    }
                    routeViewModel.addBarToSelection(name, list);
                }
            });
        }
    }

}
