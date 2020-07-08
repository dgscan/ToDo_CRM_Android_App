package com.example.user.todocrmappmobile;

public class TaskList {

    private String taskid;
    private String taskTxt;
    private String companyname;
    private String startDate;
    private String uptoDate;

    public TaskList(){
    }

    public TaskList(String taskid,String taskTxt, String companyname, String startDate, String uptoDate){
        this.taskid=taskid;
        this.taskTxt=taskTxt;
        this.companyname=companyname;
        this.startDate=startDate;
        this.uptoDate=uptoDate;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskTxt() {
        return taskTxt;
    }

    public void setTaskTxt(String taskTxt) {
        this.taskTxt = taskTxt;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getUptoDate() {
        return uptoDate;
    }

    public void setUptoDate(String uptoDate) {
        this.uptoDate = uptoDate;
    }

}
