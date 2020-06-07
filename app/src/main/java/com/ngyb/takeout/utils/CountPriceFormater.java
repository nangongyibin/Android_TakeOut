package com.ngyb.takeout.utils;

import java.text.NumberFormat;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:54
 */
public class CountPriceFormater {
    public static String format(float countPrice) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(countPrice);
    }
}
