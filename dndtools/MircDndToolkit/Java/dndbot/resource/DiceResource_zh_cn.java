package dndbot.resource;

import static org.jibble.pircbot.Colors.BLACK;
import static org.jibble.pircbot.Colors.BLUE;
import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_BLUE;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.DARK_GREEN;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DiceResource_zh_cn extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"times", "次"},
            {"rollSimple", RED+"{0}"+BLACK+" 投掷 {1}"},
            {"rollDesc", RED+"{0}"+BLACK+" 投掷 "+BOLD+"{1}"+BOLD+": {2}"},
            {"calcSimple", RED+"{0}"+BLACK+" 计算 {1}"},
            {"calcDesc", RED+"{0}"+BLACK+" 计算 "+BOLD+"{1}"+BOLD+": {2}"},
            {"rollHidden", RED+"{0}"+BLACK+" 神秘地投了一把骰子"},
            {"speedRollSaved", "快速投骰 {0} 经己储存"},
            {"speedRollRemoved", "快速投骰 {0} 经已清除"},
            {"calcDistance", "距离("+BLUE+"{0}"+BLACK+"){1}"+RED+"="+DARK_GREEN+"{2}"},
            {"calcWalkDistance", "距离("+BLUE+"{0}"+BLACK+"){1}"+RED+"="+DARK_GREEN+"{2} "+BLACK+"(移动距离 "+DARK_GREEN+"{3}"+BLACK+")"},

            {"errTooLargeDice", RED+"骰面需要是正数"},
            {"errTooManyDice", RED+"没有那么多骰子!"},
            {"errNoLastRoll", RED+"我不记得你上一次的投骰"},
            {"errNoSpeedRoll", RED+"对不起我不记得这么一号快速投骰..."},
            {"errUnknownCommand", RED+"不明指令: {0}"},
            {"errInvalidAbilityScore", RED+"能力值不正确。应该是 6 个 3 到 18 的数值。"},
            {"errFunctionParam", RED+"{0}必需有 {1} 个参数。"},            

            {"help_summary",
                    BOLD+"投骰/运算"+BOLD+": .r .rr .d .dd .sw .swd .dist .dist5 .calc .rh .ra .swh .swdh .rs"},
            {"help_r",
                    BOLD+"投骰"+BOLD+", 用法: .r [次数] 算式 [形容]\n" +
                    "骰子语法: [数量]d[骰面][投法]\n" +
                    "骰子投法: h[取高数] | l[取低数] | a "+DARK_GRAY+"平均"+BLACK+" | ih[忽略高数] | il[忽略低数] | ihl[忽略高低数] | b[低於此重投] | a[高於此重投] | x[高於此加骰]\n" +
                    "骰面预设为 d20; 此指令可与 .here 共用"},
            {"help_roll",
                    BOLD+"投骰"+BOLD+", 用法: .roll [次数] 算式 [形容]\n" +
                    "骰子语法: [数量]d[骰面][投法]\n" +
                    "骰子投法: h[取高数] | l[取低数] | a "+DARK_GRAY+"平均"+BLACK+" | ih[忽略高数] | il[忽略低数] | ihl[忽略高低数] | b[低於此重投] | a[高於此重投] | x[高於此加骰]\n" +
                    "骰面预设为 d20; 此指令可与 .here 共用"},
            {"help_rh",
                    BOLD+"隐密投骰"+BOLD+", 用法: 与 .r 一样, 不过投骰结果不公开而私信给投骰者和投骰者 DM\n" +
                         "DM 可以用 "+BOLD+".set DM (DM 昵称)"+BOLD+" 设定"},
            {"help_ra",
                    BOLD+"平均投骰"+BOLD+", 用法: 与 .r 一样, 不过使用平均值代替投骰"},

            {"help_sw",
                    BOLD+"投掷剑世界骰"+BOLD+", 用法: .sw [调整值] [说明]"},
            {"help_swh",
                    BOLD+"隐密投掷剑世界骰"+BOLD+", 用法: 与 .sw 一样, 不过投骰结果不公开而私信给投骰者和投骰者 DM\n" +
                        "DM 可以用 "+BOLD+".set DM (DM 昵称)"+BOLD+" 设定"},
            {"help_swp",
                    BOLD+"投掷剑世界威力骰"+BOLD+", 用法: .swp [威力] [调整值] [说明]\n" +
                        "威力范围 0-100."},
            {"help_swph",
                    BOLD+"隐密投掷剑世界威力骰"+BOLD+", 用法: 与 .swp 一样, 不过投骰结果不公开而私信给投骰者和投骰者 DM\n" +
                        "DM 可以用 "+BOLD+".set DM (DM 昵称)"+BOLD+" 设定"},
            {"help_swd",
                    BOLD+"投掷剑世界威力骰"+BOLD+", 用法: 与 .swp 一样"},
            {"help_swdh",
                    BOLD+"隐密投掷剑世界威力骰"+BOLD+", 用法: 与 .swph 一样" },

            {"help_rr",
                    BOLD+"重投上次投骰"+BOLD+", 用法: .rr"},
            {"help_rs",
                    BOLD+"储存快速投骰"+BOLD+", 用法: .rs (关键字) [ 指令 [ , 指令2 , 指令3 ... ] ]\n" +
                    "指令格式: (投骰指令) [说明] "+BOLD+"或"+BOLD+" .(指令) [指令参数(如有)]\n" +
                    "储存后的指令可用 .r (关键字) 使出; .rh 或 .ra 也可以, 但结果会依储存的指令执行而不一定会隐藏或平均\n" +
                    "{1}, {2}... 会被取代成参数1, 参数 2..., 例 "+BOLD+"\".rs "+DARK_GREEN+"攻击 "+DARK_BLUE+"d+15+"+RED+"{1}"+BLACK+"\""+BOLD+" 可用 "+BOLD+"\".r "+DARK_GREEN+"攻击 "+RED+"2"+BLACK+"\""+BOLD+" 投掷 "+DARK_BLUE+"\"d+15+"+RED+2+BLACK+"\""},
            {"help_d",
                    BOLD+"投百面骰"+BOLD+", 用法: 与 .r 一样, 不过预设骰面为 d100"},
            {"help_dh",
                    BOLD+"隐密投百面骰"+BOLD+", 用法: 与 .r 一样, 不过预设骰面为 d100, 且投骰结果不公开而私信给投骰者和投骰者 DM"},
            {"help_da",
                    BOLD+"平均百面骰"+BOLD+", 用法: 与 .r 一样, 不过预设骰面为 d100, 使用平均值代替投骰"},
            {"help_dd",
                    BOLD+"重投上次投骰"+BOLD+", 用法: .dd"},
            {"help_ds",
                    BOLD+"储存快速投骰"+BOLD+", 用法: 与 .rs 一样, 不过预设骰面为 d100"},
            {"help_dist",
                    BOLD+"计算距离"+BOLD+", 用法: 与 .distance 一样."},
            {"help_dist5",
                    BOLD+"计算距离"+BOLD+", 用法: 与 .distance 一样, 不过距离乘 5."},
            {"help_distance",
                    BOLD+"计算距离"+BOLD+", 用法: .distance (纵向距离),(横向距离)[,垂直距离][*倍数]"},
            {"help_distance5",
                    BOLD+"计算距离"+BOLD+", 用法: 与 .distance 一样, 不过距离乘 5."},
            {"help_pointbuy",
                    BOLD+"计算购点"+BOLD+", 用法: .pointbuy [力] [体] [敏] [智] [感] [魅]"},
    };
  }
}