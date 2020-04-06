package com.hoangdieuctu.tools.kafkas.model;

public class LikeData {
    private int likeCount;
    private boolean liked;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
