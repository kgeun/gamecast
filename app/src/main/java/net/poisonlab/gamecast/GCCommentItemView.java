package net.poisonlab.gamecast;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KKLee on 16. 7. 12..
 */
public class GCCommentItemView  extends FrameLayout {
    public GCCommentItemView(Context context) {
        super(context);
        init();
    }

    public GCCommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView userName, commentDate, commentCtnt;
    ImageView userImage;


    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.gamecastcomment_item, this);

        userName = (TextView) findViewById(R.id.txt_gccomment_username);
        commentDate = (TextView) findViewById(R.id.txt_gccomment_date);
        commentCtnt = (TextView) findViewById(R.id.txt_gccomment_content);

    }

    public void setGCCommentItemData(GCCommentItemData mData) {
        userName.setText(mData.userName);
        commentDate.setText(mData.writeDate);
        commentCtnt.setText(mData.content);
    }

}

