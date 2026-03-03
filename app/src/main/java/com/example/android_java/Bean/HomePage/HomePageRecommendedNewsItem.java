package com.example.android_java.Bean.HomePage;

public class HomePageRecommendedNewsItem {
    private String questionName;
    private String questionDetail;
    private String topicName;
    private String contentId;
    private int likeCount;
    private String questionId;  // 问题的id
    private String answerId;   // 回答的id
    private String answererId;  // 回答者的id
    private String answererName;  // 回答者的名字
    private String answererAvatar;
    private int retransmissionCount;
    private int collectionCount;
    private int commentCount;
    private boolean isSubscribe; // 是否关注

    public HomePageRecommendedNewsItem(String questionName, String questionDetail) {
        this(questionName, questionDetail, "");
    }

    public HomePageRecommendedNewsItem(String questionName, String questionDetail, String topicName) {
        this.questionName = questionName;
        this.questionDetail = questionDetail;
        this.topicName = topicName;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    // 这里应该不止有这些，但是为了方便，就暂定这两个
    // 之后写后端再改

}
