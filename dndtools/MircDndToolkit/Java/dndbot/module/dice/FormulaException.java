package dndbot.module.dice;

import java.text.MessageFormat;

/**
 * Craeted by Ho Yiu YEUNG on 2008年4月19日
 */
public class FormulaException extends RuntimeException {
  public final String[] param;

  public FormulaException(String message, String ... param) {
    super(message);
    this.param = param;
  }

  public FormulaException(Throwable cause, String message, String ... param) {
    super(message, cause);
    this.param = param;
  }

  public String format(String in) {
     return MessageFormat.format(in, (Object[])param);
  }
}
