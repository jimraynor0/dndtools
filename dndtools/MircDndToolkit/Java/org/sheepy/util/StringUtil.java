package org.sheepy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatWidthException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utility.
 * @author Ivan Yeung
 */
public class StringUtil {

  /** Zero-length String array */
  public static final String[] zeroString = new String[0];
  /** Unicode line breaks */
  public static final Pattern ucLineBreak = Pattern.compile("(?:\\r\\n|[\\r\\n\\f\\u0085\\u2028\\u2029])");
  /** Unicode space */
  public static final Pattern ucSpace = Pattern.compile("[\\u0009-\\u000D\\u0020\\u0085\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u2028\\u2029\\u202F\\u205F\\u3000]");
  /** Leading or trailing unicode space */
  private static final Pattern ucSpaceTrim = Pattern.compile("^"+ ucSpace.pattern()+"*|"+ ucSpace.pattern()+"*$");
  /** String make up of unicode space */
  private static final Pattern ucBlank = Pattern.compile("^"+ ucSpace.pattern()+"*$");
  /** spaces */
  public static final Pattern spaces = Pattern.compile("\\s+");

  /*****************************\
  |*     String processing     *|
  \*****************************/

  /**
   * Return first n character of a string.
   * If n is negative, last n characters are removed.
   * @param str String to cut short
   * @param length length of section to return (positive) or cut (negative)
   * @return Shorter string, or null if filteredResult in empty string
   */
  public static String leftStr(String str, int length) {
    if (isEmpty(str)||(length==0)) return "";
    if (length > 0)
      return str.substring(0, Math.min(length,str.length()));
    else
    if (-length >= str.length()) return "";
    else return str.substring(0, length+str.length());
  }

  /**
   * Return last n character of a string.
   * If n is negative, first n characters are removed.
   * @param str String to cut short
   * @param length length of section to return (positive) or cut (negative)
   * @return Shorter string, or null if filteredResult in empty string
   */
  public static String rightStr(String str, int length) {
    if (isEmpty(str)||(length==0)) return "";
    if (length > 0)
      return str.substring(str.length() - length, str.length());
    else
    if (-length >= str.length()) return ""; // Everything is cut
    else return str.substring(-length, str.length());
  }

  /**
   * Return first n words of a string.  Words are non-space seprated by space.
   * If insufficient or exact number of word presents, the whole string will be returned as is.
   * @param str A series of words.
   * @param count Number of words to return.
   * @return first n words of the string, each seprated by a single space.
   */
  public static String firstWords(String str, int count) {
    if (isEmpty(str) || (count <= 0)) return "";
    String[] strs = splitWords(str);
    if (strs.length <= count) return str;
    String result = "";
    for (int i = 0; i < count; i++)
      if (strs[i].length() > 0) result = result + strs[i] + " ";
    return result.trim();
  }

  /**
   * Return last n words of a string.  Words are non-space seprated by space.
   * If insufficient or exact number of word presents, the whole string will be returned as is.
   * @param str A series of words.
   * @param count Number of words to return.
   * @return last n words of the string, each seprated by a single space.
   */
  public static String lastWords(String str, int count) {
    if (isEmpty(str) || (count <= 0)) return "";
    String[] strs = splitWords(str);
    if (strs.length <= count) return str;
    String result = "";
    for (int i = strs.length-count-1; i < strs.length; i++)
      if (strs[i].length() > 0) result = result + strs[i] + " ";
    return result.trim();
  }

  /**
   * Split given string into lines according to Unicode's linebreak definition
   * @param s String to split
   * @return String in lines
   */
  public static String[] splitLines(CharSequence s) {
    if (isEmpty(s)) return zeroString;
    return ucLineBreak.split(s);
  }

  /**
   * Split given string into words according to regular expression's space definition
   * @param s String to split
   * @return String in words
   */
  public static String[] splitWords(CharSequence s) {
    if (isEmpty(s)) return zeroString;
    return spaces.split(s);
  }

