package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class TimeResource_zh_cn extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"timer_stopped", "停止计时器。"},
            {"timer_paused", "暂停计时器。"},
            {"timer_running", "计时器开始计时"},
            {"timer_restarted", "重设计时器"},
            {"timer_replaced", "计时器被设立，已取代旧计时器"},
            {"timer_created", "计时器设立"},
            {"alarm", "叮～"},
            {"timer_alert", "计时器{1} 向闹 {2}: {0} ("+BOLD+".timer{1} stop"+BOLD+" 可停止响闹)"},
            {"timer_final_alert", "计时器{1} 终结向闹: {0}"},
            {"no_timer", "计时器未被设立喔"},

            {"errUnspecifiedTimer", RED+"没有提定计时器，请用 timer0, timer1, timer2 ... timer9"},
            {"errTimerNotFound", RED+"找不到计时器"},
            {"errAlarmTooFrequent", RED+"响闹太频密, 请增大响闹间隔"},

            {"help_summary",
                    BOLD+"计时器指令"+BOLD+": .timer\n"},
            {"help_timer",
                    BOLD+"设立计时器"+BOLD+", 用法: .timer[0-9] (时间) [响闹间隔] [响闹文字]\n" +
                    "时间／间隔设定: (数量)(单位)\n" +
                    BOLD+"控制计时器"+BOLD+", 用法: .timer[0-9 | *] [ stop | pause | go | restart ]"
            },
    };
  }
}