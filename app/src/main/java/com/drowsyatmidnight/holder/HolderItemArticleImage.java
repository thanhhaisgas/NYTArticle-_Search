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

public class HolderItemArticleImage extends RecyclerView.ViewHolder {

    @BindView(R.id.image_action_like1)
    public ImageView image_action_like1;
    @BindView(R.id.image_action_share1)
    public ImageView image_action_share1;
    @BindView(R.id.image_card_cover)
    public ImageView image_card_cover;
    @BindView(R.id.text_card_name_1)
    public TextView text_card_name_1;


    public HolderItemArticleImage(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
