package net.poisonlab.gamecast;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KKLee on 16. 6. 11..
 */
public class PcListItemView1 extends FrameLayout {
    public PcListItemView1(Context context) {
        super(context);
        init();
    }

    public PcListItemView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView tnailView;
    TextView ratingView, reviewCountView, nameView, addressView,
                    eventView, cafeView, smokeView, cardpayView;
    PcListItemData1 mData;

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.pclist_itemview1,this);

        tnailView = (ImageView) findViewById(R.id.img_pclist_itemview1_tnil);
        ratingView = (TextView) findViewById(R.id.txt_pclist_itemview1_rating);
        reviewCountView = (TextView) findViewById(R.id.txt_pclist_itemview1_reviewcount);
        nameView = (TextView) findViewById(R.id.txt_pclist_itemview1_name);
        addressView = (TextView) findViewById(R.id.txt_pclist_itemview1_address);
        eventView = (TextView) findViewById(R.id.txt_pclist_itemview1_event);
        cafeView = (TextView) findViewById(R.id.txt_pclist_itemview1_cafe);
        smokeView = (TextView) findViewById(R.id.txt_pclist_itemview1_smoke);
        cardpayView = (TextView) findViewById(R.id.txt_pclist_itemview1_cardpay);
    }

    public void setPcListItemData1(PcListItemData1 mData) {
        this.mData = mData;

        tnailView.setImageResource(mData.tnailId);

        ratingView.setText(mData.rating);
        reviewCountView.setText(mData.reviewCount);
        nameView.setText(mData.name);
        addressView.setText(mData.address);

        if(mData.cafe)
            cafeView.setVisibility(VISIBLE);
        else
            cafeView.setVisibility(GONE);

        if(mData.cardpay)
            cardpayView.setVisibility(VISIBLE);
        else
            cardpayView.setVisibility(GONE);

        if(mData.event)
            eventView.setVisibility(VISIBLE);
        else
            eventView.setVisibility(GONE);

        if(mData.smoke)
            smokeView.setVisibility(VISIBLE);
        else
            smokeView.setVisibility(GONE);
    }
}
