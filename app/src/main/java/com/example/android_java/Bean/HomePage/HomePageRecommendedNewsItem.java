package com.example.android_java.Bean.HomePage;

public class HomePageRecommendedNewsItem {
    private String questionName;
    private String questionDetail;

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
