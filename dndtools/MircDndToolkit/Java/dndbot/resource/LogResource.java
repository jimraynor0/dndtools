package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class LogResource extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"logStart", "Started logging this channel."},
            {"logStop", "Stopped logging this channel."},
            {"logResume", "Resumed logging this channel."},
            {"logReset", "Started logging this channel, previous log discarded."},
            {"logClear", "Cleared log of this channel."},
            {"logGet", "Sending {0} log of channel {1}, if undeliverable please check firewall / router config."},

            {"errNoLog", RED+"This channel is not being logged, use "+BOLD+"start"+BOLD+" to start logging."},
            {"errAlreadyLogging", RED+"This channel is being logged, use "+BOLD+"restart"+BOLD+" to clear and restart logging."},
            {"errAlreadyHasLog", RED+"Logging of this channel has been stopped, use "+BOLD+"resume"+BOLD+" or "+BOLD+"restart"+BOLD+" to resume or restart logging."},
            {"errUnknownType", RED+"Unknown log type.  Supported types are xml, html, bbc, and txt"},
            {"errSaveLogFail", RED+"Saving of log failed, log cannot be sent."},
            
            {"help_summary",
                    BOLD+"Log commands"+BOLD+": .log\n"},
            {"help_log",
                    BOLD+"Set log capture"+BOLD+", Usage: .log ( start | begin | stop | end | resume | restart | clear )\n" +
                    BOLD+"Get log"+BOLD+", Usage: .log get [ xml | html | bbc | txt ]"},
    };
  }
}