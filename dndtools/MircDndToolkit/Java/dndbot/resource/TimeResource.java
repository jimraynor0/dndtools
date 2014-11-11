package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class TimeResource extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"timer_stopped", "Timer stopped and discarded."},
            {"timer_paused", "Timer paused"},
            {"timer_running", "Timer running"},
            {"timer_restarted", "Timer restarted."},
            {"timer_replaced", "Timer created.  Old timer replaced."},
            {"timer_created", "Timer created."},
            {"alarm", "Ding from timer"},
            {"timer_alert", "Alert {2} from timer {1}: {0} ("+BOLD+".timer{1} stop"+BOLD+" to stop)"},
            {"timer_final_alert", "Final alert from timer {1}: {0}"},
            {"no_timer", "I have got no timers for you."},

            {"errUnspecifiedTimer", RED+"Timer unspecified, use timer0, timer1, timer2 ... timer9 instead"},
            {"errTimerNotFound", RED+"Timer not found."},
            {"errAlarmTooFrequent", RED+"Alarm too frequent, please increase alarm interval"},

            {"help_summary",
                    BOLD+"Timer commands"+BOLD+": .timer\n"},
            {"help_timer",
                    BOLD+"Create timer"+BOLD+", Usage: .timer[0-9] (duration) [alarm interval] [alarm text]\n" +
                    "Duration / interval syntax: (number)(unit)\n" +
                    BOLD+"Control timer"+BOLD+", Usage: .timer[0-9 | *] [ stop | pause | go | restart ]"
            },
    };
  }
}