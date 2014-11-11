package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class TimeResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"timer_stopped", "停止計時器。"},
            {"timer_paused", "暫停計時器。"},
            {"timer_running", "計時器開始計時"},
            {"timer_restarted", "重設計時器"},
            {"timer_replaced", "計時器被設立，已取代舊計時器"},
            {"timer_created", "計時器設立"},
            {"alarm", "叮～"},
            {"timer_alert", "計時器{1} 嚮鬧 {2}: {0} ("+BOLD+".timer{1} stop"+BOLD+" 可停止響鬧)"},
            {"timer_final_alert", "計時器{1} 終結嚮鬧: {0}"},
            {"no_timer", "計時器未被設立喔"},

            {"errUnspecifiedTimer", RED+"沒有提定計時器，請用 timer0, timer1, timer2 ... timer9"},
            {"errTimerNotFound", RED+"找不到計時器"},
            {"errAlarmTooFrequent", RED+"響鬧太頻密, 請增大響鬧間隔"},

            {"help_summary",
                    BOLD+"計時器指令"+BOLD+": .timer\n"},
            {"help_timer",
                    BOLD+"設立計時器"+BOLD+", 用法: .timer[0-9] (時間) [響鬧間隔] [響鬧文字]\n" +
                    "時間／間隔設定: (數量)(單位)\n" +
                    BOLD+"控制計時器"+BOLD+", 用法: .timer[0-9 | *] [ stop | pause | go | restart ]"
            },
    };
  }
}