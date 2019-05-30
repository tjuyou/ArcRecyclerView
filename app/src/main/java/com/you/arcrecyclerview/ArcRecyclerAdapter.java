package com.you.arcrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Author: you
 * Data：2019/5/30 15:46
 * Description:
 */
public class ArcRecyclerAdapter extends RecyclerView.Adapter<ArcRecyclerAdapter.VH> {

    private Integer[] mImgs = {android.R.drawable.ic_menu_week, android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_day, android.R.drawable.ic_menu_agenda, android.R.drawable.ic_menu_gallery};
    private Integer[] mItemText = {R.string.test1, R.string.test2,
            R.string.test3, R.string.test4, R.string.test5};
    private Context mContext;
    private int width;

    public interface OnCircleItemClickListener {
        void onCircleItemClick(View view, int position);
    }

    private OnCircleItemClickListener mOnCircleItemClickListener;

    public void setOnCircleItemClickListener(OnCircleItemClickListener mOnCircleItemClickListener) {
        this.mOnCircleItemClickListener = mOnCircleItemClickListener;
    }

    public ArcRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = dm.widthPixels;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.arc_recycler_item, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.mLayout.getLayoutParams();
        layoutParams.width = width / 5; //显示几个图表标
        holder.mLayout.setLayoutParams(layoutParams);
        holder.mText.setText(mItemText[position % mItemText.length]);
        Glide.with(mContext)
                .load(mImgs[position % mImgs.length])
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.mImg);
        if (mOnCircleItemClickListener != null) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnCircleItemClickListener.onCircleItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.arc_item_layout)
        LinearLayout mLayout;
        @BindView(R.id.arc_item_img)
        ImageView mImg;
        @BindView(R.id.arc_item_tv)
        TextView mText;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
