package onerfid.com.oneptt.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 *  提示信息工具类
 */

public class OnePromptUtil {

    /**
     * 控制editview的输入数字区间  带提示  可控制提示的textview隐藏
     * @param et        editview
     * @param MIN_MARK 最小值
     * @param MAX_MARK 最大值
     * @param MID_MARK 默认值
     * @param tv       提示的textview
     * @param text     提示的文字
     */
    public static void setRegion(final EditText et, final int MIN_MARK , final int MAX_MARK, final int MID_MARK , final TextView tv , final String text)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText(String.valueOf(MID_MARK));
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal < MIN_MARK || markVal > MAX_MARK) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(text);
                        }  else {
                            tv.setVisibility(View.GONE);
                        }

                        return;
                    }
                }
            }
        });
    }


    /**
     * 控制editview的输入数字区间  带提示  不可控制提示的textview隐藏
     * @param et        editview
     * @param MIN_MARK 最小值
     * @param MAX_MARK 最大值
     * @param MID_MARK 默认值
     * @param tv       提示的textview
     * @param text     提示的文字
     */
    public static void setRegion2(final EditText et, final int MIN_MARK , final int MAX_MARK, final int MID_MARK , final TextView tv , final String text)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText(String.valueOf(MID_MARK));
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal < MIN_MARK || markVal > MAX_MARK) {
                            tv.setText(text);
                        }

                        return;
                    }
                }
            }
        });
    }

    /**
     * 控制editview的输入数字区间  带提示  不可控制提示的textview隐藏
     * @param et        editview
     * @param MIN_MARK 最小值
     * @param MAX_MARK 最大值
     * @param MID_MARK 默认值
     * @param tv       提示的textview
     * @param text     提示的文字
     */
    public static void setRegion3(final EditText et, final int MIN_MARK , final int MAX_MARK, final int MID_MARK , final TextView tv , final String text)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText(String.valueOf(MID_MARK));
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (s.length() == 1 &&  Integer.parseInt(s.toString()) != 0) {

                    }


                  /*  if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal < MIN_MARK || markVal > MAX_MARK) {
                            tv.setText(text);
                        }

                        return;
                    }*/
                }
            }
        });
    }
}
