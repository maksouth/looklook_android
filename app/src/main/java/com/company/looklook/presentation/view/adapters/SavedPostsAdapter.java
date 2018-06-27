package com.company.looklook.presentation.view.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.looklook.R;
import com.company.looklook.domain.model.core.Post;
import com.company.looklook.presentation.view.ui.LookApplication;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.media.CamcorderProfile.get;

/**
 * Created by maksouth on 21.01.18.
 */
public class SavedPostsAdapter extends RecyclerView.Adapter<SavedPostsAdapter.ViewHolder> {

    private static final String TAG = LookApplication.TAG_PREFIX +
            SavedPostsAdapter.class.getSimpleName();
    private static final String LIKE = " like";
    private static final String LIKES = " likes";
    private static final String DISLIKE = " dislike";
    private static final String DISLIKES = " dislikes";
    private static final String ZERO = "no";
    private static final String DAYS = " d";
    private static final String WEEKS = " w";
    private static final String MONTHS = " m";
    private static final String DATE_FORMAT = "dd.MM.yy";
    private static final String AGO = " ago";
    private static final String TODAY = "today";

    private List<Post> posts = new ArrayList<>();

    public SavedPostsAdapter(List<Post> posts){
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void updateItem(int position, int likes, int dislikes) {
        Post post = posts.get(position);
        post.setLikes(likes);
        post.setDislikes(dislikes);
        notifyItemChanged(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_saved_post_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.setImage(post.getImageUrl());

        int likes = post.getLikes();
        String likesMessage;
        if (likes == 0) {
            likesMessage = ZERO + LIKES;
        } else if (likes == 1) {
            likesMessage = likes + LIKE;
        } else {
            likesMessage = likes + LIKES;
        }
        holder.setLikes(likesMessage);

        int dislikes = post.getDislikes();
        String dislikesMessage;
        if (dislikes == 0) {
            dislikesMessage = ZERO + DISLIKES;
        } else if (dislikes == 1) {
            dislikesMessage = dislikes + DISLIKE;
        } else {
            dislikesMessage = dislikes + DISLIKES;
        }
        holder.setDislikes(dislikesMessage);

        String timeMessage;
        Date postDate = new Date(post.getTimestamp());
        Date currentDate = new Date();
        long diff = currentDate.getTime() - postDate.getTime();

        long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
        if (daysDiff == 0) {
            timeMessage = TODAY;
        } else if (daysDiff < 7) {
            timeMessage = daysDiff + DAYS + AGO;
        } else if (daysDiff < 35) {
            timeMessage = daysDiff/7 + WEEKS + AGO;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            timeMessage = sdf.format(postDate);
        }

        holder.setTime(timeMessage);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView likesLabel;
        TextView dislikesLabel;
        TextView timeLabel;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            likesLabel = view.findViewById(R.id.likes_label);
            dislikesLabel = view.findViewById(R.id.dislikes_label);
            timeLabel = view.findViewById(R.id.time_label);
            image = view.findViewById(R.id.image);
        }

        public void setLikes(String likes) {
            likesLabel.setText(likes);
        }

        public void setDislikes(String dislikes) {
            dislikesLabel.setText(dislikes);
        }

        public void setTime(String label) {
            timeLabel.setText(label);
        }

        public void setImage(String imagePath) {
            Log.d(TAG, "Saved image path: " + imagePath);

//            Glide.with(itemView)
//                    .load(imagePath)
//                    .into(image);

            if(isLocalImage(imagePath)) {
                Uri uri = Uri.parse(imagePath);
                Picasso.with(itemView.getContext())
                        .load(uri)
                        .into(image);
            } else {
                Picasso.with(itemView.getContext())
                        .load(imagePath)
                        .into(image);
            }
        }

        private boolean isLocalImage(String imagePath){
            boolean result = !imagePath.startsWith("http");
            Log.d(TAG, imagePath + " is local image: " + result);
            return result;
        }
    }

}
