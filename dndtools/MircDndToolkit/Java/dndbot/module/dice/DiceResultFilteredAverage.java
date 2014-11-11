package dndbot.module.dice;

import org.jibble.pircbot.Colors;


/**
 * Result of a roll with some dice dropped
 *
 * Craeted by Ho Yiu YEUNG on 2008年4月18日
 */
public class DiceResultFilteredAverage extends DiceResultFiltered {

  public DiceResultFilteredAverage(int times, int face, ExpressionContext context, ExpressionPartDice.DiceOpt modifier) {
    super(times, face, context, modifier);
  }

  protected void roll() {
    /*
    int ignore_high;
    int ignore_low;
    int reroll_below;
    int reroll_above;
    int explode_above;
    */
    // Basic part
    double min = 1 + reroll_below;
    double max = face - reroll_above;
    double result = (min+max)/2.0;
    filteredResult = "(";
    filteredResult += times != 1 ? (ExpressionContext.FORMAT.format( times ) + "*") : "";
    filteredResult += ExpressionContext.FORMAT.format( result );
    // Explode added value
    if (explode_above > 0) {
      double explode_chance = 1.0 / (max - min + 1.0 );
      double accumulated_chance = explode_chance;
      double current_chance = explode_chance;
      for (int i = 0; i < 10; i++) {
        current_chance *= explode_chance;
        accumulated_chance += current_chance;
      }
      accumulated_chance *= result;
      filteredResult += "+" + ExpressionContext.FORMAT.format( accumulated_chance );
      result = ( result * times ) + accumulated_chance;
    } else {
      result *= times;
    }
    total = result;
    filteredResult += ")";
  }
}