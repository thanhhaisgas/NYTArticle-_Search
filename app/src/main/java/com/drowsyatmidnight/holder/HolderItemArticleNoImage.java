package com.drowsyatmidnight.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drowsyatmidnight.nytarticle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haint on 24/06/2017.
 */

public class HolderItemArticleNoImage extends RecyclerView.ViewHolder {
    @BindView(R.id.image_action_share0)
    public ImageView image_action_share0;
    @BindView(R.id.image_action_like0)
    public ImageView image_action_like0;
    @BindView(R.id.text_card_name_0)
    public TextView text_card_name_0;

    public HolderItemArticleNoImage(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
