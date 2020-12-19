package net.poisonlab.gamecast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CarouselPagerItemFragment extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String ITEM = "item";
    private static final String DRAWABLE_RESOURCE = "resource";

    private int screenWidth;
    private int screenHeight;

    ImageView tnailView, likeItImg;
    TextView articleGroup1,articleGroup2, likeItCount, commentCount, viewCount, articleGroup0, articleTitle, writerandDate;
    String webURL;
    LinearLayout tnilBG;
    LinearLayout linearLayout;
    CarouselLinearLayout root;


    ArrayList<GameCastItemData1> items = new ArrayList<GameCastItemData1>();

    public void addAll(List<GameCastItemData1> items)
    {
        this.items.addAll(items);
    }

    public static Fragment newInstance(GameCastHomeFragment context, float scale, GameCastItemData1 item) {
        Bundle b = new Bundle();
        b.putFloat(SCALE, scale);
        b.putParcelable(ITEM, item);

        return Fragment.instantiate(context.getActivity(), CarouselPagerItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_bestitem, container, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth * 5/7, screenWidth * 17/20);
        LinearLayout bestLayout = (LinearLayout) linearLayout.findViewById(R.id.layout_bestitems);
        bestLayout.setLayoutParams(layoutParams);

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);
        final GameCastItemData1 mData = this.getArguments().getParcelable(ITEM);

        init(inflater, container);

        articleGroup1.setText("#" + Util.SavedArticleGroup1[mData.articleGroup0][mData.articleGroup1]);
        articleGroup2.setText("#" + Util.SavedArticleGroup2[mData.articleGroup0][mData.articleGroup1][mData.articleGroup2]);

        likeItCount.setText(String.valueOf(mData.likeItCount));
        commentCount.setText(String.valueOf(mData.commentCount));
        viewCount.setText(String.valueOf(mData.viewCount));
        articleGroup0.setText("#" + Util.SavedArticleGroup0[mData.articleGroup0]);
        articleTitle.setText(mData.articleTitle);
        writerandDate.setText(mData.writer);

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

        //화면 해상도에 따라 줄 수 조정하는 부분 -> WQHD는 2560x 1440 FHD는 1920x 1080
        if(screenWidth>1200)
            articleTitle.setLines(3);
        else
            articleTitle.setLines(2);

        //handling click event
        bestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameCastListFragment.smData = mData;

                Intent intent = new Intent(getContext(), GameCastViewActivity.class);
                intent.putExtra("webURL",mData.webURL);
                intent.putExtra("articleId", mData.articleId);
                intent.putExtra("likeItCount", mData.likeItCount);
                intent.putExtra("commentCount", mData.commentCount);
                intent.putExtra("viewCount", mData.viewCount);
                intent.putExtra("likeIt", mData.likeIt);

                startActivity(intent);
            }
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        screenHeight = displaymetrics.heightPixels ;
        screenWidth = (int)(displaymetrics.widthPixels);
    }

    private void init(LayoutInflater inflater, ViewGroup container){

        //TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);

        tnailView = (ImageView) linearLayout.findViewById(R.id.img_bestitems_tnil);
        likeItImg = (ImageView) linearLayout.findViewById(R.id.img_bestitems_likeit);
        articleGroup0 = (TextView) linearLayout.findViewById(R.id.txt_bestitems_articleGroup0);
        articleGroup1 = (TextView) linearLayout.findViewById(R.id.txt_bestitems_articlegroup1);
        articleGroup2 = (TextView) linearLayout.findViewById(R.id.txt_bestitems_articlegroup2);
        likeItCount = (TextView) linearLayout.findViewById(R.id.txt_bestitems_likeitcount);
        commentCount = (TextView) linearLayout.findViewById(R.id.txt_bestitems_commentcount);
        viewCount = (TextView) linearLayout.findViewById(R.id.txt_bestitems_viewcount);
        articleTitle = (TextView) linearLayout.findViewById(R.id.txt_bestitems_articletitle);
        writerandDate = (TextView) linearLayout.findViewById(R.id.txt_bestitems_writeranddate);

    }
}
