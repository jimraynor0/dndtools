package dndbot.module;

import dndbot.irc.IRC;
import dndbot.module.dice.*;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang.StringUtils;
import org.jibble.pircbot.Colors;
import org.sheepy.util.StringUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dice rolling module.
 * <p/>
 * Created by Ho Yiu YEUNG on Mar 8, 2007
 */
public class Dice extends AbstractBotModule {
  private static final Pattern oldMultiplierPattern = Pattern.compile("(?:(\\d+)(x|#|\\s))?" + // Mutliplier
          "\\s*([+\\.\\-*^%/,\\w\\(\\)]+)" + // Formula
          "(\\s+.*)?", Pattern.CASE_INSENSITIVE); // Desc
  private static final Pattern newMultiplierPattern = Pattern.compile("(?:(\\d+)(x|#|\\s))?" + // Mutliplier
          "\\s*`" + // marker, ignored
          "\\s*([+-]?[\\d\\w\\(].*?)" + // Formula
          "(\\s{2,}.*?)?", Pattern.CASE_INSENSITIVE); // Desc
  private static final Pattern swMiltiplierPattern = Pattern.compile("(\\d+[x|#]\\s+)?"+ // Multiplier
          "([^\\s]+)?" + // Simple number / expression - power rate in case of swd / swdh
          "(.*)?");  // The rest of it

  /** Pattern used to eliminate redundent brackets */
  private static final Pattern dupBracketPattern = Pattern.compile("\\(\\(([^)]+)\\)\\)");

  /** Pattern used to replace d%, d0,m and d00 to d100 */
  private static final Pattern d100Pattern = Pattern.compile("(?<=\\W|\\d|^)(d%|d00?)(?=\\W|$)");

  private static final Pattern Nd6k3 = Pattern.compile("^(4d6(h3|il)|5d6(h3|il2))$");

  /**
   * Equal sign with control code, as displayed in roll response
   */
  private static String EQUAL = Colors.RED + "=" + Colors.BLACK;


  public Dice(IRC irc) {
    super(irc);
  }

  public String getCommandPattern() {
    return "[rd][ahs]?|sw[pd]?h?|roll|rr|dd|dist(?:ance)?5?|pointbuy";
  }

  public String[] getCommand() {
    return new String[]{"r", "ra", "rh", "rr", "rs", "d", "da", "dh", "dd", "ds", "dist", "roll",
            "sw", "swh", "swd", "swdh", "swp", "swph", 
            "dist5", "distance", "distance5", "pointbuy" };
  }

