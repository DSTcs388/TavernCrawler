package com.example.taverncrawler.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taverncrawler.R;
import com.example.taverncrawler.fragments.DetailFragment;
import com.example.taverncrawler.fragments.FeedFragment;
import com.example.taverncrawler.models.Bar;
import com.example.taverncrawler.viewmodels.RouteViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Route;

/**
 * In charge of populating the RecyclerView and updating the LiveData with bars the user has selected
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<Bar> bars;
    private LifecycleOwner owner;
    private HashMap<String, List<Double>> selectedBars;
    private final Context context;
    public static final String TAG = "FeedAdapter";
    public TextView tvName;
    public Button btnAdd;
    private RouteViewModel routeViewModel;

    public FeedAdapter (Context context, List<Bar> bars, RouteViewModel routeViewModel, LifecycleOwner owner) {
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
        Bar bar = bars.get(position);
        holder.bind(bar);

    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public void addAll(List<Bar> list) {
        bars.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
        }
        public void bind (Bar bar)
        {
            tvName.setText(bar.getName());
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    DetailFragment detailFragment = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bar", Parcels.wrap(bar));
                    detailFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Double> list = new ArrayList<>();
                    list.add(bar.getLatitude());
                    list.add(bar.getLongitude());
                    String name = bar.getName();
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
