package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DatabaseResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"db_reference",
                     "db_reference"}, // Name of reference database, localisable only if the new db exists
            {"random_none",
                     "(None)"},
            {"random_result",
                     "Random result of {0}: {1}"},
            {"random_dual_result",
                     "Random result of {0}: {1} with {2}"},

            {"errConnectFail",
                     RED+"無法連線到數據庫"},
            {"errTermNotFound",
                     RED+"找不到此項目"},
            {"errTooManyTerms",
                     RED+"太多符合的項目，給我多一些字母好嗎？"},
            {"errTableNotFound",
                     RED+"Table not found"},
            {"errTooManyTable",
                     RED+"Too many matches.  Can I have some more letters?"},
            {"errInvalidData",
                     RED+"數據不全。請重新建立 term 項目索引表格。"},

            {"help_summary",
                     BOLD+"數據庫指令"+BOLD+": .dic .dict .rand .random\n"},
            {"help_dic",
                     BOLD+"查找參考條目"+BOLD+", 用法: .dic (條目)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_dict",
                    BOLD+"查找參考條目"+BOLD+", 用法: .dict (條目)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_rand",
                     BOLD+"投擲亂數表"+BOLD+", 用法: .rand (表格)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_random",
                     BOLD+"投擲亂數表"+BOLD+", 用法: .random (表格)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_db",
                     BOLD+"使用數據庫"+BOLD+", 用法: .db [指令] (項目) [內容]\n" +
                     "此指令尚未實作"},
            {"help_dbh",
                     BOLD+"秘密使用數據庫"+BOLD+", 用法: 同 .db, 但結果以私訊傳逹"},
    };
  }
}
