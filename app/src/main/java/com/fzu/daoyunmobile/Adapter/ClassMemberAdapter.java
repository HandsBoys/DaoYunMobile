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
import com.fzu.daoyunmobile.Holder.MemberViewHolder;
import com.fzu.daoyunmobile.R;

import java.util.List;

public class ClassMemberAdapter extends ArrayAdapter<Member> {
    private int resourceId;

    public ClassMemberAdapter(@NonNull Context context, int resource, @NonNull List<Member> memberList) {
        super(context, resource, memberList);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Member member = getItem(position);
        final View view;
        final MemberViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MemberViewHolder();
            viewHolder.ranking = view.findViewById(R.id.No_Tv);
            viewHolder.memberName = view.findViewById(R.id.member_name_Tv);
            viewHolder.stuId = view.findViewById(R.id.member_number_Tv);
            viewHolder.experienceScore = view.findViewById(R.id.experience_score_Tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MemberViewHolder) view.getTag();
        }
        viewHolder.ranking.setText(member.getRanking());
        viewHolder.memberName.setText(member.getMemberName());
        viewHolder.stuId.setText(member.getStu_id());
        viewHolder.experienceScore.setText(member.getExperience_score());
        return view;
    }
}