  /**
   * Trim head and tail. but in unicode space not in normal space
   *
   * @param s String to trim
   * @return Trimmed string
   */
  public static String ucTrim(CharSequence s) {
    if (isEmpty(s)) return "";
    return ucSpaceTrim.matcher(s).replaceAll("");
  }

  /**
   * Check whether the string is null or make up of unicode space
   *
   * @param s String to check
   * @return true or false
   */
  public static boolean isUcBlank(CharSequence s) {
    return isEmpty(s) || ucBlank.matcher(s).matches();
  }

  /**
   * Turns all unicode space inro, well, space.
   *
   * @param s String to normalise
   * @return Normalised string
   */
  public static String ucSpaceNormalise(CharSequence s) {
    if (isEmpty(s)) return "";
    return ucSpace.matcher(s).replaceAll(" ");
  }

  /**
   * Convert a string to url encoded form in given charset.
   * Useful in preparing string to be transmitted over Internet.
   * @param string String to be encoded
   * @param charset The charset to convert the string into before encoding.  Use system default if empty.
   * @return Encoded string, e.g. "%D2%BB%B6%FE"
   * @throws UnsupportedEncodingException If given charset is invalid
   * @see #urlDecode(java.lang.String, java.lang.String)
   * @see #urlEncode(java.lang.String)
   */
  public static String urlEncode(String string, String charset) throws UnsupportedEncodingException {
    if (isEmpty(string)) return "";
    byte[] bary;
    if (isEmpty(charset))
      bary = string.getBytes();
    else
      bary = string.getBytes(charset);
    StringBuilder buf = new StringBuilder(bary.length*3);
    for (byte b : bary) {
      String s = Integer.toHexString(b);
      if (s.length() > 2) s = s.substring(6,8);
      if (s.length() >= 2)
        buf.append('%').append(s);
      else
        buf.append("%0").append(s);
    }
    return buf.toString().toUpperCase();
  }

