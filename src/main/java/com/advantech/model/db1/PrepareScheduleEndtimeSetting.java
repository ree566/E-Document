/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "PrepareSchedule_EndtimeSetting")
public class PrepareScheduleEndtimeSetting implements Serializable {

    private int id;
    private int weekOfYear;
    private Date scheduleEndtime;

    public PrepareScheduleEndtimeSetting() {

    }

    public PrepareScheduleEndtimeSetting(Date scheduleEndtime) {
        this.scheduleEndtime = scheduleEndtime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "week_of_year", nullable = false)
    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_endTime", length = 23)
    public Date getScheduleEndtime() {
        return scheduleEndtime;
    }

    public void setScheduleEndtime(Date scheduleEndtime) {
        this.scheduleEndtime = scheduleEndtime;
    }

}
