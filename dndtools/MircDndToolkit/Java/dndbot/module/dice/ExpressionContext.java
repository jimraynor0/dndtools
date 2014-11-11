package dndbot.module.dice;

import java.text.DecimalFormat;
import java.util.Random;

import org.jibble.pircbot.Colors;

/**
 * Craeted by Ho Yiu YEUNG on 2008年4月9日
 */
public class ExpressionContext {
  public static final DecimalFormat FORMAT = new DecimalFormat("0.####");
  public static enum GodsHand {
    NONE { float rerollChance(float last, float now){ return 0; } } ,
    WEEK { float rerollChance(float last, float now){
      float difference = Math.abs(last-now);
      float rate1 = (difference < 0.13f) ? (0.13f-difference) * 3f : 0; // d20: 39%, 24%, 9%
      float rate2 = (now <= 0.2f) ? 0.21f - now : (now >= 0.6f) ? -rate1*0.2f : 0 ;
      return Math.min(rate1 + rate2, 0.7f);
    } } ,
    STRONG { float rerollChance(float last, float now){
      float difference = Math.abs(last-now);
      float rate1 = (difference < 0.21f) ? (0.21f-difference) * 4f : 0; // d20: 84%, 64%, 44%, 24%, 4%
      float rate2 = (now <= 0.25f) ? (0.26f - now) * 1.5f : (now >= 0.6f) ? -rate1*0.33f : 0 ;
      return Math.min(rate1 + rate2, 0.8f);
    } } ;
    
    abstract float rerollChance(float last, float now);
  }

  Random rand;
  int defaultFace; // Also applies as
  boolean rollAverage; // If true, we are calculating global average
  int diceCount; // Set by parts on setContext and checkRoll.  Needed to correctly format output.
  public DiceResult dices; // All dice filteredResult, appended by new DiceResult
  public float lastContextRoll; // Currently always last user roll (in raw random bit), may add last room roll but does not seems helpful.
  public GodsHand adjustRoll; // If not NONE then try to adjust randomness

  public static String NormalExpColour = Colors.DARK_GREEN;
  public static String NormalResultColour = Colors.OLIVE;

  public ExpressionContext(Random rand, int defaultFace, boolean average) {
    this.rand = rand;
    this.defaultFace = defaultFace;
    rollAverage = average;
  }

  public String formatNumber(double in) {
    return FORMAT.format(in);
  }

  public int getDiceCount() {
    return diceCount;
  }

  public void clearRollHistory() {
    if (dices.rolls.length > 0)
	   dices.rolls = new int[0];
  }
}
