package dndbot.module;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.sheepy.util.StringUtil;

import dndbot.irc.IRC;
import dndbot.module.time.Duration;
import dndbot.module.time.Timer;

/**
 * Countdown and alarm module
 * <p/>
 * Created by Ho Yiu Yeung on Mar 27, 2007
 */
public class Time extends AbstractBotModule {
  private Map<String, Map<Integer, Timer>> timers;

  @SuppressWarnings("unchecked")
  public Time(final IRC irc) {
    super(irc);
    timers = (Map<String, Map<Integer, Timer>>) irc.data.get("timers");
    if (timers == null) {
      irc.data.put("timers", timers = new HashMap<String, Map<Integer,Timer>>());
      // The real timer, this timer make other timers ticks
      new java.util.Timer().scheduleAtFixedRate(new TimerTask(){
        private final Map<String, Map<Integer, Timer>> timers = (Map<String, Map<Integer, Timer>>) irc.data.get("timers"); 
        public void run() {
          synchronized(timers){
            if (timers.size() == 0) return;
            for (Iterator<Entry<String, Map<Integer, Timer>>> listIterator = timers.entrySet().iterator(); listIterator.hasNext(); ) {
              Entry<String, Map<Integer, Timer>> list = listIterator.next();
              for (Iterator<Entry<Integer, Timer>> timerIterator = list.getValue().entrySet().iterator(); timerIterator.hasNext(); ) {
                Entry<Integer, Timer> next = timerIterator.next();
                Timer t = next.getValue();
                String key = next.getKey().toString();
                if (key.equals("10")) key = ""; // Default alarm?
                if (t.tick()) {
                  irc.deliverMessage(list.getKey(), MessageFormat.format(
                      (t.enabled) ? res.getString("timer_alert") : res.getString("timer_final_alert"),
                      t.alarm, key, t.getCount()));
                  if (!t.enabled) {
                    timerIterator.remove();
                    if (list.getValue().size() <= 0)
                      listIterator.remove();
                  }
                }
              }
            }
          }
        }
      }, 2000, 1000); // 'Tick' once a second, starting after two second
    }
    irc.addListener(this);
  }

  public void reset() {
    irc.removeListener(this);
  }

  public String getCommandPattern() {
    return "timer(?:\\d|\\*)?";
  }

  public String[] getCommand() {
    return new String[]{"timer","timer*","timer0","timer1","timer2","timer3","timer4","timer5","timer6","timer7","timer8","timer9"};
  }

