package com.example.quanlidiemrenluyen.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlidiemrenluyen.Models.HocKyView;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.activities.DanhGiaActivity;

import java.util.List;

public class HocKyAdapter extends RecyclerView.Adapter<HocKyAdapter.ViewHolder> {
    private Context context;
    private List<HocKyView> hocKyViewList;

    public HocKyAdapter(Context context, List<HocKyView> hocKyViewList) {
        this.context = context;
        this.hocKyViewList = hocKyViewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.hocky_item, viewGroup, false);

        return new HocKyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HocKyAdapter.ViewHolder viewHolder, int i) {
        viewHolder.hocky.setText(hocKyViewList.get(i).getHocky());
        viewHolder.note.setText(hocKyViewList.get(i).getNote());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DanhGiaActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return hocKyViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView hocky;
        private TextView note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hocky = itemView.findViewById(R.id.hocky);
            note = itemView.findViewById(R.id.status);

        }
    }
}
