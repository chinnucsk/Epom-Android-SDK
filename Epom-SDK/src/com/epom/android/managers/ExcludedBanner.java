package com.epom.android.managers;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/2/12
 * Time: 1:49 PM
 */
public class ExcludedBanner implements Delayed {
    private int id;
    private long creationPoint;
    private long period;

    public ExcludedBanner(long period, int id) {
        creationPoint = System.currentTimeMillis();
        this.period = period;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExcludedBanner)) return false;

        ExcludedBanner that = (ExcludedBanner) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return period - (long) ((System.currentTimeMillis() - creationPoint) * 1f / 1000);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (delayed.getDelay(TimeUnit.SECONDS) - getDelay(TimeUnit.SECONDS));
    }

    public int getId() {
        return id;
    }
}
