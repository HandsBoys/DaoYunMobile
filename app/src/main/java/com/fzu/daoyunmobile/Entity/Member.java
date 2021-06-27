package com.fzu.daoyunmobile.Entity;

public class Member implements Comparable {
    private String ranking;
    private String iconFilePath = "";
    private int imageId = -1;
    private String memberName;
    private String stu_id;
    private String experience_score;
    private String singnInDate = "";
    private int score;

    public Member(String ranking, String memberName, String stu_id, String experience_score) {
        this.ranking = ranking;
        this.memberName = memberName;
        this.stu_id = stu_id;
        this.experience_score = experience_score;
        this.score = Integer.valueOf(experience_score);
    }

    public Member(String ranking, String iconFilePath, String memberName, String stu_id, String experience_score, String singnInDate) {
        this.ranking = ranking;
        this.iconFilePath = iconFilePath;
        this.memberName = memberName;
        this.stu_id = stu_id;
        this.experience_score = experience_score;
        this.singnInDate = singnInDate;
        this.score = Integer.valueOf(experience_score);

    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getIconFilePath() {
        return iconFilePath;
    }

    public String getRanking() {
        return ranking;
    }

    public int getImageId() {
        return imageId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getStu_id() {
        return stu_id;
    }

    public String getExperience_score() {
        return experience_score;
    }

    public String getSingnInDate() {
        return singnInDate;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Object o) {
        //从大到小排序
        Member s = (Member) o;
        return s.getScore() - this.getScore();
    }
}
