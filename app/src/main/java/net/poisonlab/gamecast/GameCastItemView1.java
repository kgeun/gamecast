package net.poisonlab.gamecast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by KKLee on 16. 6. 11..
 */
public class GameCastItemView1 extends FrameLayout {
    public GameCastItemView1(Context context) {
        super(context);
        init();
    }

    public GameCastItemView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView tnailView, likeItImg;
    TextView articleGroup1,articleGroup2, likeItCount, commentCount, viewCount, articleSource, articleTitle, writerandDate;
    String webURL;
    LinearLayout tnilBG;

    GameCastItemData1 mData;

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.gamecastlist_item,this);

        tnailView = (ImageView) findViewById(R.id.img_gamecast_tnil);
        likeItImg = (ImageView) findViewById(R.id.txt_bestitems_view);
        articleGroup1 = (TextView) findViewById(R.id.txt_gamecast_articlegroup1);
        articleGroup2 = (TextView) findViewById(R.id.txt_gamecast_articlegroup2);
        likeItCount = (TextView) findViewById(R.id.txt_gamecast_likeitcount);
        commentCount = (TextView) findViewById(R.id.txt_gamecast_commentcount);
        viewCount = (TextView) findViewById(R.id.txt_gamecast_viewcount);
        articleSource = (TextView) findViewById(R.id.txt_gamecast_articleSource);
        articleTitle = (TextView) findViewById(R.id.txt_gamecast_articletitle);
        writerandDate = (TextView) findViewById(R.id.txt_gamecast_writeranddate);
        tnilBG = (LinearLayout) findViewById(R.id.layout_tnilBG);
    }

    public void setGameCastItemData1(GameCastItemData1 mData) {
        this.mData = mData;

        tnailView.setImageResource(mData.tnailId);

        articleGroup1.setText("#" + Util.SavedArticleGroup1[mData.articleGroup0][mData.articleGroup1]);
        articleGroup2.setText("#" + Util.SavedArticleGroup2[mData.articleGroup0][mData.articleGroup1][mData.articleGroup2]);

        likeItCount.setText(String.valueOf(mData.likeItCount));
        commentCount.setText(String.valueOf(mData.commentCount));
        viewCount.setText(String.valueOf(mData.viewCount));
        articleSource.setText(mData.articleSource);
        articleTitle.setText(mData.articleTitle);
        writerandDate.setText(mData.writer);

        final int paddingBottom = tnilBG.getPaddingBottom(), paddingLeft = tnilBG.getPaddingLeft();
        final int paddingRight = tnilBG.getPaddingRight(), paddingTop = tnilBG.getPaddingTop();

        if(mData.articleSource.equals("Youtube")){
            tnilBG.setBackgroundResource(R.drawable.bulpi_9patch_v2_red);
            tnilBG.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        else if(mData.articleSource.equals("Facebook")){
            tnilBG.setBackgroundResource(R.drawable.bulpi_9patch_v2_blue);
            tnilBG.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        else if(mData.articleSource.equals("Afreeca")){
            tnilBG.setBackgroundResource(R.drawable.bulpi_9patch_v2_green);
            tnilBG.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }

        if(mData.articleGroup0 == 0) {
            Glide
                    .with(getContext())
                    .load(mData.imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.owplaceholder)
                    .crossFade()
                    .into(tnailView);
        }
        else if(mData.articleGroup0 == 1) {
            Glide
                    .with(getContext())
                    .load(mData.imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.lolplaceholder)
                    .crossFade()
                    .into(tnailView);
        }
        else {
            Glide
                    .with(getContext())
                    .load(mData.imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.saplaceholder)
                    .crossFade()
                    .into(tnailView);
        }

        if(mData.likeIt)
        {
            likeItImg.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            likeItImg.clearColorFilter();
        }


        //                                .centerCrop()
        //                .bitmapTransform(new RoundedCornersTransformation(getContext(), 15, 0, RoundedCornersTransformation.CornerType.ALL))

    }
}
