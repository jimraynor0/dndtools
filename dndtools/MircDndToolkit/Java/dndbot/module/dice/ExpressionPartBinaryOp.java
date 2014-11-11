package dndbot.module.dice;

import org.apache.commons.lang.ArrayUtils;

/**
 * An expression of a binary operation
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartBinaryOp extends ExpressionPart {
  private ExpressionPart expr1;
  private ExpressionPart expr2;
  private String Op;
  private static final String[] order1 = {"+","-","^"};
  private static final String[] order2 = {"*","/","%"};

  public ExpressionPartBinaryOp(ExpressionPart expr1, String op, ExpressionPart expr2) {
    this.expr1 = expr1;
    this.expr2 = expr2;
    Op = op;
  }

  public void setContext(ExpressionContext context) {
    expr1.setContext(context);
    expr2.setContext(context);
  }

  public ExpressionPart simplify() {
    expr1 = expr1.simplify();
    expr2 = expr2.simplify();

    if (expr1 instanceof ExpressionPartBinaryOp) {
      ExpressionPartBinaryOp x1 = (ExpressionPartBinaryOp) expr1;
      if ( (x1.expr2 instanceof ExpressionPartNumber && expr2 instanceof ExpressionPartNumber) &&
          ( (ArrayUtils.contains(order1, Op) && ArrayUtils.contains(order1, x1.Op)) ||
            (ArrayUtils.contains(order2, Op) && ArrayUtils.contains(order2, x1.Op)) ) ) {
        if (x1.Op.equals("-")) { // 2d4-1 is more like 2d4+(-1) for purpose of simplification...
            x1.Op = "+";
            ((ExpressionPartNumber)x1.expr2).number = -((ExpressionPartNumber)x1.expr2).number;
        }
        expr1 = x1.expr2;
        expr2 = new ExpressionPartNumber(calc());
        expr1 = x1.expr1;
        Op = x1.Op;
        if (Op.equals("+") && ((ExpressionPartNumber)expr2).number < 0) {
          // Restore +-2 as -2
          Op = "-";
          ((ExpressionPartNumber)expr2).number = -((ExpressionPartNumber)expr2).number;
        }
      }
    }

    if (expr1 instanceof ExpressionPartNumber && expr2 instanceof ExpressionPartNumber)
      return new ExpressionPartNumber(calc());
    else
      return this;
  }

  public String asFormula() {
    return expr1.asFormula() + ExpressionContext.NormalExpColour + Op + expr2.asFormula() ;
  }

  public String asResult() {
    return expr1.asResult() + ExpressionContext.NormalResultColour + Op + expr2.asResult() ;
  }

  public double calc() {
    switch (Op.charAt(0)) {
      case '+': return expr1.calc() + expr2.calc();
      case '-': return expr1.calc() - expr2.calc();
      case '*': return expr1.calc() * expr2.calc();
      case '/': return expr1.calc() / expr2.calc();
      case '%': return expr1.calc() % expr2.calc();
      case '^': return Math.pow(expr1.calc(), expr2.calc());
      default: throw new UnsupportedOperationException("Unknown binary operator: "+Op);
    }
  }
}