  /**
   * Convert a string to url encoded form in system default charset
   * @param string String to be encoded
   * @return Encoded string, e.g. "%D2%BB%B6%FE"
   * @see #urlDecode(java.lang.String)
   * @see #urlEncode(java.lang.String, java.lang.String)
   */
  public static String urlEncode(String string) {
    try {
      return urlEncode(string, null);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Decode an url encoded string in given charset
   * @param string String to be decoded, e.g. "%D2%BB%B6%FE"
   * @return Decoded string
   * @param charset The charset to convert the string into after encoding.  Use system default if empty.
   * @throws UnsupportedEncodingException, IllegalFormatWidthException
   * @see #urlEncode(java.lang.String, java.lang.String)
   * @see #urlDecode(java.lang.String)
   */
  public static String urlDecode(String string, String charset) throws UnsupportedEncodingException {
    if (isEmpty(string)) return "";
    if ((string.length()%3)!=0) throw new IllegalFormatWidthException(string.length());
    byte[] buf = new byte[string.length()/3];
    string = string.toUpperCase();
    for (int i=0; i < string.length(); i+=3) {
      if (string.charAt(i) != '%') throw new IllegalFormatConversionException(string.charAt(i), String.class);
      buf[i/3] = (byte)Integer.parseInt(string.substring(i+1, i+3), 16);
    }
    if (isEmpty(charset))
      return new String(buf);
    else
      return new String(buf, charset);
  }

  /**
   * Decode an url encoded string in system default charset
   * @param string String to be decoded, e.g. "%D2%BB%B6%FE"
   * @return Decoded string
   * @see #urlEncode(java.lang.String)
   * @see #urlDecode(java.lang.String, java.lang.String)
   */
  public static String urlDecode(String string) {
    try {
      return urlDecode(string, null);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * CamlCase given text
   * @param text (e.g. "good day")
   * @return CamlCased text (e.g. "GoodDay"), or empty string if input is empty
   */
  public static String camlCase(String text) {
    if (isEmpty(text)) return "";
    String[] s = splitWords(text);
    StringBuilder result = new StringBuilder(text.length());
    for (String section : s) {
      if (section.length() > 0)
        result.append(Character.toUpperCase(section.charAt(0))).append(rightStr(section, -1));
    }
    return result.toString();
  }

  /**
   * Un-CamlCase given text with space
   * @param text CamlCased text to restore (e.g. GoodDay)
   * @return Un-CamlCased text (e.g. good day)
   */
  public static String unCamlCase(CharSequence text) {
    if (isEmpty(text)) return "";
    StringBuilder result = new StringBuilder(text.length()+10);
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (!Character.isUpperCase(c)) {
        result.append(c);
      } else {
        result.append(' ').append(Character.toLowerCase(c));
      }
    }
    return result.substring(1, result.length());
  }

  /**
   * Make given text Title Case. All words will be separated by a single space, regardness of original spacings
   * (white space, tab, line separater, carriage return)
   * @param text Text to convert to Title Case
   * @return Same words but with beginning letter of each word in Upper case and the rest in lower case.
   */
  public static String titleCase(String text) {
    if (isEmpty(text)) return "";
    String[] s = splitWords(text);
    StringBuilder result = new StringBuilder(text.length());
    for (String section : s) {
      result.append(' ').append(Character.toUpperCase(section.charAt(0))).append(rightStr(section, -1).toLowerCase());
    }
    return result.substring(1);
  }


  /**
   * Convert given string's half-width characters into full-width counterparts.
   * This function is not thread safe.
   * @param s String to convert
   * @return Converted string.
   */
  public static CharSequence toFullWidth(CharSequence s) {
    StringBuilder str = new StringBuilder(s.toString().replaceAll("\\.{3}", "…"));
    int quote = 0;
    int d_quote = 0;
    for (int i = 0; i < str.length(); i++) {
      char key = str.charAt(i);
      if (Character.charCount(key) > 1) {
        i += Character.charCount(key)-1;
        continue;
      }
      if (key >= 'a' && key <= 'z')
        switch (key) {
          case 'a': str.setCharAt(i, 'ａ'); continue;
          case 'b': str.setCharAt(i, 'ｂ'); continue;
          case 'c': str.setCharAt(i, 'ｃ'); continue;
          case 'd': str.setCharAt(i, 'ｄ'); continue;
          case 'e': str.setCharAt(i, 'ｅ'); continue;
          case 'f': str.setCharAt(i, 'ｆ'); continue;
          case 'g': str.setCharAt(i, 'ｇ'); continue;
          case 'h': str.setCharAt(i, 'ｈ'); continue;
          case 'i': str.setCharAt(i, 'ｉ'); continue;
          case 'j': str.setCharAt(i, 'ｊ'); continue;
          case 'k': str.setCharAt(i, 'ｋ'); continue;
          case 'l': str.setCharAt(i, 'ｌ'); continue;
          case 'm': str.setCharAt(i, 'ｍ'); continue;
          case 'n': str.setCharAt(i, 'ｎ'); continue;
          case 'o': str.setCharAt(i, 'ｏ'); continue;
          case 'p': str.setCharAt(i, 'ｐ'); continue;
          case 'q': str.setCharAt(i, 'ｑ'); continue;
          case 'r': str.setCharAt(i, 'ｒ'); continue;
          case 's': str.setCharAt(i, 'ｓ'); continue;
          case 't': str.setCharAt(i, 'ｔ'); continue;
          case 'u': str.setCharAt(i, 'ｕ'); continue;
          case 'v': str.setCharAt(i, 'ｖ'); continue;
          case 'w': str.setCharAt(i, 'ｗ'); continue;
          case 'x': str.setCharAt(i, 'ｘ'); continue;
          case 'y': str.setCharAt(i, 'ｙ'); continue;
          case 'z': str.setCharAt(i, 'ｚ'); continue;
        }
      else if (key >= 'A' && key <= 'Z')
        switch (key) {
          case 'A': str.setCharAt(i, 'Ａ'); continue;
          case 'B': str.setCharAt(i, 'Ｂ'); continue;
          case 'C': str.setCharAt(i, 'Ｃ'); continue;
          case 'D': str.setCharAt(i, 'Ｄ'); continue;
          case 'E': str.setCharAt(i, 'Ｅ'); continue;
          case 'F': str.setCharAt(i, 'Ｆ'); continue;
          case 'G': str.setCharAt(i, 'Ｇ'); continue;
          case 'H': str.setCharAt(i, 'Ｈ'); continue;
          case 'I': str.setCharAt(i, 'Ｉ'); continue;
          case 'J': str.setCharAt(i, 'Ｊ'); continue;
          case 'K': str.setCharAt(i, 'Ｋ'); continue;
          case 'L': str.setCharAt(i, 'Ｌ'); continue;
          case 'M': str.setCharAt(i, 'Ｍ'); continue;
          case 'N': str.setCharAt(i, 'Ｎ'); continue;
          case 'O': str.setCharAt(i, 'Ｏ'); continue;
          case 'P': str.setCharAt(i, 'Ｐ'); continue;
          case 'Q': str.setCharAt(i, 'Ｑ'); continue;
          case 'R': str.setCharAt(i, 'Ｒ'); continue;
          case 'S': str.setCharAt(i, 'Ｓ'); continue;
          case 'T': str.setCharAt(i, 'Ｔ'); continue;
          case 'U': str.setCharAt(i, 'Ｕ'); continue;
          case 'V': str.setCharAt(i, 'Ｖ'); continue;
          case 'W': str.setCharAt(i, 'Ｗ'); continue;
          case 'X': str.setCharAt(i, 'Ｘ'); continue;
          case 'Y': str.setCharAt(i, 'Ｙ'); continue;
          case 'Z': str.setCharAt(i, 'Ｚ'); continue;
        }
      else if (key >= '0' && key <= '9')
        switch (key) {
          case '0': str.setCharAt(i, '０'); continue;
          case '1': str.setCharAt(i, '１'); continue;
          case '2': str.setCharAt(i, '２'); continue;
          case '3': str.setCharAt(i, '３'); continue;
          case '4': str.setCharAt(i, '４'); continue;
          case '5': str.setCharAt(i, '５'); continue;
          case '6': str.setCharAt(i, '６'); continue;
          case '7': str.setCharAt(i, '７'); continue;
          case '8': str.setCharAt(i, '８'); continue;
          case '9': str.setCharAt(i, '９'); continue;
        }
      else
        switch (key) {
          case '\'': str.setCharAt(i, (quote++ % 2 == 0) ? '‘' : '’'); continue;
          case '"': str.setCharAt(i, (d_quote++ % 2 == 0) ? '“' : '”'); continue;

          case '.': str.setCharAt(i, '。'); continue;
          case ',': str.setCharAt(i, '，'); continue;
          case ':': str.setCharAt(i, '：'); continue;
          case ';': str.setCharAt(i, '；'); continue;
          case '!': str.setCharAt(i, '！'); continue;
          case '?': str.setCharAt(i, '？'); continue;
          case '@': str.setCharAt(i, '＠'); continue;
          case '#': str.setCharAt(i, '＃'); continue;
          case '$': str.setCharAt(i, '＄'); continue;
          case '%': str.setCharAt(i, '％'); continue;
          case '^': str.setCharAt(i, '︿'); continue;
          case '&': str.setCharAt(i, '＆'); continue;
          case '<': str.setCharAt(i, '＜'); continue;
          case '>': str.setCharAt(i, '＞'); continue;
          case '(': str.setCharAt(i, '（'); continue;
          case ')': str.setCharAt(i, '）'); continue;
          case '[': str.setCharAt(i, '〔'); continue;
          case ']': str.setCharAt(i, '〕'); continue;
          case '{': str.setCharAt(i, '｛'); continue;
          case '}': str.setCharAt(i, '｝'); continue;
          case '+': str.setCharAt(i, '＋'); continue;
          case '-': str.setCharAt(i, '－'); continue;
          case '*': str.setCharAt(i, '＊'); continue;
          case '=': str.setCharAt(i, '＝'); continue;
          case '|': str.setCharAt(i, '｜'); continue;
          case '/': str.setCharAt(i, '／'); continue;
          case '\\': str.setCharAt(i, '＼'); continue;
          case '`': str.setCharAt(i, '～'); continue;
          case '~': str.setCharAt(i, '‵'); continue;
          case '_': str.setCharAt(i, '＿'); continue;
          case ' ': str.setCharAt(i, '　'); continue;
//        case '': str.setCharAt(i, ''); continue;
        }
    }
    return str;
  }

  /******************************\
  |*     String information     *|
  \******************************/

  /**
   * Test the occurance of a substring within a string.
   * On null / empty str or substring, returns false.
   * @param str String to search in
   * @param substr Substring to look for
   * @return True if occurence of substr in str has been found.
   */
  public static boolean occured(String str, String substr) {
    return occured(str, substr, false);
  }

  public static boolean occured(String str, String substr, boolean ignoreCase) {
    if (isEmpty(substr)||isEmpty(str)) return false;
    if (ignoreCase) {
      substr = substr.toLowerCase();
      str = str.toLowerCase();
    }
    return str.indexOf(substr) >= 0;
  }

  /**
   * Count the occurance of a substring within a string.
   * On null / empty str or substring, returns 0.
   * @param str String to search in
   * @param substr Substring to look for
   * @return Number of occurence of substr in str.
   */
  public static int countOccurence(String str, String substr) {
    if (isEmpty(substr)||isEmpty(str)) return 0;
    int o = 0;
    int p = str.indexOf(substr);
    for (; p >= 0; p = str.indexOf(substr, p+substr.length())) o++;
    return o;
  }

  /**
   * Count the occurance of a character within a string.
   * On null / empty string, returns 0.
   * @param str String to search in
   * @param symbol Character to look for
   * @return Number of occurence of symbol in str.
   */
  public static int countOccurence(String str, char symbol) {
    if (isEmpty(str)) return 0;
    int o = 0;
    int p = str.indexOf(symbol);
    for (; p >= 0; p = str.indexOf(symbol, p+1)) o++;
    return o;
  }

  /**
   * Check whether all the input strings are equals.
   * @param s1 String 1
   * @param s2 String 2
   * @param str More string to compare
   * @return true if all Strings are equal
   */
  public static boolean equals(String s1, String s2, String ... str) {
    if (s1 == null) {
      if (s2 != null) return false;
      for (String s : str)
        if (s != null) return false;
      return true;
    } else {
      if (!s1.equals(s2)) return false;
      for (String s : str)
        if (!s1.equals(s)) return false;
      return true;
    }
  }

  /**
   * Check whether given string is empty (null or zero length)
   * @param s String to check
   * @return true if input is null or is zero length
   */
  public final static boolean isEmpty(CharSequence s) {
    return (s==null)||(s.length()<=0);
  }

  /**
   * Check whether given string is empty (null or zero length)
   * @param s String to check
   * @return true if input is null or is zero length
   */
  public final static boolean isBlank(CharSequence s) {
    return isEmpty(s) || s.toString().trim().equals("");
  }

  /**
   * Convert a (potentially empty) string to integer
   * @param str Input string
   * @param def Default value if string is null
   * @return Converted number
   */
  public static final int strToInt(String str, int def) {
    if (str == null || str.equals("")) return def;
    try {
      return Integer.parseInt(str.trim());
    } catch (NumberFormatException e) {
      return def;
    }
  }

  /**
   * Convert a (potentially empty) string to double
   * @param str Input string
   * @param def Default value if string is null
   * @return Converted number
   */
  public static final double strToDouble(String str, double def) {
    if (str == null || str.equals("")) return def;
    try {
      return Double.parseDouble(str.trim());
    } catch (NumberFormatException e) {
      return def;
    }
  }

  /**
   * Glue collection's object to a single string
   * @param c Collection to glue
   * @param glue Glue to use
   * @return result string
   */
  public static final String implode(Collection c, CharSequence glue) {
    StringBuilder buffer = new StringBuilder();
    for (Object o : c) buffer.append(o).append(glue);
    buffer.setLength(buffer.length()-glue.length());
    return buffer.toString();
  }

  /**
   * Glue collection's object to a single string
   * @param c Collection to glue
   * @param glue Glue to use
   * @return result string
   */
  public static final String implode(Object[] c, CharSequence glue) {
    StringBuilder buffer = new StringBuilder();
    for (Object o : c) buffer.append(o).append(glue);
    buffer.setLength(buffer.length()-glue.length());
    return buffer.toString();
  }

  /**
   * Check whether given strings are the same.
   * Null and empty string are not the same.
   * @param s1 String 1
   * @param s2 String 2
   * @return trus if same
   */
  public static boolean isSame(CharSequence s1, CharSequence s2) {
    return (s1 == s2) || (s1 != null && s1.equals(s2));
  }

  /**
   * Check whether given strings are the same.
   * @param s1 String 1
   * @param s2 String 2
   * @param nullEqualsEmpty if true, null are the same as empty
   * @return true if same
   */
  public static boolean isSame(CharSequence s1, CharSequence s2, boolean nullEqualsEmpty) {
    return (s1 == s2) || (nullEqualsEmpty && isEmpty(s1) && isEmpty(s2)) || (s1 != null && s1.equals(s2));
  }

  /***************************\
  |*     String creation     *|
  \***************************/

  /**
   * Load all the string from a system resource
   * @param filename Name of the system resource
   * @param encoding Encoding of the system resource, or null for default.
   * @return All lines of the resources.
   * @throws IOException When anything goes wrong.
   */
  public static String[] loadLines(String filename, String encoding) throws IOException {
    java.util.List<String> s = new ArrayList<String>(21);
    BufferedReader r;
    if (!isEmpty(encoding))
      r = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResource(filename).openStream(), encoding));
    else
      r = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResource(filename).openStream()));
    while (r.ready()) s.add(r.readLine());
    return s.toArray(zeroString);
  }

  /**
   * Generate a string by repeating another string.
   * On null, negative or zero input, return null.
   * @param str String to repeat
   * @param repeat_count number of repeat
   * @return A string of repeated occurence of str
   */
  public static String repeatString(int repeat_count, String str) {
    if (isEmpty(str)||(repeat_count<=0)) return "";
    if (repeat_count == 1) return str;
    StringBuilder s = new StringBuilder(str.length()*repeat_count);
    for (; repeat_count > 0; repeat_count--) s.append(str);
    return str.toString();
  }

  /**
   * Generate a string by repeating another character.
   * On negative or zero input, return null.
   * Done using Arrays.fill(char[], char)
   * @param symbol Character to repeat
   * @param repeat_count number of repeat
   * @return A string of repeated occurence of symbol
   */
  public static String repeatString(int repeat_count, char symbol) {
    if (repeat_count<=0) return "";
    char[] c = new char[repeat_count];
    Arrays.fill(c, symbol);
    return new String(c);
  }

  public static String toCSV(Object[] array) {
    return toCSV(array, ',', '"');
  }

  /**
   * Convert array into CSV
   * @param array Source array
   * @param separator CSV separator, must be regx charset safe like pipe or tab
   * @param delimiter CSV delimiter, much be regx charset safe like double quote or pipe
   * @return Array in CSV form
   */
  public static String toCSV(Object[] array, char separator, final char delimiter) {
    StringBuffer buf = new StringBuffer();
    String delimit = new String(new char[]{delimiter});
    Matcher breakOrDelimit = Pattern.compile("[\\r\\n\\f\\u0085\\u2028\\u2029"+delimiter+separator+"]").matcher("");
    for (Object o : array) {
      if (o != null) {
        String s = o.toString();
        if (s.length() > 0) {
          breakOrDelimit.reset(s);
          if (breakOrDelimit.find()) {
            buf.append(delimiter).append(s.replaceAll(delimit, delimit+delimit)).append(delimit);
          } else
            buf.append(s);
        }
      }
      buf.append(separator);
    }
    if (buf.length() > 0)
      buf.setLength(buf.length()-1);
    return buf.toString();
  }

}

