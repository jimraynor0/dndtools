package dndbot.module.dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jibble.pircbot.Colors;

/**
 * Result of a roll with some dice dropped
 *
 * Craeted by Ho Yiu YEUNG on 2008年4月18日
 */
public class DiceResultFiltered extends DiceResult {
  protected int ignore_high;
  protected int ignore_low;
  protected int reroll_below;
  protected int reroll_above;
  protected int explode_above;
  protected List<Integer> effectiveRolls;
  protected ExpressionPartDice.DiceOpt modifier;

  protected String filteredResult;

  public DiceResultFiltered(int times, int face, ExpressionContext context, ExpressionPartDice.DiceOpt modifier) {
    super(times, face, context);
    this.modifier = modifier;

    // Calculate ig
    String type = modifier.getType();
    int ih = 0, il = 0, value = 0;
    final ExpressionPart modParam = modifier.getModParam();
    value = (modParam == null) ? 1 : (int) Math.round(modParam.calc());
    if (type.equals("h")) {
      il = times- value;
    } else if (type.equals("l")) {
      ih = times- value;
    } else if (type.equals("ih")) {
      ih = value;
    } else if (type.equals("il")) {
      il = value;
    } else if (type.equals("ihl")) {
      ih = il = value;
    } else if (type.equals("b")) {
      reroll_below = value;
    } else if (type.equals("a")) {
      reroll_above = modParam == null ? face : value;
    } else if (type.equals("x")) {
      explode_above = modParam == null ? face : value; 
    }

    this.ignore_high = Math.max(ih, 0);
    this.ignore_low = Math.max(il, 0);
    if (reroll_below >= face) throw new IllegalArgumentException("Infinite roll");
    if (reroll_above != 0 && reroll_above <= 1) throw new IllegalArgumentException("Infinite roll");
  }

  protected void roll() {
	List<Integer> rollList = new ArrayList<Integer>(times);
	if (effectiveRolls == null)
		effectiveRolls = new ArrayList<Integer>(times);
	else
		effectiveRolls.clear();

    // Rolls. Also handle reroll above or below.
    for (int i = times -1; i >= 0; i--) {
      int value = genNextRoll();
      if (explode_above != 0 && value >= explode_above)
          i++;
      if ((reroll_above  != 0 && value >= reroll_above) || value <= reroll_below)
    	  i++;
      else
    	  effectiveRolls.add(value);
	  rollList.add(value);
    }
    
    if (ignore_high > 0 || ignore_low > 0) {
    	// Process ignore high and ignore low: convert to array, sort, then get middle
    	Collections.sort(effectiveRolls);
    	int len = effectiveRolls.size()-ignore_high-ignore_low;
    	List<Integer> newRolls = new ArrayList<Integer>(len);
    	for (int i = ignore_low; i < ignore_low+len; i++)
          newRolls.add(effectiveRolls.get(i));
    	effectiveRolls = newRolls;
    }
    
    // Copy to superclass and context
    rolls = new int[rollList.size()];
    int index = 0;
    for (Integer i : rollList) rolls[index++] = i;
    if (effectiveRolls.size() > 0) { // Append to context dice filteredResult
      int[] crolls = context.dices.rolls;
      int l = crolls.length;
      int[] newList = new int[l + effectiveRolls.size()];
      System.arraycopy(crolls, 0, newList, 0, l);
      index = l;
      for (Integer i : effectiveRolls) newList[l++] = i; 
      context.dices.rolls = newList;
    }

    // Generate output string and calculate final total
    List<Integer> usedRolls = new ArrayList<Integer>(effectiveRolls);
    StringBuilder s = new StringBuilder(usedRolls.size()*4);
    int total = 0;
    s.append("(");
    char lastState = ' '; // Last roll colour. ' ' is none, 'n' for normal roll, 'h' for highlight, 'i' for inactive, 'l' for low highlight.  Not used for explode rolls.
    char state = ' ';
    char nextState = ' '; // Next roll colour. ' ' is none, 'n' for normal roll, 'h' for highlight
    for (int r : rolls) {
      if (usedRolls.contains(r)) {
        if (explode_above > 0) {
          state = nextState == ' ' ? 'n' : nextState;
          nextState = r >= explode_above ? 'h' : 'n'; // Mark explode
        } else if (face > 20) state = r == face ? 'h' : ( r == 1 ? 'l' : 'n'); // Mark high/low
        else state = 'n'; // No explode no high/low
        usedRolls.remove(new Integer(r));
        total += r;
      } else {
        state = 'i';
      }
      if (state != lastState) {
        switch (state) {
          case 'n': s.append(Colors.PURPLE); break;
          case 'i': s.append(Colors.LIGHT_GRAY); break;
          case 'h':
          case 'l': s.append(Colors.RED); break;
        }
      }
      s.append(r).append(',');
      lastState = state;
    }
    s.setLength(s.length()-1);
    s.append(ExpressionContext.NormalResultColour).append(")");
    filteredResult = s.toString();
    super.total = (double)total;
  }

  public String asFormula() {
    return Colors.RED+modifier.toString(); // If this is filtered, we must get some modifier, right?
    /*
    if (ignore_high <= 0 && ignore_low <= 0 && explode_above <= 0)
//      return super.asFormula();
      return modifier == "";
    else
//      return super.asFormula()+Colors.RED+ExpressionContext.NormalExpColour+modifier.toString();
      return Colors.RED+modifier.toString();
      */
  }

  public String asResult() {
    return filteredResult;
  }
}
