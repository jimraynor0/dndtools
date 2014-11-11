package dndbot.module.dice;

import dndbot.module.Dice;
import org.jibble.pircbot.Colors;

import java.util.List;

/**
 * A function in the expression
 *
 * @since 2011-05-15
 * @author Ho Yiu Yeung
 */
public class ExpressionPartFunction extends ExpressionPart {
  private String functionName;
  private List<ExpressionPart> params;
  private Double eval;

  public ExpressionPartFunction(String function, List<ExpressionPart> params) {
    functionName = function;
    this.params = params;
    if (functionName.toLowerCase().equals("swp") && params.size() != 2) {
      throw new IllegalArgumentException("swp() must have 2 parameter(s)");
    }
//    this.params = new ArrayList<ExpressionPart>(1);
//    this. params.add(params);
  }

  public void setContext(ExpressionContext context) {
    if (context == null)
      eval = null;
    for (ExpressionPart p : params)
      p.setContext(context);
  }

  public ExpressionPart simplify() {
    return this;
  }

  public String asFormula() {
    StringBuilder result = new StringBuilder( Colors.MAGENTA +  functionName + "(" );
    for (int i = 0; i < params.size(); i++) {
      if (i > 0)
        //filteredResult.append(ExpressionContext.NormalExpColour).append(",");
       result.append(",");
      result.append(params.get(i).asFormula());
    }
    return result.append(Colors.MAGENTA + ")").toString();
  }

  public String asResult() {
    StringBuilder result = new StringBuilder( Colors.MAGENTA +  functionName + "(" );
    for (int i = 0; i < params.size(); i++) {
      if (i > 0)
        //filteredResult.append(ExpressionContext.NormalResultColour).append(",");
        result.append(",");
      result.append(params.get(i).asResult());
    }
    return result.append( Colors.MAGENTA).append(")[").append(Colors.PURPLE).
            append(ExpressionContext.FORMAT.format(eval)).append(Colors.MAGENTA).append("]").toString();
  }

  public double calc() {
    if (eval != null) return eval;
    if (functionName.toLowerCase().equals("swp")) {
      evalSWP();
    } else
      eval = Double.NaN;
    return eval;
  }

  private void evalSWP() {
    long power = Math.round(params.get(0).calc());
    long roll = Math.round(params.get(1).calc());
    if (power < 0 || power >= Dice.SwordWorldPowerTable.length || roll < 2 || roll > 12)
      eval = Double.NaN;
    else if (roll == 2)
      eval = Double.NEGATIVE_INFINITY;
    else
      eval = (double)Dice.SwordWorldPowerTable[(int)power][(int)roll-3];
  }
}