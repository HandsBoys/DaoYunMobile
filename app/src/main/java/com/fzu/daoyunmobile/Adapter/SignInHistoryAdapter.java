package com.fzu.daoyunmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.Entity.SignInHistory;
import com.fzu.daoyunmobile.Holder.SignInHistoryViewHolder;
import com.fzu.daoyunmobile.Holder.SignIngViewHolder;
import com.fzu.daoyunmobile.R;

import java.util.List;

public class SignInHistoryAdapter extends ArrayAdapter<SignInHistory> {
    //课程ID
    private int resourceId;

    public SignInHistoryAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<SignInHistory> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SignInHistory his = getItem(position);
        final View view;
        final SignInHistoryViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new SignInHistoryViewHolder();
            viewHolder.consistTv = view.findViewById(R.id.his_consist_Tv);
            viewHolder.datetypeTv = view.findViewById(R.id.his_datetype_Tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (SignInHistoryViewHolder) view.getTag();
        }
        viewHolder.consistTv.setText(his.getConDate());
        viewHolder.datetypeTv.setText(his.getDateType());

        return view;
    }
}
