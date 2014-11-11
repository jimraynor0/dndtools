package dndbot.module.dice;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.JVMRandom;
import org.jibble.pircbot.Colors;

/**
 * An expression of a dice rolling operation
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartDice extends ExpressionPart {
  private ExpressionPart count; // If null, then default to one
  private ExpressionPart faces; // If null, default to 20
  private DiceResult result; // Calculatted roll filteredResult
  private DiceOpt modifier; // Roll modifier
  private ExpressionContext context; // Roll context
  private boolean average;

  private static final String[] modifiers = {"a","b","h","l","k","il","ih", "ihl", "ilh","x"};

  public static class DiceOpt {
    private String type; // Roll modifier
    private ExpressionPart modParam; // Modifier's numeric parameter

    DiceOpt(String type, ExpressionPart modParam) {
      type = type.toLowerCase();
      if (!ArrayUtils.contains(modifiers, type)) throw new IllegalArgumentException("Unknown dice modifier: "+type);
//      if (type.equals("a") && modParam != null) throw new IllegalArgumentException("Unsupported dice modifier: "+type+modParam.toString());
      if (type.equals("k")) type = "h";
      if (type.equals("ilh")) type = "ihl";
      this.type = type;
      //if (modParam == null && !type.equals("a")) modParam = new ExpressionPartNumber(1);
      //if (modParam == null) modParam = new ExpressionPartNumber( type.equals("a"));
      this.modParam = modParam;
    }

    public ExpressionPart getModParam() {
      return modParam;
    }

    public String getType() {
      return type;
    }
    
    public String toString() {
      if (modParam == null)
        return type;
      else
        return type + modParam.asFormula();
    }
  }

  public ExpressionPartDice(ExpressionPart count, ExpressionPart faces, DiceOpt modifier) {
    this.count = count;
    this.faces = faces;
    this.modifier = modifier;
  }

  ExpressionPartDice(ExpressionPart count, ExpressionPartDice die) {
    this.count = count;
    faces = die.faces;
    modifier = die.modifier;
  }

  public void setContext(ExpressionContext context) {
    if (context == null || context != this.context) {
      if (context == null && !average) {
    	if (result != null) this.context.diceCount -= result.times - 1; // Reset to 1
    	//this.context.dices = new DiceResult(0, 0, this.context); // Usually won't get many PartDice, can afford to just create
    	//this.context.dices.roll(); // No roll, no dice buffer...
      }
      result = null;
      if (context == null) return;
    }
    this.context = context;
    context.dices = new DiceResult(0, 0, context); // Dice buffer
    context.dices.roll();
    average = context.rollAverage; // || ( modifier != null && modifier.isAverage() );
    if (count != null) count.setContext(context);
    if (faces != null) faces.setContext(context);
    if (!average) context.diceCount += 1; // Assume it is one for now
    if (modifier != null && modifier.modParam != null) modifier.modParam.setContext(context);
  }

  public ExpressionPart simplify() {
    return this;
  }

  private void checkRoll() {
    if (result != null) return; // Already rolled!

    if (context == null) context = new ExpressionContext(new JVMRandom(), 6, false);
    int times = (int) Math.round( (count == null) ? 1 : count.calc() );
    int face = (int) Math.round( (faces == null) ? context.defaultFace : faces.calc() );
    if (average) {
      if (modifier == null) {
        result = new DiceResultAverage(times, face, context);
      } else {
        result = new DiceResultFilteredAverage(times, face, context, modifier);
      }
    } else {
      context.diceCount += times - 1;
      if (modifier == null) { // h, l, ih, il, ihl
        result = new DiceResult(times, face, context);
      } else {
        result = new DiceResultFiltered(times, face, context, modifier);
      }
    }
    result.roll();
  }

  public ExpressionPart getCount() {
    return count;
  }

  public ExpressionPart getFaces() {
    return faces;
  }

  public String asFormula() {
    checkRoll();
    return  ((count == null || count instanceof ExpressionPartNumber) ? Colors.DARK_BLUE+result.times : ExpressionContext.NormalExpColour+"("+count.asFormula()+")")
            + Colors.DARK_BLUE + "d" +
            ((faces == null || faces instanceof ExpressionPartNumber) ? Colors.DARK_BLUE+result.face : ExpressionContext.NormalExpColour+"("+faces.asFormula()+")")
            + (ExpressionContext.NormalExpColour + result.asFormula());
  }

  public String asResult() {
    checkRoll();
    return ( (count == null || count instanceof ExpressionPartNumber) ? "" : count.asResult() )
            + ( (faces == null || faces instanceof ExpressionPartNumber) ? "" : faces.asResult() )
            + result.asResult();
  }

  public double calc() {
    checkRoll();
    return result.calc();
  }
}