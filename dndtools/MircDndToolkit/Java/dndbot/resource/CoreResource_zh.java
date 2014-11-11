package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

import dndbot.irc.IRC;

public class CoreResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"list", "這是 "+IRC.version + ", 指令清單詳見 "+BOLD+".help"},

            {"setNone", "移除 \"{0}\" 設定"},
            {"setDone", "\"{0}\" 設定為 \"{1}\""},

            {"on", "爬過來, 奉上骰子"},
            {"off", "奉命靜靜蹲到一角去"},
            {"gc", "開始回收"},
            {"gcDone", "回收完畢, 回收了 {0} 位元組"},
            {"resetting", "重設中..."},
            {"resetted", "重設完畢"},
            {"reconnecting", "依 {0} 之令重連線中..."},
            {"reconnected", "恢復連線"},
            {"shutdown", "退出中"},
            {"private_help", "你可以用私訊向我求助而不需要在頻道大喊"},

            {"errNoCmd", RED+"找不到指令"},
            {"errNoHelp", RED+"找不到 {0} 指令的說明"},
            {"errNotInChannel", RED+"請先邀請我進入頻道, 或指定一個你我都在場的頻道."},
            {"errNotAdmin", RED+"此指令限管理員使用.  請私訊 \".set admin (管理員密碼)\" 來確認你的管理員資格."},
            {"errOldJre", RED+"爪哇執行環境不夠新, 建議使用版本 6. 一些功能可能無法運作.\n" +
                    RED+"下載最新的爪哇執行環境 (JRE) 請到 http://java.sun.com/javase/downloads/index.jsp"},

            {"help_summary",
                    BOLD+"用戶及系統指令"+BOLD+": .set .here .nick .me .gc .reconnect .reset .shutdown"},
            {"help_help",
                    BOLD+"顯示說明"+BOLD+", 用法: .help (指令)\n" +
                    "指令用法表示： [...] 代表可選參數, (...) 代表必要參數"},


            {"help_set",
                    BOLD+"個人設定"+BOLD+", 用法: .set (選項) (設定值)\n" +
                    "頻道/個人設定 colour - 把此设定为 off 會移除一切顏式及格式輸出\n" +
                    "頻道設定  disabled - 設定為非空白會關閉大部份指令的處理\n" +
                    "個人設定 admin - 把此設定為管理員密碼以執行管理員指令\n" +
                    "個人設定 DM - 把此設定為 DM 暱稱對方即可接收秘密投骰"},
            {"help_nick",
                    BOLD+"設定暱稱"+BOLD+", 用法: .nick (新暱稱) "+DARK_GRAY+"(限管理員私信)\n"},
            {"help_me",
                    BOLD+"作出動作"+BOLD+", 用法: .me (行動) "+DARK_GRAY+"(限管理員私信)\n"+
                    "動作輸出頻道可以用 .here 設定"},
            {"help_gc",
                    BOLD+"回收記憶體"+BOLD+", 用法: .gc "+DARK_GRAY+"(限管理員私信)"},
            {"help_reconnect",
                    BOLD+"斷線並重連線"+BOLD+", 用法: .reconnect "+DARK_GRAY+"(限管理員私信)"},
            {"help_reset",
                    BOLD+"重設機器人"+BOLD+", 用法: .reset "+DARK_GRAY+"(限管理員私信)"},
            {"help_shutdown",
                    BOLD+"終結機器人"+BOLD+", 用法: .shutdown [訊息] "+DARK_GRAY+"(限管理員私信)"},
    };
  }
}
