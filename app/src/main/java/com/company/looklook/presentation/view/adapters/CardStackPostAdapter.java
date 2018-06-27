package com.company.looklook.presentation.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.company.looklook.R;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.domain.model.core.SimplePost;
import com.squareup.picasso.Picasso;

/**
 * Created by maksouth on 23.01.18.
 */

public class CardStackPostAdapter extends ArrayAdapter<SimplePost>{

    public CardStackPostAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.card_explore_post_swipe, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SimplePost post = getItem(position);
        if (post != null)
            viewHolder.setImage(post.getImageUrl());

        return convertView;
    }

    private static class ViewHolder {
        View view;
        ImageView image;

        public ViewHolder(View view) {
            this.view = view;
            image = view.findViewById(R.id.image);
        }

        public void setImage(String imagePath) {
            Picasso.with(view.getContext())
                    .load(imagePath)
                    .into(image);
        }

    }

}