  public void onCommand(ModuleEvent evt) {
    try {
      if (evt.command.startsWith("dist")) {
        calculateDistance(evt);
        return;
      }
      if (evt.command.startsWith("point")) {
        evt.sendMessage(evt.channel != null ? evt.channel : evt.sender, calcAndAddPointBuy(evt.parameter));
        return;
      }
      if (evt.command.startsWith("sw")) {
        processSWRoll(evt);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      evt.sendNotice(evt.getLocation(), e.toString());
    }

/*  rmode To be implemented. Need to get target channel rmode setting before roll instead of after
    if (evt.command.startsWith("rmode")) {
      String currentMode = irc.settings.get(evt.channel, "rollMode");
      if (currentMode == null || currentMode.equals("WEEK")) {
        irc.settings.put(evt.channel, "rollMode", "NONE");
      }
      return;
    }
     */

    if (evt.command.equals("rr") || evt.command.equals("dd")) {
      reroll(evt);
//      return;
    } else if (evt.command.equals("rs") || evt.command.equals("ds")) {
      if (evt.parameter.length() <= 0) return;

      // Save speed roll
      String type = evt.command.substring(0, 1);
      String[] param = Colors.removeFormattingAndColors(evt.parameter).split("\\s+", 2);
      if (param.length < 2 || evt.parameter.length() <= 0) {
        // Name only; removes speed roll
        if (param.length > 0) {
          saveSpeedRoll(evt.sender, type, param[0], null);
          evt.sendMessage(evt.sender, MessageFormat.format(res.getString("speedRollRemoved"), param[0].toLowerCase()));
          evt.processed = true;
        }
      } else {
        // Save speed roll
        saveSpeedRoll(evt.sender, type, param[0], param[1]);
        evt.sendMessage(evt.sender, MessageFormat.format(res.getString("speedRollSaved"), param[0].toLowerCase()));
        evt.processed = true;
      }
    } else {
      if (evt.parameter.length() <= 0) return;

      if (speedRoll(evt)) {
        evt.processed = true;
        return;
      }

      try {
        processRoll(evt);
      } catch (Exception e) {
        e.printStackTrace();
        evt.sendNotice(evt.getLocation(), e.toString());
      }
    }
  }

  /**
   * Parse and process a sword world roll
   * @param evt
   */
  private void processSWRoll(ModuleEvent evt) {
    final boolean isHidden = evt.command.toLowerCase().endsWith("h");

    String finalExpr = "2d6";
    if (evt.parameter.trim().length() > 0) {
      // Parse parameters
      Matcher multiplier = swMiltiplierPattern.matcher(evt.parameter);
      if (!multiplier.matches()) return;
      final boolean isPowerLookup = evt.command.toLowerCase().endsWith("d") || evt.command.toLowerCase().endsWith("dh");
      final String repeat = multiplier.group(1) == null ? "" : multiplier.group(1);
      final String rate = multiplier.group(2) == null ? "0" : multiplier.group(2).trim();
      final String param = multiplier.group(3) == null ? (isPowerLookup ? "0" : "") : multiplier.group(3).trim();
      final String preBrackets = StringUtils.repeat("(", StringUtil.countOccurence(param, ')')-StringUtil.countOccurence(param, '(')) ;

      // Make formula
      if (isPowerLookup) {
        // swd or swdh: 2d6 reroll mapped to power table
        finalExpr = repeat + preBrackets + "swp(" + rate + ",2d6)+"+param;
      } else {
        // sw or swh: normal 2d6 reroll
        finalExpr = repeat + preBrackets + "2d6+"+ rate + param;
      }
    }
    List<String> result = roll(evt.sender, finalExpr, 6, false);

    if (result != null) {
      evt.processed = true;
      irc.data.put("last_roll_" + evt.command.charAt(0) + "_" + evt.login, evt);
      sendResult(result, evt, isHidden);
    }
  }

  /**
   * Do basic parse on a roll command before passing it to roll routine which do more checking.
   * If roll routines return valid results, mark event as process, call sendResult, and store the command
   * @param evt Roll event
   */
  private void processRoll(ModuleEvent evt) {
    final boolean isAverage = evt.command.length() > 1 && evt.command.toLowerCase().charAt(1) == 'a';
    final boolean isHidden = !isAverage && evt.command.length() > 1 && evt.command.toLowerCase().charAt(1) == 'h';
    final int defaultFace = (evt.command.charAt(0) == 'r')
            ? StringUtil.strToInt(irc.settings.get(evt.sender, "Dice"), 20)
            : 100;

    final List<String> result = roll(evt.sender, evt.parameter, defaultFace, isAverage);
    if (result != null) {
      evt.processed = true;
      irc.data.put("last_roll_" + evt.command.charAt(0) + "_" + evt.login, evt);
      sendResult(result, evt, isHidden);
    }
  }

  /**
   * Actually parse and process a roll expression
   * @param roller
   * @param parameter
   * @param defaultFace
   * @param isAverage
   * @return
   */
  private List<String> roll(String roller, String parameter, int defaultFace, boolean isAverage) {
    // Parse multipliers, expression, and description
    Matcher multiplier = oldMultiplierPattern.matcher(parameter);
    if (parameter.contains("`") || parameter.contains("  ")) {
      multiplier = newMultiplierPattern.matcher(parameter);
      if (!multiplier.matches())
        multiplier = oldMultiplierPattern.matcher(parameter);
    } else {
      if (!multiplier.matches())
        multiplier = newMultiplierPattern.matcher(parameter);
    }
    if (!multiplier.matches()) return null;
    DiceLexer lexer = new DiceLexer(new ANTLRStringStream(
            StringUtil.ucSpaceNormalise(d100Pattern.matcher(Colors.removeFormattingAndColors(multiplier.group(3))).replaceAll("d100").trim())
    ));
    DiceParser diceParser = new DiceParser(new CommonTokenStream(lexer));
    ExpressionPart exp = null;
    try {
      exp = diceParser.full_expression();
    } catch (RecognitionException e) {
      e.printStackTrace();
      return null; // Not a valid roll afterall
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    if (exp == null) return null;

    // Prepare and apply context
    ExpressionContext context = new ExpressionContext(irc.rand, defaultFace, isAverage);
    context.adjustRoll = ExpressionContext.GodsHand.STRONG;
    String lastRoll = irc.settings.get(roller, "lastDiceRollRatio");
    context.lastContextRoll = lastRoll == null ? Float.NaN : Float.parseFloat(lastRoll);
    exp.setContext(context);

    // Now everything is in order, we can determine remaining parameters
    final String rollDesc = StringUtil.ucTrim(multiplier.group(4));
    final boolean hasDice = context.getDiceCount() > 0;
    final boolean shortform = hasDice && !StringUtil.isBlank(multiplier.group(2));
    int repeat = hasDice? Math.max(StringUtil.strToInt(multiplier.group(1), 1), 1) : 1;
    if (hasDice && repeat > 1) repeat = Math.min(repeat, shortform ? 20 : 8);
    String type = hasDice ? "roll" : "calc";
    List<String> resultStack = (shortform) ? new ArrayList<String>(repeat) : null;
    List<String> fullResultStack = (shortform) ? new ArrayList<String>(repeat) : null;
    List<String> sendStack = new ArrayList<String>(repeat);

    String parsed_expression = exp.asFormula(); // Need it before simplification
    boolean show_roll_result = true;
    // Repeat the whole thing as many times as specified
    for (int i = 0; i < repeat; i++) {
      if (i != 0) {
    	  context.clearRollHistory();
        exp.setContext(null); // Send a null to force reroll
      } else { // First time evaluation ? Simplify expression and check for need of showing verbose roll/calc filteredResult
        exp = exp.simplify();
        if (exp instanceof ExpressionPartNumber) show_roll_result = false;
      }
      String finalValue = context.formatNumber(exp.calc());

      // Send detailed roll if not short form
      if (!shortform) {
        // Assemble filteredResult
        String rollResult = ExpressionContext.NormalExpColour + parsed_expression;
        if (show_roll_result) {
          String result = exp.asResult();
          if (!removeDuplicateBracket(Colors.removeFormattingAndColors(result)).equals(finalValue))
            rollResult +=  EQUAL + ExpressionContext.NormalResultColour + result;
        }
        rollResult += EQUAL + Colors.DARK_GREEN + finalValue;

        // Normalise bracket and reduce control char.
        rollResult = removeDuplicateBracket(IRC.optimiseColours(rollResult));

        String desc = (rollDesc == null)
                ? MessageFormat.format(res.getString(type + "Simple"), roller, rollResult)
                : MessageFormat.format(res.getString(type + "Desc"), roller, rollDesc, rollResult);

        // Find where to send filteredResult to
        sendStack.add(desc);
      } else {
        resultStack.add(finalValue);
        if (context.getDiceCount() == 1) {
          if (!(exp instanceof ExpressionPartDice))
            fullResultStack.add(context.dices.asResult().replace("(", "").replace(ExpressionContext.NormalResultColour+")", ""));
        } else
          fullResultStack.add(context.dices.asResult());
      }
    }

    if (!Float.isNaN(context.lastContextRoll))
      irc.settings.put(roller, "lastDiceRollRatio", Float.toString(context.lastContextRoll));

    // If using short form, then send once instead
    if (shortform && resultStack.size() > 0) {
      String rollResult = repeat + res.getString("times") + " " + parsed_expression + " " + EQUAL + " ";
      if (fullResultStack.size() > 0)
        rollResult += ExpressionContext.NormalResultColour + StringUtil.implode(fullResultStack, ", ") + " " + EQUAL + " " ;
      rollResult += StringUtil.implode(resultStack, " ");

      String plainFormula = Colors.removeFormattingAndColors(parsed_expression);
      if (resultStack.size() == 6 && Nd6k3.matcher(plainFormula).matches())
          rollResult = rollResult + Colors.NORMAL + " (" + calcAndAddPointBuy(resultStack) + Colors.NORMAL + ")";

      String desc = (rollDesc == null) ?
              MessageFormat.format(res.getString(type + "Simple"), roller, rollResult)
              : MessageFormat.format(res.getString(type + "Desc"), roller, rollDesc, rollResult);
      sendStack.add(desc);
    }
    return sendStack;
  }

  /**
   * Send a public or hidden roll results
   * @param results Roll results
   * @param evt Command event, used to determine the proper channel and subject to send
   * @param isHidden whether this is a hidden roll
   */
  private void sendResult(List<String> results, ModuleEvent evt, boolean isHidden) {
    String destination;
    String channel = null; // Hidden roll public message channel
    String dm = null; // Where to cc filteredResult
    if (isHidden) {
      destination = evt.sender;
      channel = evt.resultChannel(evt.sender); // Detect .here
      dm = irc.settings.get(evt.sender, "DM");
    } else {
      destination = evt.channel;
      if (destination == null) destination = evt.resultChannel(evt.sender); // Detect .here
      if (destination == null) destination = evt.sender; // Fallback to sender
    }
    for (String desc : results) {
      if (isHidden) {
        evt.sendNotice(destination, desc);
        if (channel != null)
          evt.sendMessage(channel, MessageFormat.format(res.getString("rollHidden"), evt.sender));
        if (!StringUtil.isEmpty(dm))
          evt.sendNotice(dm, desc);// evt.sendMessage(dm, desc);
      } else {
        evt.sendMessage(destination, desc);
      }
    }
  }

  /** Point buy array for 3rd edition, starting with 9 */
  private static final byte[] PointBuy3e = {1, 2, 3, 4, 5, 6, 8, 10, 13, 16};
  /** Point buy array for 4th edition, starting with 11 */
  private static final byte[] PointBuy4e = {1, 2, 3, 5, 7, 9, 12, 16};

  /**
   * Given a string of six number, return point buy needed to purchase them
   * @param param Ability scores, e.g. "8 12 16 12 10 18"
   * @return Point buy filteredResult in string, or invalid scroes message
   */
  private String calcAndAddPointBuy(String param) {
    String[] parts = param.trim().split("(\\s*,\\s*|\\s+)");
    if (parts.length != 6) return res.getString("errInvalidAbilityScore");
    final ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < 6; i++) {
      String p = parts[i];
      if (StringUtils.isEmpty(p) || !StringUtils.isNumeric(p) || Integer.parseInt(p) < 3 || Integer.parseInt(p) > 18)
        return res.getString("errInvalidAbilityScore");
      list.add(p);
    }
    return calcAndAddPointBuy(list);
  }

  /**
   * Calculate and add point buy cost.  Plus total bonus.  Language independent.
   * @param stack Stack of ability points
   * @return
   */
  private String calcAndAddPointBuy(List<String> stack) {
    //int[] abilities = new int[6];
    int bonus = 0;
    int Cost3 = 0;
    int Cost4 = 0;
    int lowest = 20;
    for (int i = 0; i < 6; i++) {
      int a = Integer.parseInt(stack.get(i));
      bonus += Math.floor((a-10)/2);
      if (a > 18) a = 18;
      if (a > 8 && a < 19) Cost3 += PointBuy3e[a-9];
      if (a > 10 && a < 19) Cost4 += PointBuy4e[a-11];
      if (a < lowest) lowest = a;
    }
    if (lowest > 8) Cost4 += Math.min(2, lowest-8);
    String result = bonus > 3 && bonus < 9 ? Colors.DARK_GREEN : Colors.RED;
    result += bonus >= 0 ? "+" : "";
    result += bonus + Colors.NORMAL + ", 4e:" + Colors.DARK_BLUE + Cost4 + Colors.NORMAL + ", 3e:" + Colors.BROWN + Cost3 ;
    return result;
}


  private String removeDuplicateBracket(String in) {
    String original;
    do {
      original = in;
      in = dupBracketPattern.matcher(in).replaceAll("($1)");
    } while (!in.equals(original));
    if (in.charAt(0) == '(' && in.charAt(in.length()-1) == ')')
      in = in.substring(1, in.length()-1);
    return in;
  }


  Pattern distanceSeparator = Pattern.compile("(?:\\s+|\\s*,\\s*)");


  private void calculateDistance(ModuleEvent evt) {
    String parameter = evt.parameter;
    int multiplier = (parameter.indexOf('*') > 0) ? StringUtil.strToInt(parameter.substring(parameter.indexOf('*') + 1), 1) : 1;
    if (evt.command.endsWith("5")) multiplier *= 5;
    parameter = (parameter.indexOf('*') > 0) ? parameter.substring(0, parameter.indexOf('*')) : parameter;
    String[] params = distanceSeparator.split(parameter);
    if (params.length <= 0) return;

    double result = 0;
    boolean divisible = true;
    for (String p : params) {
      double param = StringUtil.strToDouble(p, 0);
      result += Math.pow(param, 2);
      if (divisible && param * multiplier % 5 != 0) divisible = false;
    }
    result = Math.sqrt(result) * multiplier;
    if (result <= 0) return;

    if (!divisible || params.length < 2 || params.length > 3) // Not in grid or not 2d/3d
      evt.sendMessage(evt.getLocation(), MessageFormat.format(res.getString("calcDistance"), StringUtil.implode(params, ","), (multiplier == 1) ? "" : ("*" + multiplier), result));
    else {
      int x = (int) StringUtil.strToDouble(params[0], 0) * multiplier;
      int y = (int) StringUtil.strToDouble(params[1], 0) * multiplier;
      double walk = Math.min(x, y) / 5 * 1.5;
      y = Math.max(x, y) - Math.min(x, y);

      if (params.length == 2) {
        walk += y / 5;
      } else {
        int z = (int) StringUtil.strToDouble(params[2], 0) * multiplier;

        walk += Math.min(z, y) / 5 * 1.5 + (Math.max(z, y) - Math.min(z, y)) / 5;
      }

      walk = Math.floor(walk) * 5;

      evt.sendMessage(evt.getLocation(), MessageFormat.format(res.getString("calcWalkDistance"), StringUtil.implode(params, ","), (multiplier == 1) ? "" : ("*" + multiplier), result, walk));
    }
  }

  /**
   * Process a reroll
   *
   * @param e Reroll event
   */
  private void reroll(ModuleEvent e) {
    ModuleEvent last = (ModuleEvent) irc.data.get("last_roll_" + e.command.charAt(0) + "_" + e.login);
    if (last == null) {
      e.sendMessage(e.sender, res.getString("errNoLastRoll"));
    } else {
      last.processed = false;
      onCommand(last);
      e.processed = last.processed;
    }
  }

  private Pattern speedRollSeparator = Pattern.compile("\\s*,\\s*");

  /**
   * Save speed roll
   *
   * @param user User login
   * @param type r (d20) or d (d%)
   * @param key  Speed roll key
   * @param roll Roll to store
   */
  private void saveSpeedRoll(String user, String type, String key, String roll) {
    StringBuilder value = new StringBuilder();
    if (roll != null) {
      String[] cmds = speedRollSeparator.split(roll);
      for (String command : cmds) {
        command = StringUtil.ucTrim(command);
        if (command.length() <= 0) continue;
        if (!command.startsWith(".")) value.append(".").append(type).append(" ");
        value.append(command);
        value.append("\n");
      }
    }
    key = "speedroll_" + type + "_" + key.toLowerCase();
    irc.settings.put(user, key, (roll == null) ? null : value.toString());
  }

  /**
   * Get speed roll
   *
   * @param user User login
   * @param type r (d20) or d (d%)
   * @param key  Speed roll key
   * @return Stored speed roll
   */
  private String getSpeedRoll(String user, String type, String key) {
    key = "speedroll_" + type + "_" + key.toLowerCase();
    String result = irc.settings.get(user, key, true, null);
    return result;
  }

  /**
   * Try speed roll
   *
   * @param e Event
   * @return true if speed roll success
   */
  private boolean speedRoll(ModuleEvent e) {
    if (e.parameter.length() <= 0) return false;
    // Speed roll key + parameter
    String rollParam[] = StringUtil.splitWords(e.parameter);
    String key = rollParam[0];
    // Check existance of speed roll
    String speed = getSpeedRoll(e.sender, e.command.substring(0, 1), key);
    if (speed == null) return false;
    String[] cmds = StringUtil.splitLines(speed);
    boolean processed = false;
    // Process each command
    for (String c : cmds) {
      // Fill in parameter
      if (c.indexOf('{') >= 0)
        for (int i = 1; i < 9; i++) {
          c = c.replace("{" + i + "}", (rollParam.length > i) ? rollParam[i] : "");
          if (c.indexOf('{') < 0) break;
        }
      String[] cmdParam = c.split("\\s+", 2);
      // Create event to be processed
      ModuleEvent evt = new ModuleEvent(e.bot, e.channel, e.sender, e.login, e.hostname, c, cmdParam[0].toLowerCase().substring(1), (cmdParam.length > 1) ? cmdParam[1] : "", e.isNotice);
      if (irc.moduleOfCommand(evt.command) != null) {
        irc.moduleOfCommand(evt.command).onCommand(evt);
        if (evt.processed) processed = true;
      } else {
        e.sendMessage(e.login, MessageFormat.format(res.getString("errUnknownCommand"), evt.command));
      }
    }
    if (processed) {
      System.out.println("Found and processed sped roll "+e.message);
      irc.data.put("last_roll_" + e.command.charAt(0) + "_" + e.login, e);
    }
    return processed;
  }

  static final public int[][] SwordWorldPowerTable =
      {{0,0,0,1,2,2,3,3,4,4},
      {0,0,0,1,2,3,3,3,4,4},
      {0,0,0,1,2,3,4,4,4,4},
      {0,0,1,1,2,3,4,4,4,5},
      {0,0,1,2,2,3,4,4,5,5},
      {0,1,1,2,2,3,4,5,5,5},
      {0,1,1,2,3,3,4,5,5,5},
      {0,1,1,2,3,4,4,5,5,6},
      {0,1,2,2,3,4,4,5,6,6},
      {0,1,2,3,3,4,4,5,6,7},
      {1,1,2,3,3,4,5,5,6,7},
      {1,2,2,3,3,4,5,6,6,7},
      {1,2,2,3,4,4,5,6,6,7},
      {1,2,3,3,4,4,5,6,7,7},
      {1,2,3,4,4,4,5,6,7,8},
      {1,2,3,4,4,5,5,6,7,8},
      {1,2,3,4,4,5,6,7,7,8},
      {1,2,3,4,5,5,6,7,7,8},
      {1,2,3,4,5,6,6,7,7,8},
      {1,2,3,4,5,6,7,7,8,9},
      {1,2,3,4,5,6,7,8,9,10},
      {1,2,3,4,6,6,7,8,9,10},
      {1,2,3,5,6,6,7,8,9,10},
      {2,2,3,5,6,7,7,8,9,10},
      {2,3,4,5,6,7,7,8,9,10},
      {2,3,4,5,6,7,8,8,9,10},
      {2,3,4,5,6,8,8,9,9,10},
      {2,3,4,6,6,8,8,9,9,10},
      {2,3,4,6,6,8,9,9,10,10},
      {2,3,4,6,7,8,9,9,10,10},
      {2,4,4,6,7,8,9,10,10,10},
      {2,4,5,6,7,8,9,10,10,11},
      {3,4,5,6,7,8,10,10,10,11},
      {3,4,5,6,8,8,10,10,10,11},
      {3,4,5,6,8,9,10,10,11,11},
      {3,4,5,7,8,9,10,10,11,12},
      {3,5,5,7,8,9,10,11,11,12},
      {3,5,6,7,8,9,10,11,12,12},
      {3,5,6,7,8,10,10,11,12,13},
      {4,5,6,7,8,10,11,11,12,13},
      {4,5,6,7,9,10,11,11,12,13},
      {4,6,6,7,9,10,11,12,12,13},
      {4,6,7,7,9,10,11,12,13,13},
      {4,6,7,8,9,10,11,12,13,14},
      {4,6,7,8,10,10,11,12,13,14},
      {4,6,7,9,10,10,12,13,13,14},
      {4,6,7,9,10,10,12,13,13,14},
      {4,6,7,9,10,11,12,13,13,15},
      {4,6,7,9,10,12,12,13,13,15},
      {4,6,7,10,10,12,12,13,14,15},
      {4,6,8,10,10,12,12,13,15,15},
      {5,7,8,10,10,12,12,13,15,15},
      {5,7,8,10,11,12,12,13,15,15},
      {5,7,9,10,11,12,12,14,15,15},
      {5,7,9,10,11,12,13,14,15,15},
      {5,7,10,10,11,12,12,14,15,15},
      {5,8,10,10,11,12,13,15,16,16},
      {5,8,10,11,11,12,13,15,16,17},
      {5,8,10,11,12,12,13,15,16,17},
      {5,9,10,11,12,12,14,15,16,17},
      {5,9,10,11,12,13,14,15,16,18},
      {5,9,10,11,12,13,14,16,17,18},
      {5,9,10,11,13,13,14,16,17,18},
      {5,9,10,11,13,13,15,17,17,18},
      {5,9,10,11,13,14,15,17,17,18},
      {5,9,10,12,13,14,15,17,18,18},
      {5,9,10,12,13,15,15,17,18,19},
      {5,9,10,12,13,15,16,17,19,19},
      {5,9,10,12,14,15,16,17,19,19},
      {5,9,10,12,14,16,16,17,19,19},
      {5,9,10,12,14,16,17,18,19,19},
      {5,9,10,13,14,16,17,18,19,20},
      {5,9,10,13,15,16,17,18,19,20},
      {5,9,10,13,15,16,17,19,20,21},
      {6,9,10,13,15,16,18,19,20,21},
      {6,9,10,13,16,16,18,19,20,21},
      {6,9,10,13,16,17,18,19,20,21},
      {6,9,10,13,16,17,18,20,21,22},
      {6,9,10,13,16,17,19,20,22,23},
      {6,9,10,13,16,18,19,20,22,23},
      {6,9,10,13,16,18,20,21,22,23},
      {6,9,10,13,17,18,20,21,22,23},
      {6,9,10,14,17,18,20,21,22,24},
      {6,9,11,14,17,18,20,21,23,24},
      {6,9,11,14,17,19,20,21,23,24},
      {6,9,11,14,17,19,21,22,23,24},
      {7,10,11,14,17,19,21,22,23,25},
      {7,10,12,14,17,19,21,22,24,25},
      {7,10,12,14,18,19,21,22,24,25},
      {7,10,12,15,18,19,21,22,24,26},
      {7,10,12,15,18,19,21,23,25,26},
      {7,11,13,15,18,19,21,23,25,26},
      {7,11,13,15,18,20,21,23,25,27},
      {8,11,13,15,18,20,22,23,25,27},
      {8,11,13,16,18,20,22,23,25,28},
      {8,11,14,16,18,20,22,23,26,28},
      {8,11,14,16,19,20,22,23,26,28},
      {8,12,14,16,19,20,22,24,26,28},
      {8,12,15,16,19,20,22,24,27,28},
      {8,12,15,17,19,20,22,24,27,29},
      {8,12,15,18,19,20,22,24,27,30}};
}
