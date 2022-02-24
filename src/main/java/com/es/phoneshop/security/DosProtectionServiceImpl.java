package com.es.phoneshop.security;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final long THRESHOLD = 20;

    private static DosProtectionService instance;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, Calendar> thresholdTimeMap = new ConcurrentHashMap<>();

    public static DosProtectionService getInstance() {
        if (instance == null) {
            instance = new DosProtectionServiceImpl();
        }
        return instance;
    }

    private DosProtectionServiceImpl() {
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        Calendar thresholdTime = thresholdTimeMap.get(ip);
        if (count == null || thresholdTime == null) {
            count = 1L;
            thresholdTime = new GregorianCalendar();
            thresholdTime.add(Calendar.MINUTE, 1);
            thresholdTimeMap.put(ip, thresholdTime);
        } else {
            Calendar currentTime = new GregorianCalendar();
            if (currentTime.before(thresholdTime)) {
                if (count > THRESHOLD) {
                    return false;
                }
                count++;
            } else {
                currentTime.add(Calendar.MINUTE, 1);
                thresholdTimeMap.put(ip, currentTime);
                count = 0L;
            }
        }
        countMap.put(ip, count);
        return true;
    }
}
