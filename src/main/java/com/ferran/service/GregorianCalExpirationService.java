package com.ferran.service;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GregorianCalExpirationService implements ExpirationService{

    @Override
    public Date getNewExpirationDate(int milis) {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.MILLISECOND, milis);
        return c.getTime();
    }
}
