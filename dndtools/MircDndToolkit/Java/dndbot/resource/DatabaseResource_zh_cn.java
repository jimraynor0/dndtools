package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DatabaseResource_zh_cn extends ListResourceBundle {

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
                     RED+"无法连线到数据库"},
            {"errTermNotFound",
                     RED+"找不到此项目"},
            {"errTooManyTerms",
                     RED+"太多符合的项目，给我多一些字母好吗？"},
            {"errTableNotFound",
                     RED+"Table not found"},
            {"errTooManyTable",
                     RED+"Too many matches.  Can I have some more letters?"},
            {"errInvalidData",
                     RED+"数据不全。请重新建立 term 项目索引表格。"},

            {"help_summary",
                     BOLD+"数据库指令"+BOLD+": .dic .dict .rand .random\n"},
            {"help_dic",
                     BOLD+"查找参考条目"+BOLD+", 用法: .dic (条目)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_dict",
                    BOLD+"查找参考条目"+BOLD+", 用法: .dict (条目)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_rand",
                     BOLD+"投掷乱数表"+BOLD+", 用法: .rand (表格)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_random",
                     BOLD+"投掷乱数表"+BOLD+", 用法: .random (表格)\n" +
                        "可用 * 替代任何字串, ? 替代任何字符"},
            {"help_db",
                     BOLD+"使用数据库"+BOLD+", 用法: .db [指令] (项目) [内容]\n" +
                     "此指令尚未实作"},
            {"help_dbh",
                     BOLD+"秘密使用数据库"+BOLD+", 用法: 同 .db, 但结果以私讯传逹"},
    };
  }
}