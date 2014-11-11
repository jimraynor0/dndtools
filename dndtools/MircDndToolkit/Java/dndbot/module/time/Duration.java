package dndbot.module.time;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sheepy.util.StringUtil;

/**
 * Represents a time duration
 * 
 * Created by Ho Yiu Yeung on Apr 4, 2007
 */
public class Duration {
  public enum Unit { SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, SEASON, YEAR, ROUND, TURN }
  
  final double count;
  final Unit unit;
  
  public Duration(double count, Unit unit) {
    this.count = count;
    this.unit = unit;
  }
  
  private static final Pattern timePattern = Pattern.compile("(?<=^|\\W)"+
//      "(-?\\d+(?:.\\d+))"+ // number
      "(-?\\d+)"+ // integer
      "(s|m|h|d|w|n|y|r|t|sec|second|min|minute|hr|hour|day|week|month|year||rd|round|turn)"+ // unit
      "(?=\\W|$)", Pattern.CASE_INSENSITIVE);
    
  /**
   * Parse a time string
   * @param time String to parse
   * @return Time duration
   */
  public static Duration parseTime(String time) {
    Matcher m = timePattern.matcher(time);
    if (!m.matches()) throw new IllegalArgumentException("Invalid time format");
    double duration = StringUtil.strToDouble(m.group(1), 0.0);
    String unit = m.group(2).toLowerCase();
    if (unit.startsWith("month")) unit = "n";
    switch (unit.charAt(0)) {
      case 's': return new Duration(duration, Unit.SECOND);
      case 'm': return new Duration(duration, Unit.MINUTE);
      case 'h': return new Duration(duration, Unit.HOUR);
      case 'd': return new Duration(duration, Unit.DAY);
      case 'w': return new Duration(duration, Unit.WEEK);
      case 'n': return new Duration(duration, Unit.MONTH);
      case 'y': return new Duration(duration, Unit.YEAR);
      case 'r': return new Duration(duration, Unit.ROUND);
      case 't': return new Duration(duration, Unit.TURN);
      default: throw new IllegalArgumentException("Invalid time unit - "+unit);
    }
  }
  
  /**
   * Return value of this duration in nearest second 
   * @return value of this duration in nearest second
   */
  public long inSecond() {
    double time = count;
    switch (unit) {
      case ROUND: return Math.round(time*6);
      case YEAR: time *= 365.2425;
      case MONTH: time *= 30.436875;
      case WEEK: time *= 7;
      case DAY: time *= 24;
      case HOUR: time *= 60;
      case TURN:
      case MINUTE: time *= 60;
      case SECOND: break;
      default: throw new IllegalArgumentException("Invalid time unit - "+unit);
    }
    return Math.round(time);
  }

  /**
   * Get amount of unit, for use with GregorianCalendar
   * @return amount of unit
   * @see #getField()
   */
  public int getAmount() {
    if (unit != Unit.ROUND)
      return Math.round((float)count);
    else
      return Math.round((float)count*6);
  }

  /**
   * Get time unit in term of GregorianCalendar
   * @return time unit
   * @see #getAmount()
   */
  public int getField() {
    switch (unit) {
      case ROUND:
      case SECOND: return Calendar.SECOND;
      case TURN:
      case MINUTE: return Calendar.MINUTE;
      case HOUR: return Calendar.HOUR_OF_DAY;
      case DAY: return Calendar.DAY_OF_YEAR;
      case WEEK: return Calendar.WEEK_OF_YEAR;
      case MONTH: return Calendar.MONTH;
      case YEAR: return Calendar.YEAR;
      default: throw new IllegalArgumentException("Invalid time unit - "+unit);
    }
  }
  
  /**
   * Moves given calendar forward
   * @param cal
   * @return Givan calendar, now with new date
   */
  public Calendar add(Calendar cal) {
    cal.add(getField(), getAmount());
    return cal;
  }

  public static String format(Long remaining) {
    if (remaining == null) return null;
    long l = remaining;
    if (l > 86400) {
      return l/86400+"/"+(l%86400)/3600+":"+(l%3600)/60+":"+l%60;
    } else
      return l/3600+":"+(l%3600)/60+":"+l%60;
  }

}