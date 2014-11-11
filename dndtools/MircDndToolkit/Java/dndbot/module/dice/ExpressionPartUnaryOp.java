package dndbot.module.dice;

/**
 * An expression of an unary operation
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartUnaryOp extends ExpressionPart {
  private ExpressionPart expr;
  private String Op;
  private boolean preOp = true; // If true, this operator goes before expression

  public ExpressionPartUnaryOp(String op, ExpressionPart expr) {
    this.expr = expr;
    Op = op;
  }

  public void setContext(ExpressionContext context) {
    expr.setContext(context);
  }

  public ExpressionPart simplify() {
    expr = expr.simplify();
    if (expr instanceof ExpressionPartNumber)
      return new ExpressionPartNumber(calc());
    else
      return this;
  }

  public String asFormula() {
    return preOp
            ? Op + expr.asFormula()
            : expr.asFormula() + Op;
  }

  public String asResult() {
    return preOp
            ? Op + expr.asResult()
            : expr.asResult() + Op;
  }

  public double calc() {
    switch (Op.charAt(0)) {
      case '-': return -expr.calc();
      case '+': return +expr.calc();
      default: throw new UnsupportedOperationException("Unknown unary operator: "+Op);
    }
  }
}