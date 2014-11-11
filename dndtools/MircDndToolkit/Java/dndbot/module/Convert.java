package dndbot.module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dndbot.irc.IRC;

/**
 * Unit conversion module
 * <p/>
 * Created by Ho Yiu YEUNG on 2007 Apr 10
 */
public class Convert extends AbstractBotModule {
  public Convert(IRC irc) {
    super(irc);
  }

  public String getCommandPattern() {
    return "unit";
  }

  public String[] getCommand() {
    return new String[]{"unit"};
  }

  private static final Unit[][] units = new Unit[][]{
  {
          new Unit(0.3048*5, "battle grid", "(?:battle\\s+)?grid"),
          new SizeFromLength(), 
          new Unit(1E-10, "ångström", "ångströms?|aangstroems?|angstroms?|Å"),
          new Unit(0.0254, "inch", "inch|inches|in|\""),
          new Unit(0.3048, "feet", "feet|foot|ft|'"),
          new Unit(0.9144, "yard", "yards?|yd"),
          new Unit(1609.344, "mile", "miles?|mi|ml|M"),
          new Unit(1E-9, "nanometre", "nanomet(?:er|re)s?|nm"),
          new Unit(1E-6, "micrometre", "micromet(?:er|re)s?|microns?|µm"),
          new Unit(0.001, "millimetre", "millimet(?:er|re)s?|mm"),
          new Unit(0.01, "centimetre", "centimet(?:er|re)s?|cm"),
          new Unit(1, "metre", "met(?:er|re)s?|m"),
          new Unit(1000, "kilometre", "kilomet(?:er|re)s?|km"),
          new Unit(149597870691.0, "astronomical unit", "astronomical\\sunits?|AU|au|a.u|ua"),
          new Unit(9460730472580800.0, "lightyear", "light-?years?|ly"),
          new Unit(3.08568E16, "parsec", "parsecs?|pc"),
  }};
  

  public void onCommand(ModuleEvent evt) {
    Matcher m = conversionPattern.matcher(evt.parameter); 
    if (m.matches()) {
      // Both unit given
      Double amount = Double.parseDouble(m.group(1));
      String fromUnit = m.group(2);
      String toUnit = m.group(3);
      for (Unit[] units : Convert.units) {
        Unit from = null;
        Unit to = null;
        for (Unit u : units) {
          if (from == null && u.match(fromUnit)) {
            from = u;
            if (from != null && to != null) break;
          } else if (to == null && u.match(toUnit)) {
            to = u;
            if (from != null && to != null) break;
          }
        }
        if (from != null && to != null) {
          evt.sendMessage(evt.getLocation(), to.toString(from.convert(amount)));
          evt.processed = true;
          break;
        }
      }
    } else {
      // Single unit.
    }
  }
  
  private Pattern conversionPattern = Pattern.compile("(-?\\d+(?:.\\d+)?(?:[eE]-?\\d+))(.+)\\s+(?:in|@)\\s+(.+)");
//  private Pattern singleUnit = Pattern.compile("(-?\\d+(?:.\\d+)?(?:[eE]-?\\d+))(.+)");

  private static class Unit {
    final double unit;
    private final String name;
    private final Pattern pattern;
//    private final boolean common;

    private Unit(double unit, String symbol, String pattern) {
      this(unit, symbol, pattern, false);
    }

    private Unit(double unit, String symbol, String pattern, boolean common) {
      this.unit = unit;
      this.name = symbol;
      this.pattern = Pattern.compile(pattern);
//      this.common = common;
    }

    private boolean match(String unit) {
      return pattern.matcher(unit).matches();
    }

    private double convert(double in) {
      return in * unit;
    }

    String toString(double unit) {
      return (unit * this.unit) + " " + name;
    }
  }
  
  private static class LookupUnit extends Unit {
    private LookupUnit(String name, String pattern) {
      super(1, name, pattern, true);
    }
  }

  private static class SizeFromLength extends LookupUnit {
    private SizeFromLength() {
      super("size", "size|creature");
    }
    String toString(double unit) {
      double metre = this.unit * unit;
      if (metre < 0) return "Fine";
      else if (metre < 0) return "Dim";
      else if (metre < 0) return "Tiny";
      else if (metre < 0) return "Small";
      else if (metre < 0) return "Medium";
      else if (metre < 0) return "Large";
      else if (metre < 0) return "Huge";
      else if (metre < 0) return "Gar";
      else return "Col";
    }
  }
}
