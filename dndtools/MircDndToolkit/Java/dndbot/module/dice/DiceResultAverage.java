package dndbot.module.dice;

import org.jibble.pircbot.Colors;

/**
 * Result of average of normal roll
 *
 * Craeted by Ho Yiu YEUNG on 2008年4月18日
 */
public class DiceResultAverage extends DiceResult {

  public DiceResultAverage(int times, int face, ExpressionContext context) {
    super(times, face, context);
    total = times * ( face / 2 + 0.5 );
  }

  protected void roll() {
    // Override default.  We already got total sorted.
  }

  public String asFormula() {
    // Original forumla + modifier
    return super.asFormula()+ Colors.RED+"a"+ExpressionContext.NormalExpColour;
  }

  public String asResult() {
    if (times > 1)
      return "("+times+"*"+context.formatNumber(face/2+0.5)+")";
    else
      return "("+context.formatNumber(face/2+0.5)+")";
  }

}