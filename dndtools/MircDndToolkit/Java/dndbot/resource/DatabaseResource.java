package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DatabaseResource extends ListResourceBundle {

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
                     RED+"Failed to connect to database"},
            {"errTermNotFound",
                     RED+"Term not found"},
            {"errTooManyTerms",
                     RED+"Too many matches.  Can I have some more letters?"},
            {"errTableNotFound",
                     RED+"Table not found"},
            {"errTooManyTable",
                     RED+"Too many matches.  Can I have some more letters?"},
            {"errInvalidData",
                     RED+"Data integrity damaged.  Please rebuild term index table."},

            {"help_summary",
                     BOLD+"Database commands"+BOLD+": .dic .dict .rand .random"},
            {"help_dic",
                     BOLD+"Lookup reference"+BOLD+", Usage: .dic (keyword)\n" +
                        "Use * for any number of characters and ? for any character"},
            {"help_dict",
                     BOLD+"Lookup reference"+BOLD+", Usage: .dict (keyword)\n" +
                        "Use * for any number of characters and ? for any character"},
            {"help_rand",
                     BOLD+"Roll random table"+BOLD+", Usage: .rand (table)\n" +
                        "Use * for any number of characters and ? for any character"},
            {"help_random",
                     BOLD+"Roll random table"+BOLD+", Usage: .random (table)\n" +
                        "Use * for any number of characters and ? for any character"},
            {"help_db",
                     BOLD+"Use database"+BOLD+", Usage: .db [command] (keyword) [value]\n" +
                     "This feature is under construction"},
            {"help_dbh",
                     BOLD+"Lookup reference privatelly"+BOLD+", Usage: same as .db"},
    };
  }
}
