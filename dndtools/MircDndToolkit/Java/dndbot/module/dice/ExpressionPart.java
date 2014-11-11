package dndbot.module.dice;

/**
 * An expression or a component of it
 * <p/>
 * Craeted by Mea on 2008年4月7日
 */
public abstract class ExpressionPart {
  /**
   * Set context of this component and subcomponent.
   * Reroll everytime this is set.
   */
  abstract public void setContext(ExpressionContext context);

  /**
   * Display this part in formula form
   */
  abstract public String asFormula();

  /**
   * Display this part in filteredResult form
   */
  abstract public String asResult();

  /**
   * Calculate and return filteredResult
   */
  abstract public double calc();

  /**
   * Merge constants
   * @return this, if nothing is simplified, otherwise a new simplified Component
   */
  abstract public ExpressionPart simplify();

  public String toString() {
    return asFormula();
  }
}