package dndbot.module.dice;

/**
 * A grouped expression
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartGroup extends ExpressionPart {
  private ExpressionPart expr;

  public ExpressionPartGroup(ExpressionPart expr) {
    this.expr = expr;
  }

  public void setContext(ExpressionContext context) {
    expr.setContext(context);
  }

  public ExpressionPart simplify() {
    expr = expr.simplify();
    if (expr instanceof ExpressionPartNumber || expr instanceof ExpressionPartDice)
      return expr;
    else
      return this;
  }

  public String asFormula() {
    return ExpressionContext.NormalExpColour + "(" + expr.asFormula() + ExpressionContext.NormalExpColour + ")";
  }

  public String asResult() {
    return ExpressionContext.NormalResultColour + "(" + expr.asResult() + ExpressionContext.NormalResultColour + ")";
  }

  public double calc() {
    return expr.calc();
  }
}