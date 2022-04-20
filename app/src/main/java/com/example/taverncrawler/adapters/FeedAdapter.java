package com.example.taverncrawler.adapters;

import android.content.Context;
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

import java.util.List;

import okhttp3.Route;

/**
 * In charge of populating the RecyclerView and updating the LiveData with bars the user has selected
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<String> bars;
    private LifecycleOwner owner;
    private List<String> selectedBars;
    private final Context context;
    public static final String TAG = "FeedAdapter";
    public TextView tvName;
    public Button btnAdd;
    private RouteViewModel routeViewModel;

    public FeedAdapter (Context context, List<String> bars, RouteViewModel routeViewModel, LifecycleOwner owner) {
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
        String bar = bars.get(position);
        holder.bind(bar);

    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedBars.add(tvName.getText().toString());
                    //Update LiveData object
                    routeViewModel.setSelectedBars(selectedBars);
                }
            });
        }
        public void bind (String bar) {
            tvName.setText(bar);
        }
    }

}
