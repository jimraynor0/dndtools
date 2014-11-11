package dndbot.module.dice;

import org.jibble.pircbot.Colors;

/**
 * An expression with note
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartNotedExpression extends ExpressionPart {
  private ExpressionPart expr;
  private String note;

  public ExpressionPartNotedExpression(ExpressionPart expr, String note) {
    this.expr = expr;
    this.note = note;
  }

  public void setContext(ExpressionContext context) {
    expr.setContext(context);
  }

  public ExpressionPart simplify() {
    return expr.simplify();
  }

  public String asFormula() {
    return expr.asFormula() + " " + Colors.DARK_GRAY + note + ' ' + ExpressionContext.NormalExpColour;
  }

  public String asResult() {
    return expr.asResult();
  }

  public double calc() {
    return expr.calc();
  }
}