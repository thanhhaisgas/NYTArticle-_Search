package com.drowsyatmidnight.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.drowsyatmidnight.holder.HolderItemArticleImage;
import com.drowsyatmidnight.holder.HolderItemArticleNoImage;
import com.drowsyatmidnight.model.Article;
import com.drowsyatmidnight.model.Multimedium;
import com.drowsyatmidnight.nytarticle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haint on 24/06/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int No_Image = 0;
    private static final int Has_Image = 1;
    private final  List<Article> articles;
    private final Context context;
    private Listenner listener;

    public interface Listenner{
        void onLoadMore();
    }

    public void setListener(Listenner listener) {
        this.listener = listener;
    }

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    private boolean hasImageAt(int position) {
        Article article = articles.get(position);
        return article.getMultimedia() !=null && article.getMultimedia().size() > 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(hasImageAt(position))
            return Has_Image;
        else return No_Image;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Has_Image)
            return new HolderItemArticleImage(LayoutInflater.from(context).
                    inflate(R.layout.item_article_has_image, parent, false));
        else
            return new HolderItemArticleNoImage(LayoutInflater.from(context)
                    .inflate(R.layout.item_article_no_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        Log.d("adapter123", article.getSnippet());
        if (holder instanceof HolderItemArticleImage) {
            bindViewArticleHasImage((HolderItemArticleImage) holder, article, position);
        }
        else{
            if (holder instanceof  HolderItemArticleNoImage){
                bindViewArticleNoImage((HolderItemArticleNoImage) holder, article, position);
            }
        }

        if(position == articles.size() - 1 && listener != null){
            listener.onLoadMore();
        }
    }

    private void bindViewArticleNoImage(HolderItemArticleNoImage holder, Article article,int Pos) {
        holder.text_card_name_0.setText(article.getSnippet());
        holder.text_card_name_0.setOnClickListener(v -> goDetail(article));
        holder.image_action_share0.setOnClickListener(v -> doQuickShare(article));
        holder.image_action_like0.setOnClickListener(v -> {
            if(holder.image_action_like0.getDrawable().getConstantState().equals
                    (context.getResources().getDrawable(R.drawable.ic_action_action_favorite).getConstantState())){
                holder.image_action_like0.setImageResource(R.drawable.ic_action_favorite_enable);
            }else {
                holder.image_action_like0.setImageResource(R.drawable.ic_action_action_favorite);
            }
        });
    }

    private void doQuickShare(Article article) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "email@email.com");
        intent.putExtra(Intent.EXTRA_SUBJECT,article.getSnippet());
        intent.putExtra(Intent.EXTRA_TEXT, article.getWeb_url());
        context.startActivity(intent);
    }

    private void bindViewArticleHasImage(HolderItemArticleImage holder, Article article, int Pos) {
        holder.text_card_name_1.setText(article.getSnippet());
        Multimedium multimedium = article.getMultimedia().get(0);
        Glide.with(context)
                .load(multimedium.getUrl())
                .into(holder.image_card_cover);
        holder.text_card_name_1.setOnClickListener(v -> goDetail(article));
        holder.image_card_cover.setOnClickListener(v -> goDetail(article));
        holder.image_action_share1.setOnClickListener(v -> doQuickShare(article));
        holder.image_action_like1.setOnClickListener(v -> {
            if(holder.image_action_like1.getDrawable().getConstantState().equals
                    (context.getResources().getDrawable(R.drawable.ic_action_action_favorite).getConstantState())){
                holder.image_action_like1.setImageResource(R.drawable.ic_action_favorite_enable);
            }else {
                holder.image_action_like1.setImageResource(R.drawable.ic_action_action_favorite);
            }
        });
    }

    private void goDetail(Article article) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share_chrome);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "email@email.com");
        intent.putExtra(Intent.EXTRA_SUBJECT,article.getSnippet());
        intent.putExtra(Intent.EXTRA_TEXT, article.getWeb_url());
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setActionButton(bitmap, "Share link", pendingIntent, true);
        builder.setStartAnimations(context, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        builder.setExitAnimations(context, R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(article.getWeb_url()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Article> data) {
        this.articles.clear();
        this.articles.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<Article> articles){
        int nextPos = articles.size();
        this.articles.addAll(nextPos, articles);
        notifyDataSetChanged();
    }
}
