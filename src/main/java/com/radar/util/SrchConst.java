package com.radar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SrchConst {
    private Date[] timeRange;
    private String dataType;

    public boolean inTimeRange(String time) throws ParseException {
        if(time !=null && time .equals("")) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date d = format.parse(time);
        int left = d.compareTo(timeRange[0]);
        int right = d.compareTo(timeRange[1]);
        return (left >=0 && right <= 0);
    }
    public boolean inDataType(String dataType){
        return this.dataType.equals(dataType);

    }

    public Date[] getPeriod() {
        return timeRange;
    }

    public void setPeriod(Date[] period) {
        this.timeRange = period;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
