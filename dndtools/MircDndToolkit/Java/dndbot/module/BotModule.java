package dndbot.module;

/**
 */
public interface BotModule {
  /**
   * Reset the module.
   * The module should attempt to reload language resource, recompile patterns, and reconnect database.
   * User settings and module settings should be preserved.
   */
  public void reset();

  /**
   * Get regular expression to match all channel command
   * @return non-zero length channel command pattern, or null for none
   */
  public String getCommandPattern();

  /**
   * Get regular expression to match all private command
   * @return non-zero length direct command pattern, or null for none
   */
  public String getDirectCommandPattern();

  /**
   * Get a list of channel command
   * @return list of channel command, maybe empty
   */
  public String[] getCommand();

  /**
   * Get a list of private message command
   * @return list of private message command, maybe empty
   */
  public String[] getDirectCommand();

  /**
   * Called to process a command.
   * Should set evt.processed to true if the command is really processed
   * @param evt Command event
   */
  public void onCommand(ModuleEvent evt);

  /**
   * Get help of given command
   * @param command Command to get help of, or "summary" for command list
   * @return Command help or command list
   */
  public String getHelp(String command);

  /**
   * If return on, this module's commands ignore on/off setting
   * @return boolean
   */
  public boolean isAlwaysOn();
}
