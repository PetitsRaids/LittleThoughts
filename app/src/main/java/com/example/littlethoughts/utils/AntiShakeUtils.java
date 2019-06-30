package com.example.littlethoughts.utils;

import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.example.littlethoughts.R;

/**
 * 防抖动点击
 *
 * @author jiangshicheng
 */
public class AntiShakeUtils {

    private final static long INTERNAL_TIME = 1000;

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see #isInvalidClick(View, long)
     */
    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp = 0;
        Object o = target.getTag(R.id.last_click_time);
        if (o == null){
            target.setTag(R.id.last_click_time, curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (Long) o;
        boolean isInvalid = curTimeStamp - lastClickTimeStamp < internalTime;
        if (!isInvalid)
            target.setTag(R.id.last_click_time, curTimeStamp);
        return isInvalid;
    }
}
