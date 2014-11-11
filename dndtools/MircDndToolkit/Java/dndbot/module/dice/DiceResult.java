package dndbot.module.dice;

import org.jibble.pircbot.Colors;

/**
 * Result of standard dice rolling, without any modifiers
 *
 * Craeted by Ho Yiu YEUNG on 2008年4月18日
 */
public class DiceResult extends ExpressionPart {
  protected int times; // Calculated final times.  So that we don't need a new object for that '1'
  protected int face; // Calculated final faces
  protected Double total; // Calculatted roll total
  protected ExpressionContext context; // Roll context
  protected int[] rolls; // Roll filteredResult

  private static final int MAX_DICE = 999;

  public DiceResult(int times, int face, ExpressionContext context) {
    this.times = times;
    this.face = face;
    this.context = context;
  }

  /**
   * Roll dice and store filteredResult sum in this.total
   */
  protected void roll() {
//    if (isRolled()) return;
    if (times > MAX_DICE) throw new FormulaException("errTooManyDice");
    if (times < 0) throw new FormulaException("errNoNegativeDice");
    rolls = new int[times];
    int total = 0;
    for (int i = times -1; i >= 0; i--) {
      int roll = genNextRoll();
      rolls[i] = roll;
      total += roll;
    }
    if (times > 0) { // Append to context dice filteredResult
      int l = context.dices.rolls.length;
      int[] newList = new int[l + times];
      System.arraycopy(context.dices.rolls, 0, newList, 0, l);
      System.arraycopy(rolls, 0, newList, l, times);
      context.dices.rolls = newList;
    }
    this.total = (double) total;
  }

  protected int genNextRoll() {
    int roll = context.rand.nextInt(face) + 1;
    if (context.adjustRoll == ExpressionContext.GodsHand.NONE || Float.isNaN(context.lastContextRoll)) {
      context.lastContextRoll = roll/face;
      return roll;
    }
    // God's hand on. Adjust roll.
    final float lastRoll = context.lastContextRoll;
    float rerollChance = context.adjustRoll.rerollChance(lastRoll, (float)roll/(float)face);
//    System.out.println(Math.abs(lastRoll - (float)roll/(float)face) + "\tReroll chance " + rerollChance + "\t Rolled a "+roll);
    while (rerollChance > 0) {
      if (context.rand.nextFloat() <= rerollChance) {
        roll = context.rand.nextInt(face) + 1;
        rerollChance = context.adjustRoll.rerollChance(lastRoll, (float)roll/(float)face);
//        System.out.println("Reroll triggered");
//        System.out.println(Math.abs(lastRoll - (float)roll/(float)face) + "\tReroll chance " + rerollChance + "\t Rolled a "+roll);
      } else break;
    }
    context.lastContextRoll = (float)roll/(float)face;
    return roll;
  }

  /*
  private boolean isRolled() {
    // Not fully implemented, remember to override in subclass if this is needed
    return rolls == null;
  }
  */

  public void setContext(ExpressionContext context) {
    this.context = context;
  }

  public String asFormula() {
//    roll();
//    return Colors.DARK_BLUE+context.formatNumber(times) +"d"+ context.formatNumber(face)+ExpressionContext.NormalExpColour;
    return "";
  }

  public String asResult() {
//    roll();
    StringBuilder s = new StringBuilder((int)Math.round(times)*5);
    s.append('(');

    char lastState = ' '; // Last roll colour. ' ' is none, 'n' for normal roll, 'h' for high highlight, 'i' for inactive, 'l' for low highlight
    char state = ' ';
    for (int r : rolls) {
      if (face >= 20) state = r == face ? 'h' : ( r == 1 ? 'l' : 'n'); 
      else state = 'n';
      if (state != lastState)
        if (state == 'h')
          s.append(Colors.RED);
        else if (state == 'l')
          s.append(Colors.RED);
        else
          s.append(Colors.PURPLE);
      s.append(r).append(',');
      lastState = state;
    }

    if (s.length() > 1) s.setLength(s.length()-1);
    s.append(ExpressionContext.NormalResultColour).append(')');
    return s.toString();
  }

  public double calc() {
//    roll();
    return total;
  }

  public ExpressionPart simplify() {
    return this;
  }
}
