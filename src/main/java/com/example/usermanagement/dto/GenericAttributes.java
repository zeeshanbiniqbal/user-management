package com.example.usermanagement.dto;

import org.springframework.security.core.Transient;

import java.util.Date;

@Transient
public class GenericAttributes {

    private Long requestId;
    private Date startTime;
    private Date endTime;

    public Date getEndTime() {
        if(null==endTime){
            endTime= new Date();
        }
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        if(null==startTime){
            startTime= new Date();
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "GenericRequestParams{" +
                "requestId=" + requestId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
