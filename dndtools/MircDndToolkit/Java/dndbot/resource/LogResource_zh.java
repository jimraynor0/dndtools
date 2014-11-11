package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class LogResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"logStart", "開始記錄此頻道"},
            {"logStop", "停止記錄此頻道"},
            {"logResume", "恢復記錄此頻道"},
            {"logReset", "重新開始記錄此頻道, 舊記錄己清除"},
            {"logClear", "己清除此頻道的記錄"},
            {"logGet", "正在傳送 {1} 頻道的 {0} 記錄。如無法接收請檢查防火牆／路由器設定。"},

            {"errNoLog", RED+"此頻道沒有記錄，要開始記錄請用 "+BOLD+"start"},
            {"errAlreadyLogging", RED+"此頻道現正被記錄，要重新開始記錄請用 "+BOLD+"restart"},
            {"errAlreadyHasLog", RED+"此頻道已經停止記錄，要恢復請用 "+BOLD+"resume"+BOLD+"，或用 "+BOLD+"restart"+BOLD+" 重新開始記錄"},
            {"errUnknownType", RED+"不明記錄格式。支援的格式有 xml, html, bbc, 和 txt"},
            {"errSaveLogFail", RED+"記錄儲存失敗，無法傳送記錄"},
            
            {"help_summary",
                    BOLD+"記錄指令"+BOLD+": .log\n"},
            {"help_log",
                    BOLD+"控制記錄"+BOLD+", 用法: .log ( start | begin | stop | end | resume | restart | clear )\n" +
                    BOLD+"傳送記錄"+BOLD+", 用法: .log get [ xml | html | bbc | txt ]"},
    };
  }
}