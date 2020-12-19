package net.poisonlab.gamecast;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KKLee on 16. 6. 11..
 */
public class GameCastItemData1 implements Parcelable{
    public int tnailId, articleGroup0,articleGroup1,articleGroup2, likeItCount, commentCount, viewCount, articleId;
    public String articleSource, articleTitle, writer, writeDate;
    public String webURL, imageURL;
    public boolean likeIt;

    protected GameCastItemData1(Parcel in) {
        tnailId = in.readInt();
        articleGroup0 = in.readInt();
        articleGroup1 = in.readInt();
        articleGroup2 = in.readInt();
        likeItCount = in.readInt();
        commentCount = in.readInt();
        viewCount = in.readInt();
        articleId = in.readInt();
        articleSource = in.readString();
        articleTitle = in.readString();
        writer = in.readString();
        writeDate = in.readString();
        webURL = in.readString();
        imageURL = in.readString();
        likeIt = in.readByte() != 0;
    }

    protected GameCastItemData1() {
    }

    public boolean isLikeIt() {
        return likeIt;
    }

    public void setLikeIt(boolean likeIt) {
        this.likeIt = likeIt;
    }

    public int getTnailId() {
        return tnailId;
    }

    public void setTnailId(int tnailId) {
        this.tnailId = tnailId;
    }

    public int getArticleGroup0() {
        return articleGroup0;
    }

    public void setArticleGroup0(int articleGroup0) {
        this.articleGroup0 = articleGroup0;
    }

    public int getArticleGroup1() {
        return articleGroup1;
    }

    public void setArticleGroup1(int articleGroup1) {
        this.articleGroup1 = articleGroup1;
    }

    public int getArticleGroup2() {
        return articleGroup2;
    }

    public void setArticleGroup2(int articleGroup2) {
        this.articleGroup2 = articleGroup2;
    }

    public int getLikeItCount() {
        return likeItCount;
    }

    public void setLikeItCount(int likeItCount) {
        this.likeItCount = likeItCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleSource() {
        return articleSource;
    }

    public void setArticleSource(String articleSource) {
        this.articleSource = articleSource;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public static final Creator<GameCastItemData1> CREATOR = new Creator<GameCastItemData1>() {
        @Override
        public GameCastItemData1 createFromParcel(Parcel in) {
            return new GameCastItemData1(in);
        }

        @Override
        public GameCastItemData1[] newArray(int size) {
            return new GameCastItemData1[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tnailId);
        dest.writeInt(articleGroup0);
        dest.writeInt(articleGroup1);
        dest.writeInt(articleGroup2);
        dest.writeInt(likeItCount);
        dest.writeInt(commentCount);
        dest.writeInt(viewCount);
        dest.writeInt(articleId);
        dest.writeString(articleSource);
        dest.writeString(articleTitle);
        dest.writeString(writer);
        dest.writeString(writeDate);
        dest.writeString(webURL);
        dest.writeString(imageURL);
        dest.writeByte((byte) (likeIt ? 1 : 0));
    }
}
