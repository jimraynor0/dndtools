package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class ChannelResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"invited", "奉 {0} 之邀加入"},
            {"outputSet", "輸出設定為 {0}"},
            {"outputUnset", "輸出設定已經取消"},
            {"auto-op_on", "開啟自動 Op"},
            {"auto-op_off", "關閉自動 Op"},
            {"auto-op_none", "取消本頻道自動 Op 設定, 使用全域設定"},
            {"auto-op_all_on", "開啟全頻道自動 Op"},
            {"auto-op_all_off", "關閉全頻道自動 Op"},

            {"errNotAdmin", RED +"此指令限管理員使用.  請私訊 \".set admin (管理員密碼)\" 來確認你的管理員資格."},
            {"errNotOp", RED +"此指令限管理員和頻道管理員使用.  請私訊 \".set admin (管理員密碼)\" 來確認你的管理員資格."},
            {"errTooManyChannel", RED +"無法再加入新頻道。如需投骰，請用 .here 指定頻道然後私信指令。" +
                    RED+"你也可以再試一次看看我能否脫離閒置的頻道。"},
            {"errNotInChannel", RED +"我不在那頻道裡，先邀請我好嗎？"},

            {"help_summary",
                    BOLD +"頻道指令"+BOLD +": .here .join .part .auto-op"},

            {"help_here",
                    BOLD +"設定指令結果輸出地"+BOLD +", 用法: .here [頻道]" },
            {"help_join",
                    BOLD +"加入頻道"+BOLD +", 用法: .join (頻道) "+DARK_GRAY +"(限私信)" },
            {"help_part",
                    BOLD +"離開頻道"+BOLD +", 用法: .part (頻道) "+DARK_GRAY +"(限管理員或頻道管理員私信)" },
            {"help_auto-op",
                    BOLD +"自動 Op 用戶"+BOLD +", 用法: .auto-op [頻道] "+DARK_GRAY +"(限管理員或頻道管理員)\n" +
                    "重覆指令以關閉此功能" },
    };
  }
}
