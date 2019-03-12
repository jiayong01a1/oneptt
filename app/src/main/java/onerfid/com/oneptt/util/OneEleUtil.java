package onerfid.com.oneptt.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jiayong on 2018/4/27.
 */
public class OneEleUtil {

    //加密   32位小写
    public static String get32MD5Str(String str) {


        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.toLowerCase().getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * unicode编码转中文
     */
    public static String decodeUnicode(final String data) {
        int start = 0;
        int end = 0;
        String dataStr = data.toLowerCase();
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
    /**
     * 中文转Unicode码
     * @param gbString
     * @return
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
//            unicodeBytes = unicodeBytes + "\\u" + hexB.toUpperCase();
            unicodeBytes = unicodeBytes  + hexB.toUpperCase();
        }
        return unicodeBytes;
    }


    /**
     * 字符串转换为Ascii码   输出总和
     * @param value    需转换的字符串
     * @return
     */
    public static int strToASCII(String value)

    {
        int asciiNum = 0;
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                asciiNum = (int)chars[i] + asciiNum;
            }
            else {
                asciiNum = (int)chars[i] + asciiNum;
            }
        }

        return asciiNum;
    }

}
