package dndbot.module.dice;

/**
 * An expression of a simple number
 *
 * Craeted by Ho Yiu Yeung on 2008年4月7日
 */
public class ExpressionPartNumber extends ExpressionPart {
  double number;

  public ExpressionPartNumber(double number) {
    this.number = number;
  }

  public ExpressionPartNumber(String number) {
    this.number = Double.parseDouble(number);
  }

  public void setContext(ExpressionContext context) {
  }

  public ExpressionPart simplify() {
    return this;
  }


  public String asFormula() {
    return ExpressionContext.FORMAT.format(number);
  }

  public String asResult() {
    return ExpressionContext.FORMAT.format(number);
  }

  public double calc() {
    return number;
  }
}