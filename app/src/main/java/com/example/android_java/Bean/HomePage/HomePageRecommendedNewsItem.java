package com.example.android_java.Bean.HomePage;

public class HomePageRecommendedNewsItem {
    private String questionName;
    private String questionDetail;
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
        this.questionName = questionName;
        this.questionDetail = questionDetail;
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

    // 这里应该不止有这些，但是为了方便，就暂定这两个
    // 之后写后端再改

}
