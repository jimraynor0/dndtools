package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class LogResource_zh_cn extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"logStart", "开始记录此频道"},
            {"logStop", "停止记录此频道"},
            {"logResume", "恢复记录此频道"},
            {"logReset", "重新开始记录此频道, 旧记录己清除"},
            {"logClear", "己清除此频道的记录"},
            {"logGet", "正在传送 {1} 频道的 {0} 记录。如无法接收请检查防火墙／路由器设定。"},

            {"errNoLog", RED+"此频道没有记录，要开始记录请用 "+BOLD+"start"},
            {"errAlreadyLogging", RED+"此频道现正被记录，要重新开始记录请用 "+BOLD+"restart"},
            {"errAlreadyHasLog", RED+"此频道已经停止记录，要恢复请用 "+BOLD+"resume"+BOLD+"，或用 "+BOLD+"restart"+BOLD+" 重新开始记录"},
            {"errUnknownType", RED+"不明记录格式。支援的格式有 xml, html, bbc, 和 txt"},
            {"errSaveLogFail", RED+"记录储存失败，无法传送记录"},
            
            {"help_summary",
                    BOLD+"记录指令"+BOLD+": .log\n"},
            {"help_log",
                    BOLD+"控制记录"+BOLD+", 用法: .log ( start | begin | stop | end | resume | restart | clear )\n" +
                    BOLD+"传送记录"+BOLD+", 用法: .log get [ xml | html | bbc | txt ]"},
    };
  }
}