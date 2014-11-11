package dndbot.module.time;

import java.util.Calendar;

/**
 * A single timer
 * <p/>
 * Created by Ho Yiu YEUNG on 2007 Apr 5
 */
public class Timer {
  private final Calendar started; // Timer start time
  private Calendar now; // The time now, as know by the timer
  private Calendar stop; // When to stop
  private Calendar next_alarm; // Next alarm to throw
  private final int interval_field; // Alarm interval
  private final int interval_amount; // Alarm intercal
  private int count; // Alarm count 
  public String alarm; // Alarm string, not used by this class

  public boolean enabled = true;


  public Timer(Calendar now, Calendar stop) {
    this(now, stop, 0, 0);
  }

  /**
   * Creates a timer with regular alarm
   * @param now Start time
   * @param stop Stop time, maybe null
   * @param interval_field
   * @param interval_amount
   */
  public Timer(Calendar now, Calendar stop, int interval_field, int interval_amount) {
    if (now == null) throw new NullPointerException();
    this.started = (Calendar) now.clone();
    this.now = now;
    this.stop = stop;
    this.interval_field = interval_field;
    this.interval_amount = interval_amount;
    if (interval_amount > 0) {
      next_alarm = (Calendar) now.clone();
      next_alarm.add(interval_field, interval_amount);
    }
  }

  /**
   * Moves the timer forward or backward
   *
   * @param field  Time field: second, day, tenday, year, etc.
   * @param amount Amount of field to add
   * @return true if alarm is triggered.  false otherwise.
   */
  public boolean tick(int field, int amount) {
    if (!enabled) return false;
    if (amount == 0) return false;
    else if (amount > 0) synchronized (this) {
      // Moves forward
      now.add(field, amount);
      // Check alarm and, if triggered, set next alarm
      if (stop != null && stop.compareTo(now) <= 0) {
        // Stop time reached
        count++;
        now = (Calendar) stop.clone();
        next_alarm = (Calendar) stop.clone();
        enabled = false;
        return true;
      } else if (next_alarm != null) {
        // Alarm time reached
        if (next_alarm.compareTo(now) <= 0) {
          while (next_alarm.compareTo(now) <= 0) {
            count++;
            next_alarm.add(interval_field, interval_amount);
          }
          return true;
        }
      }
    } else {
      // Moves backward
      throw new IllegalArgumentException("Time cannot goes backward... yet.");
    }
    return false;
  }

  /**
   * Moves forward by one second
   * @return true if alarm is triggered.  false otherwise.
   */
  public boolean tick() {
    return tick(Calendar.SECOND, 1);
  }
  
  public int getCount() {
    return count;
  }

  public Long getRunTime() {
    return (started.getTimeInMillis()-now.getTimeInMillis())/1000;
  }

  /**
   * Remaining time until this timer stops
   *
   * @return remaining run time in second, null if forever
   */
  public Long getRemainingTime() {
    if (stop == null) return null;
    return (stop.getTimeInMillis() - now.getTimeInMillis()) / 1000;
  }

  /**
   * Next alarm time in second
   *
   * @return next alarm time / stop time in second, null if never
   */
  public Long getNextAlarmTime() {
    if (next_alarm == null && stop == null) return null;
    return (((next_alarm == null)
             ? stop.getTimeInMillis()
             : (stop != null)
               ? next_alarm.getTimeInMillis()
               : Math.min(next_alarm.getTimeInMillis(), stop.getTimeInMillis()))
            -now.getTimeInMillis())/1000;
  }


  public String toString() {
    StringBuffer buf = new StringBuffer(enabled ? "> " : "X ");
    Long remaining = getRemainingTime();
    if (remaining != null) buf.append(Duration.format(remaining));
    if (next_alarm != null) {
      Long next = getNextAlarmTime();
      if (remaining != null && next != null) buf.append(" | ");
      if (next != null) buf.append(Duration.format(next));
    }
    if (alarm != null) buf.append("\t").append(alarm);
    return buf.toString();
  }

  public synchronized void restart() {
    count = 0;
    started.setTime(now.getTime());
    if (interval_amount > 0) {
      next_alarm.setTime(now.getTime());
      next_alarm.add(interval_field, interval_amount);
    }
  }
}
