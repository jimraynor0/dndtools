package dndbot.module.dice;

import org.sheepy.util.StringUtil;

/**
 * Expression builder
 *
 * Craeted by Ho Yiu YEUNG on 2008年4月7日
 */
public class ExpressionBuilder {

  /**
   * Build a binary operation
   *
   * @param expr1 Left expression
   * @param operator Operator
   * @param expr2 Right operation
   * @return A expressionComponentBinaryOp, or the other expression if one of them is null
   */
  static ExpressionPart buildBinaryOp(ExpressionPart expr1, String operator, ExpressionPart expr2) {
    if (expr2 == null) return expr1;
    else if (expr1 == null) return expr2;
    else if (operator == null || StringUtil.isUcBlank(operator)) throw new IllegalArgumentException("Cannot build binary operator");
    else return new ExpressionPartBinaryOp(expr1, operator, expr2);
  }

  /**
   * Build a unary operation
   *
   * @param operator Operator
   * @param expr Right operation
   * @return A expressionComponentBinaryOp, or the other expression if one of them is null
   */
  static ExpressionPart buildUnaryOp(String operator, ExpressionPart expr) {
    if (operator == null || StringUtil.isUcBlank(operator) ) throw new IllegalArgumentException("Cannot build binary operator");
    else return new ExpressionPartUnaryOp(operator, expr);
  }

  /**
   * Build a noted operation
   *
   * @param expr expression
   * @param note Note of the expression
   * @return A expressionComponentBinaryOp, or the other expression if one of them is null
   */
  static ExpressionPart buildNote(ExpressionPart expr, String note) {
    if (note == null || StringUtil.isUcBlank(note)) return expr;
    else return new ExpressionPartNotedExpression(expr, StringUtil.ucTrim(note));
  }
}