  public void onCommand(ModuleEvent evt) {
    if (evt.command.startsWith("timer")) {
      // Load timer list and commanded timer
      Map<Integer, Timer> list = timers.get(evt.getWhere());
      Timer t = null;
      int key = StringUtil.strToInt(evt.command.substring(5), -1);
      if (evt.command.length() == 5)
        key = (evt.parameter.length() > 0) ? 10 : -1;  
      if (list != null && key > 0)
        t = list.get(key);
      if (key < 0 && (list == null || list.size() == 0)) {
        evt.sendMessage(evt.getLocation(), res.getString("no_timer"));
        return;
      }
      
      // LIST TIMER
      if (evt.parameter.length() <= 0) {
        if (key < 0) {
          // List all timers
          for (Map.Entry<Integer,Timer> timerEntry : list.entrySet()) {
            Timer te = timerEntry.getValue();
            String k = timerEntry.getKey().toString();
            if (k.equals("10")) k = "D"; // Default alarm?
            evt.sendMessage(evt.getLocation(), k+" - "+te.toString());
          }
        } else {
          if (!timerExists(t, evt)) return;
          // List single timer
          evt.sendMessage(evt.getLocation(), key+" - "+list.get(key).toString());
       }
      } else if (evt.parameter.indexOf(' ') < 0) {
        String param = evt.parameter.toLowerCase();
        
        // REMOVE TIMER
        if (param.equals("stop")) {
          if (key >= 0) {
            if (!timerExists(t, evt)) return;
            t.enabled = false;
            list.remove(key);
            if (list.size() == 0) synchronized (timers) {
              timers.remove(evt.getWhere());
            }
          } else {
            synchronized (timers) { timers.remove(evt.getWhere()); }
          }
          evt.sendMessage(evt.getLocation(), res.getString("timer_stopped"));
          
        // PAUSE TIMER
        } else if (param.equals("pause")) {
          if (key > 0) {
            if (!timerExists(t, evt)) return;
            t.enabled = false;
          } else {
            for (Timer timer : list.values())
              timer.enabled = false;
          }
          evt.sendMessage(evt.getLocation(), res.getString("timer_paused"));
          
        // UN-PAUSE TIMER
        } else if (param.equals("go")) {
          if (key > 0) {
            if (!timerExists(t, evt)) return;
            t.enabled = true;
          } else {
            for (Timer timer : list.values())
              timer.enabled = true;
          }
          evt.sendMessage(evt.getLocation(), res.getString("timer_running"));
          
        // RESTART TIMER
        } else if (param.equals("restart")) {
          if (key > 0) {
            if (!timerExists(t, evt)) return;
            t.restart();
            evt.sendMessage(evt.getLocation(), res.getString("timer_restarted"));
            evt.sendMessage(evt.getLocation(), key+"\t"+list.get(key).toString());
          } else {
            for (Timer timer : list.values())
              timer.enabled = false;
            evt.sendMessage(evt.getLocation(), res.getString("timer_restarted"));
          }
          
        // CREATE TIMER (Alarm)
        } else {
          if (key < 0) {
            evt.sendMessage(evt.getLocation(), res.getString("errUnspecifiedTimer"));
            return;
          }
          Duration duration = Duration.parseTime(param);
          if (duration != null) {
            Calendar now = Calendar.getInstance();
            Timer newTimer = new Timer(now, duration.add((Calendar) now.clone()));
            newTimer.alarm = res.getString("alarm");
            if (!addTimer(evt, key, newTimer)) return;
          } else {
          }
        }
        
      } else {
        // Create a countdown alarm?
        String[] param = evt.parameter.split("\\s+", 3);
        String desc = res.getString("alarm");
        Duration till = Duration.parseTime(param[0]); // end time
        Duration tick = null;
        try {
          tick = Duration.parseTime(param[1]);
          if (param.length > 2) desc = param[2];
          // On a public channel, more then once per 5s or more then 20 shorter then 1 minute alarm ?
          if (evt.channel != null && (tick.inSecond() < 5 || (tick.inSecond() < 60 && tick.inSecond()*20 < till.inSecond()))) {
            evt.sendMessage(evt.channel, res.getString("errAlarmTooFrequent"));
            return;
          }
        } catch (IllegalArgumentException e) {
          // Not a tick? Desc after all
          desc = (param.length > 2) ? param[1]+" "+param[2] : param[1];
        }
         
        Calendar now = Calendar.getInstance();
        Timer newTimer = (tick == null) 
            ? new Timer(now, till.add((Calendar) now.clone()))
            : new Timer(now, till.add((Calendar) now.clone()), tick.getField(), tick.getAmount());
        newTimer.alarm = desc;
        if (!addTimer(evt, key, newTimer)) return;
      }
      evt.processed = true;
    }
  }

  // Remove all timer on being kick
  public boolean onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    if (recipientNick.equalsIgnoreCase(irc.getNick()))
      synchronized (timers) {
        if (timers.containsKey(channel)) timers.remove(channel);
      }
    return super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
  }

  // Remove all timer on depart
  public boolean onPart(String channel, String sender, String login, String hostname) {
    if (sender.equalsIgnoreCase(irc.getNick()))
      synchronized (timers) {
        if (timers.containsKey(channel)) timers.remove(channel);
      }
    return super.onPart(channel, sender, login, hostname);
  }

  /**
   * Add a timer
   * @param evt Event, used to send message
   * @param key Key of timer
   * @param newTimer New timer
   * @return true if success (after processing the addition), false otherwise
   */
  private boolean addTimer(ModuleEvent evt, int key, Timer newTimer) {
    synchronized (timers) {
      Map<Integer, Timer> list = timers.get(evt.getWhere());
      if (list == null) {
        evt.sendMessage(evt.getLocation(), res.getString("timer_created"));
        timers.put(evt.getWhere(), list = new HashMap<Integer, Timer>());
      } else if (list.containsKey(key)) {
        evt.sendMessage(evt.getLocation(), res.getString("timer_replaced"));
        list.get(key).enabled = false;
      } else {
        evt.sendMessage(evt.getLocation(), res.getString("timer_created"));
      }
      list.put(key, newTimer);
    }
    return true;
  }

  /**
   * Verify the exustance of a timer exists
   * @param timer Acquired timer
   * @param evt Event, used to send message
   * @return true if not null, false if null (after sending error message)
   */
  private boolean timerExists(Timer timer, ModuleEvent evt) {
    if (timer == null) {
      evt.sendMessage(evt.getLocation(), res.getString("errTimerNotFound"));
      return false;
    }
    return true;
  }

}
