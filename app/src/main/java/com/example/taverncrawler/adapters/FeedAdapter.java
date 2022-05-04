package com.example.taverncrawler.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public TextView tvDistance;
    public TextView tvClickableOne;
    public TextView tvClickableTwo;
    public Button btnAdd;
    public RatingBar ratingBarReviews;
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
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvClickableOne = (TextView) itemView.findViewById(R.id.tvClickableOne);
            tvClickableTwo = (TextView) itemView.findViewById(R.id.tvClickableTwo);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            ratingBarReviews = (RatingBar) itemView.findViewById(R.id.ratingBarReviews);
        }
        public void bind (Bar bar)
        {
            if(bar.getName().length() >= 35){
                tvName.setText(bar.getName().substring(0, 35) + "...");
            }
            else {
                tvName.setText(bar.getName());
            }
            tvDistance.setText(String.valueOf(bar.getDistance()) + " miles away");
            tvClickableOne.setOnClickListener(new View.OnClickListener() {
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
            tvClickableTwo.setOnClickListener(new View.OnClickListener() {
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
                    CharSequence[] options = {"Yes", "No"};
                    AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity) view.getContext());
                    builder.setTitle("Add to route?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(options[i].equals("Yes")) {
                                List<Double> list = new ArrayList<>();
                                list.add(bar.getLatitude());
                                list.add(bar.getLongitude());
                                String name = bar.getName();
                                //Update LiveData object
                                if(routeViewModel.getSelectedBars().getValue() == null) {
                                    routeViewModel.setSelectedBars(new HashMap<>());
                                }
                                routeViewModel.addBarToSelection(name, list);
                                Toast.makeText((AppCompatActivity) view.getContext(), "Bar successfully added to route", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                            else if (options[i].equals("No")) {
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            });
            ratingBarReviews.setRating((float) bar.getRating());
        }
    }

}
