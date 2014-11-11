package dndbot.resource;

import static org.jibble.pircbot.Colors.BLACK;
import static org.jibble.pircbot.Colors.BLUE;
import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.DARK_GREEN;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DiceResource extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"times", " times"},
            {"rollSimple", RED+"{0}"+BLACK+" rolls {1}"},
            {"rollDesc", RED+"{0}"+BLACK+" rolls " + BOLD + "{1}" + BOLD + ": {2}"},
            {"calcSimple", RED+"{0}"+BLACK+" calculates {1}"},
            {"calcDesc", RED+"{0}"+BLACK+" calculates " + BOLD + "{1}" + BOLD + ": {2}"},
            {"rollHidden", "{0} secretly throws a bunch of dices"},
            {"speedRollSaved", "Speed roll {0} has been saved"},
            {"speedRollRemoved", "Speed roll {0} has been removed"},
            {"calcDistance", "distance("+BLUE+BOLD+"{0}"+BOLD+BLACK+"){1}"+RED+"="+BOLD+DARK_GREEN+"{2}"},
            {"calcWalkDistance", "distance("+BLUE+"{0}"+BLACK+"){1}"+RED+"="+DARK_GREEN+"{2} "+BLACK+"(movement distance "+DARK_GREEN+"{3}"+BLACK+")"},

            {"errTooLargeDice", RED+"Dice face must be positive integer"},
            {"errTooManyDice", RED+"Haven't got so much dices!"},
            {"errNoLastRoll", RED+"I can't seem to remember your last roll."},
            {"errNoSpeedRoll", RED+"Sorry I can't remember that speed roll."},
            {"errUnknownCommand", RED+"Unknown command {0}"},
            {"errInvalidAbilityScore", RED+"Invalid ability scores, should be 6 scores between 3 to 18."},
            {"errFunctionParam", RED+"{0} must have {1}  parameter(s)."},

            {"help_summary",
                    BOLD+"Dice / calculation commands"+BOLD+": .r .rr .d .dd .dist .dist5 .rh .ra .rs"},
            {"help_r",
                    BOLD+"Roll dice"+BOLD+", Usage: .r [repeat] expression [description]" +
                    "Dice syntax: [count]d[dice_face][modifier]\n" +
                    "Dice modifier syntax: h[highest_count] | l[lowest_count] | a "+DARK_GRAY+"average"+BLACK+" | ih[ignore_highest_count] | il[ignore_lowest_count] | ihl[ignore_both_count] | b[reroll_if_below] | a[reroll_if_above] | x (explode_if_above)\n" +
                    "Dice face default to 20; This command may be used with .here"},
            {"help_roll",
                    BOLD+"Roll dice"+BOLD+", Usage: .roll [repeat] expression [description]" +
                    "Dice syntax: [count]d[dice_face][modifier]\n" +
                    "Dice modifier syntax: h[highest_count] | l[lowest_count] | a "+DARK_GRAY+"average"+BLACK+" | ih[ignore_highest_count] | il[ignore_lowest_count] | ihl[ignore_both_count] | b[reroll_if_below] | a[reroll_if_above] | x (explode_if_above)\n" +
                    "Dice face default to 20; This command may be used with .here"},
            {"help_rh",
                    BOLD+"Roll hidden"+BOLD+", Usage: Same as .r, except that roll result is sent to roller and to roller's DM\n" +
                        "use "+BOLD+".set dm (dm's nick)"+BOLD+" to set DM" },
            {"help_ra",
                    BOLD+"Roll average"+BOLD+", Usage: Same as .r, except that average result is used instead of rolling."},

            {"help_sw",
                    BOLD+"Roll sword world dice"+BOLD+", Usage: .sw [modifier] [description]"},
            {"help_swh",
                    BOLD+"Roll hidden sword world dice"+BOLD+", Usage: same as .sw, except that roll result is send to roller and to roller's DM\n" +
                        "use "+BOLD+".set dm (dm's nick)"+BOLD+" to set DM" },
            {"help_swp",
                    BOLD+"Roll sword world power dice"+BOLD+", Usage: .swp [power] [modifier] [description]\n" +
                        "Power should be 0-100."},
            {"help_swph",
                    BOLD+"Roll hidden sword world power dice"+BOLD+", Usage: same as .swp, except that roll result is send to roller and to roller's DM\n" +
                        "use "+BOLD+".set dm (dm's nick)"+BOLD+" to set DM" },
            {"help_swd",
                    BOLD+"Roll sword world power dice"+BOLD+", Usage: same as swp."},
            {"help_swdh",
                    BOLD+"Roll hidden sword world power dice"+BOLD+", Usage: same as .swph" },

            {"help_rr",
                    BOLD+"Reroll"+BOLD+", Usage: .rr"},
            {"help_rs",
                    BOLD+"Save speed roll"+BOLD+", Usage: .rs (key) [ command [ , command 2 , command 3 ... ] ]\n"+
                    "Command syntax: (roll expression) [description] "+BOLD+"OR"+BOLD+" .(command) [parameter, if any]\n" +
                    "Saved command can be called by .r (key) ; .rh/.ra can also be used, but result is resolved with saved command not necessary with hidden or average.\n" +
                    "{1}, {2}... will be replaced by parameter 1, parameter 2..., e.g. "+BOLD+".rs atk d+15+{1}"+BOLD+" can be rolled by "+BOLD+".r atk 2"+BOLD+" for d+15+2"},
            {"help_d",
                    BOLD+"Roll d100"+BOLD+", Usage: Same as .r, except that default dice is d100"},
            {"help_dh",
                    BOLD+"Roll hidden d100"+BOLD+", Usage: Same as .d, except that roll result is sent to roller and to roller's DM"},
            {"help_da",
                    BOLD+"Roll average d100"+BOLD+", Usage: Same as .d, except that average result is used instead of rolling."},
            {"help_dd",
                    BOLD+"Reroll"+BOLD+", Usage: .dd"},
            {"help_ds",
                    BOLD+"Speed roll"+BOLD+", Usage: Same as .rs, except that default dice is d100"},

            {"help_dist",
                    BOLD+"Calculate Distance"+BOLD+", Usage: Same as .distance"},
            {"help_dist5",
                    BOLD+"Calculate Distance"+BOLD+", Usage: Same as .distance, except that input distances will be multiplied by 5"},
            {"help_distance",
                    BOLD+"Calculate Distance"+BOLD+", Usage: .distance (longitude distance),(latitude distance)[,vertical distance][*multiplier]" +
                    "Return absolute distance and, if it is 2-3 distances all divisible by 5, it will also shows D&D battle movement distance"},
            {"help_distance5",
                    BOLD+"Calculate Distance"+BOLD+", Usage: Same as .distance, except that input distances will be multiplied by 5"},
            {"help_pointbuy",
                    BOLD+"Calculate Point Buy"+BOLD+", Usage: .pointbuy [str] [con] [dex] [int] [wis] [cha]"},
    };
  }
}
