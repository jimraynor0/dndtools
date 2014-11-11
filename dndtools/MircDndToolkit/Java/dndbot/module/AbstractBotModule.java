package dndbot.module;

import java.util.Locale;
import java.util.ResourceBundle;

import org.sheepy.pirc.PircAdapter;

import dndbot.irc.IRC;
import dndbot.resource.ResourceLoader;

/**
 * BotModule with basic implementation
 */
public abstract class AbstractBotModule extends PircAdapter implements BotModule {
  protected ResourceBundle res;
  protected final IRC irc;

  protected AbstractBotModule(IRC irc) {
    this.irc = irc;
    res = loadBundle(getClass(), irc.locale);
  }

  /**
   * Reset module.  Reload (language) resource.
   */
  public void reset() {
    res = loadBundle(getClass(), irc.locale);
  }

  public static ResourceBundle loadBundle(Class<?> ofClass, Locale locale) {
    return ResourceLoader.getBundle(ofClass.getSimpleName()+"Resource", locale);
  }

  /**
   * Get help for given command
   * Default will load and return "help_command" string resource
   *
   * @param command Command to get help for
   * @return Help if any, null if not found
   */
  public String getHelp(String command) {
    if (command.startsWith(".")) command = command.substring(1);
    try {
      return res.getString("help_"+command);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Return same pattern as channel commands
   * @return command pattern
   * @see #getCommandPattern()
   */
  public String getDirectCommandPattern() {
    return getCommandPattern();
  }

  /**
   * Return same commands as channel commands
   * @return list of commands
   * @see #getCommand()
   */
  public String[] getDirectCommand() {
    return getCommand();
  }

  /**
   * If return on, this module's commands ignore on/off setting
   * @return boolean
   */
  public boolean isAlwaysOn() {
    return false;
  }
}
