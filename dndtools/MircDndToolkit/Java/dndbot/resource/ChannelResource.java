package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class ChannelResource extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"invited", "has been invited by {0}"},
            {"outputSet", "Output channel is set to {0}"},
            {"outputUnset", "Output channel is now unset"},
            {"auto-op_on", "Auto-op on"},
            {"auto-op_off", "Auto-op off"},
            {"auto-op_none", "Channel Auto-op unset, use global"},
            {"auto-op_all_on", "Global auto-op on"},
            {"auto-op_all_off", "Global auto-op off"},

            {"errNotAdmin", RED +"Only administrator can command that.  Private message \".set admin (password)\" to authorise yourself."},
            {"errNotOp", RED +"Only administrator or channel operator can command that.  Private message \".set admin (password)\" to authorise yourself as administrator."},
            {"errTooManyChannel", RED +"I've joined too many channels.  For dice rolling, use .here and issue command with private message.\n" +
                    RED+"You can also try again to see whether I managed to free myself from idle channels"},
            {"errNotInChannel", RED +"I am not in that channel.  Invite me first."},

            {"help_summary",
                    BOLD +"Channel commands"+BOLD +": .here .join .part .auto-op"},

            {"help_here",
                    BOLD +"Set output channel"+BOLD +", Usage: .here [channel]" },
            {"help_join",
                    BOLD +"Join a channel"+BOLD +", Usage: .join (channel) "+DARK_GRAY +"(private message only)" },
            {"help_part",
                    BOLD +"Part a channel"+BOLD +", Usage: .part (channel) "+DARK_GRAY +"(Administrator or channel operator private message only)" },
            {"help_auto-op",
                    BOLD +"Op users automatically"+BOLD +", Usage: .auto-op [channel] "+DARK_GRAY +"(Administrator or channel operator only)\n" +
                    "Repeat command to disable" },
    };
  }
}
