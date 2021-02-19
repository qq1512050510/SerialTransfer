package com.lingyue.util;

import java.io.PrintStream;
import org.springframework.util.StringUtils;

public class StringComUtils
{
    public static byte[] hexStringToBinaryStr(String hexString)
    {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[(int)Math.round(len / 2.0D)];
        while (index < len) {
            int indexR = index + 2;
            if (indexR > len) {
                indexR = len;
            }
            String sub = hexString.substring(index, indexR);
            bytes[(index / 2)] = (byte)Integer.parseInt(sub, 16);
            index = indexR;
        }
        return bytes;
    }

    public static void main(String[] args) {
        String hexString = "FE";
        System.out.println(hexStringToBinaryStr(hexString));
    }
}