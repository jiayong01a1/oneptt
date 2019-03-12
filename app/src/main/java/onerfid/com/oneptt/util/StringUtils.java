package onerfid.com.oneptt.util;

public class StringUtils {
	  
	  public static boolean isBlank(String value)
	  {
	    return (value == null) || (value.trim().length() == 0);
	  }
	  
	  public static boolean isEmpty(CharSequence value)
	  {
	    return (value == null) || (value.length() == 0);
	  }
}
