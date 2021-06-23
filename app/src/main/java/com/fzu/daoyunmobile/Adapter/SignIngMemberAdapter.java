package com.fzu.daoyunmobile.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.Holder.SignIngViewHolder;
import com.fzu.daoyunmobile.R;

import java.util.List;


/**
 * 签到中的成员适配器
 */
public class SignIngMemberAdapter extends ArrayAdapter<Member> {
    //课程ID
    private int resourceId;

    public SignIngMemberAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Member> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Member member = getItem(position);
        final View view;
        final SignIngViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new SignIngViewHolder();
            viewHolder.ranking = view.findViewById(R.id.No_Tv);
            viewHolder.memberName = view.findViewById(R.id.member_name_Tv);
            viewHolder.stuId = view.findViewById(R.id.member_number_Tv);
            viewHolder.signinDate = view.findViewById(R.id.signin_Datetime_Tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (SignIngViewHolder) view.getTag();
        }
        viewHolder.ranking.setText(member.getRanking());
        viewHolder.memberName.setText(member.getMemberName());
        viewHolder.stuId.setText(member.getStu_id());
        viewHolder.signinDate.setText(member.getSingnInDate());
        return view;
    }
}
