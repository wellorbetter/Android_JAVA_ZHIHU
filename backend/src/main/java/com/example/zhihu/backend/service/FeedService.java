package com.example.zhihu.backend.service;

public class FeedService {

    public String recommendJson() {
        return "{\"items\":[{\"contentId\":\"q_1001\",\"title\":\"Java 面试如何系统准备？\",\"summary\":\"给出一份从基础到项目的计划\",\"authorName\":\"架构师A\",\"likeCount\":128,\"commentCount\":36,\"topicName\":\"Backend\"}],\"nextCursor\":\"cursor_2\",\"hasMore\":false}";
    }

    public String followingJson() {
        return "{\"items\":[{\"contentId\":\"q_2001\",\"title\":\"Android 性能优化实战\",\"summary\":\"启动优化、内存治理、卡顿排查\",\"authorName\":\"移动端B\",\"likeCount\":64,\"commentCount\":12,\"topicName\":\"Android\"}],\"nextCursor\":\"\",\"hasMore\":false}";
    }
}
