package dndbot.resource;

import static org.jibble.pircbot.Colors.BLACK;
import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.RED;
import static org.jibble.pircbot.Colors.WHITE;

import java.util.ListResourceBundle;

import dndbot.irc.IRC;

public class CoreResource extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"list", "This is "+IRC.version + ", for command list please say "+BOLD+".help"},

            {"setNone", "\"{0}\" is now unset"},
            {"setDone", "\"{0}\" is set to \"{1}\""},

            {"on", "is now on"},
            {"off", "is now off"},
            {"gc", "Started recycling..."},
            {"gcDone", "Finished, recycled {0} bytes"},
            {"resetting", "Resetting..."},
            {"resetted", "Resetted"},
            {"reconnecting", "Reconnecting on command of {0}..."},
            {"reconnected", "Reconnected"},
            {"shutdown", "Shutting down"},
            {"private_help", "You may browse the helps with private message instead of in public channel."},

            {"errNoCmd", RED+"Command not found"},
            {"errNoHelp", RED+"Help for command {0} not found"},
            {"errNotInChannel", RED+"Please invite me into channel first, or specify a channel where we both joined."},
            {"errNotAdmin", RED+"Only administrator can command that.  Private message \".set admin (password)\" to authorise yourself."},
            {"errOldJre", RED+"Java Runtime Environment not sufficiently up to date, version 6 recommended. Some functionality may not be available.\n" +
                    RED+"Lastest JRE can be downloaded from http://java.sun.com/javase/downloads/index.jsp"},

            {"help_summary",
                    BOLD+"User/System commands"+BOLD+": .set .nick .me .gc .reconnect .reset .shutdown"},
            {"help_help",
                    BOLD+"Show help"+BOLD+", Usage: .help (command)\n" +
                    "Legend: [...] optional parameters "+WHITE+";"+BLACK+" (...) mandatory parameters"},


            {"help_set",
                    BOLD+"Settings"+BOLD+", Usage: .set (setting) (value)" +
                    "Channel/personal setting \"colour\" - Set to 'off' to disable colour and formatting to you or to the channel.\n" +
                    "Channel setting \"disabled\" - Set to non-blank to disable most command processing\n" +
                    "Personal setting \"admin\" - Set to admin password to execute admin commands\n" +
                    "Personal setting \"DM\" - Set to DM's nick to send him/her hidden roll result"},
            {"help_nick",
                    BOLD+"Set nickname"+BOLD+", Usage: .nick (new nickname) "+DARK_GRAY+"(Administrator private message only)" },
            {"help_me",
                    BOLD+"Perform action"+BOLD+", Usage: .me (action) "+DARK_GRAY+"(Administrator private message only)\n"+
                    "Action channel can be set with .here" },
            {"help_gc",
                    BOLD+"Recycle memory"+BOLD+", Usage: .gc "+DARK_GRAY+"(Administrator private message only)"},
            {"help_reconnect",
                    BOLD+"Disconnect and reconnect"+BOLD+", Usage: .reconnect "+DARK_GRAY+"(Administrator private message only)"},
            {"help_dndbot",
                    BOLD+"DnDBot targeting command prefix"+BOLD+", Usage: .dndbot (command)\n"+
                    "e.g. \".dndbot off\" equals to \".off\", but only DnDBot will response."},
            {"help_off",
                    BOLD+"Disable DnDBot"+BOLD+", Usage: .off [channel]\n"+
                    "When disabled, DnDBot will not response to non-core, non-channel commands in specified channel or in this channel."},
            {"help_on",
                    BOLD+"Enable DnDBot"+BOLD+", Usage: .on [channel]\n"+
                    "See: .off"},
            {"help_reset",
                    BOLD+"Reset bot"+BOLD+", Usage: .reset "+DARK_GRAY+"(Administrator private message only)"},
            {"help_shutdown",
                    BOLD+"Shutdown bot"+BOLD+", Usage: .shutdown [message] "+DARK_GRAY+"(Administrator private message only)"},
    };
  }
}
