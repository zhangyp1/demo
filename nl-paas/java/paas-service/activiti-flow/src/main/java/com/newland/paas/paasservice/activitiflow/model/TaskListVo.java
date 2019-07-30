package com.newland.paas.paasservice.activitiflow.model;

/**
 * 任务
 */
public class TaskListVo {
    private String taskId;
    //当前任务名称
    private String taskName;
    //流程开始时间
    private String startTime;
    //流程结束时间
    private String endTime;
    //审批状态
    private String status;
    //审批人
    private String assignee;
    //候选执行人
    private String candiate;
    //审批动作
    private String action;
    //意见
    private String comments;
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCandiate() {
        return candiate;
    }

    public void setCandiate(String candiate) {
        this.candiate = candiate;
    }

}
