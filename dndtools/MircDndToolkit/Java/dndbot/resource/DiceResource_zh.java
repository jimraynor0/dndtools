package dndbot.resource;

import static org.jibble.pircbot.Colors.BLACK;
import static org.jibble.pircbot.Colors.BLUE;
import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_BLUE;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.DARK_GREEN;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

public class DiceResource_zh extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"times", "次"},
            {"rollSimple", RED+"{0}"+BLACK+" 投擲 {1}"},
            {"rollDesc", RED+"{0}"+BLACK+" 投擲 "+BOLD+"{1}"+BOLD+": {2}"},
            {"calcSimple", RED+"{0}"+BLACK+" 計算 {1}"},
            {"calcDesc", RED+"{0}"+BLACK+" 計算 "+BOLD+"{1}"+BOLD+": {2}"},
            {"rollHidden", RED+"{0}"+BLACK+" 神秘地投了一把骰子"},
            {"speedRollSaved", "快速投骰 {0} 經己儲存"},
            {"speedRollRemoved", "快速投骰 {0} 經已清除"},
            {"calcDistance", "距離("+BLUE+"{0}"+BLACK+"){1}"+RED+"="+DARK_GREEN+"{2}"},
            {"calcWalkDistance", "距離("+BLUE+"{0}"+BLACK+"){1}"+RED+"="+DARK_GREEN+"{2} "+BLACK+"(移動距離 "+DARK_GREEN+"{3}"+BLACK+")"},

            {"errNoNegativeDice", RED+"骰數和骰面需要是正數"},
            {"errTooManyDice", RED+"沒有那麼多骰子!"},
            {"errNoLastRoll", RED+"我不記得你上一次的投骰"},
            {"errNoSpeedRoll", RED+"對不起我不記得這麼一號快速投骰..."},
            {"errUnknownCommand", RED+"不明指令: {0}"},
            {"errInvalidAbilityScore", RED+"能力值不正確。應該是 6 個 3 到 18 的數值。"},
            {"errFunctionParam", RED+"{0}必需有 {1} 個參數。"},

            {"help_summary",
                    BOLD+"投骰/運算"+BOLD+": .r .rr .d .dd .dist .dist5 .rh .ra .rs"},
            {"help_r",
                    BOLD+"投骰"+BOLD+", 用法: .r [次數] 算式 [形容]\n" +
                    "骰子語法: [數量]d[骰面][投法]\n" +
                    "骰子投法: h[取高數] | l[取低數] | a "+DARK_GRAY+"平均"+BLACK+" | ih[忽略高數] | il[忽略低數] | ihl[忽略高低數] | b[低於此重投] | a[高於此重投] | x[高於此加骰]\n" +
                    "骰面預設為 d20; 此指令可與 .here 共用"},
            {"help_roll",
                    BOLD+"投骰"+BOLD+", 用法: .roll [次數] 算式 [形容]\n" +
                    "骰子語法: [數量]d[骰面][投法]\n" +
                    "骰子投法: h[取高數] | l[取低數] | a "+DARK_GRAY+"平均"+BLACK+" | ih[忽略高數] | il[忽略低數] | ihl[忽略高低數] | b[低於此重投] | a[高於此重投] | x[高於此加骰]\n" +
                    "骰面預設為 d20; 此指令可與 .here 共用"},
            {"help_rh",
                    BOLD+"隱密投骰"+BOLD+", 用法: 與 .r 一樣, 不過投骰結果不公開而私信給投骰者和投骰者 DM\n" +
                        "DM 可以用 "+BOLD+".set DM (DM 暱稱)"+BOLD+" 設定"},
            {"help_ra",
                    BOLD+"平均投骰"+BOLD+", 用法: 與 .r 一樣, 不過使用平均值代替投骰"},

            {"help_sw",
                    BOLD+"投擲劍世界骰"+BOLD+", 用法: .sw [調整值] [說明]"},
            {"help_swh",
                    BOLD+"隱密投擲劍世界骰"+BOLD+", 用法: 與 .sw 一樣, 不過投骰結果不公開而私信給投骰者和投骰者 DM\n" +
                        "DM 可以用 "+BOLD+".set DM (DM 暱稱)"+BOLD+" 設定"},
            {"help_swp",
                    BOLD+"投擲劍世界威力骰"+BOLD+", 用法: .swp [威力] [調整值] [說明]\n" +
                        "威力範圍 0-100."},
            {"help_swph",
                    BOLD+"隱密投擲劍世界威力骰"+BOLD+", 用法: 與 .swp 一樣, 不過投骰結果不公開而私信給投骰者和投骰者 DM\n" +
                        "DM 可以用 "+BOLD+".set DM (DM 暱稱)"+BOLD+" 設定"},
            {"help_swd",
                    BOLD+"投擲劍世界威力骰"+BOLD+", 用法: 與 .swp 一樣"},
            {"help_swdh",
                    BOLD+"隱密投擲劍世界威力骰"+BOLD+", 用法: 與 .swph 一樣" },

            {"help_rr",
                    BOLD+"重投上次投骰"+BOLD+", 用法: .rr"},
            {"help_rs",
                    BOLD+"儲存快速投骰"+BOLD+", 用法: .rs (關鍵字) [ 指令 [ , 指令2 , 指令3 ... ] ]\n" +
                    "指令格式: (投骰指令) [說明] "+BOLD+"或"+BOLD+" .(指令) [指令參數(如有)]\n" +
                    "儲存後的指令可用 .r (關鍵字) 使出; .rh 或 .ra 也可以, 但結果會依儲存的指令執行而不一定會隱藏或平均\n" +
                    "{1}, {2}... 會被取代成參數1, 參數 2..., 例 "+BOLD+"\".rs "+DARK_GREEN+"攻擊 "+DARK_BLUE+"d+15+"+RED+"{1}"+BLACK+"\""+BOLD+" 可用 "+BOLD+"\".r "+DARK_GREEN+"攻擊 "+RED+"2"+BLACK+"\""+BOLD+" 投擲 "+DARK_BLUE+"\"d+15+"+RED+2+BLACK+"\""},
            {"help_d",
                    BOLD+"投百面骰"+BOLD+", 用法: 與 .r 一樣, 不過預設骰面為 d100"},
            {"help_dh",
                    BOLD+"隱密投百面骰"+BOLD+", 用法: 與 .r 一樣, 不過預設骰面為 d100, 且投骰結果不公開而私信給投骰者和投骰者 DM"},
            {"help_da",
                    BOLD+"平均百面骰"+BOLD+", 用法: 與 .r 一樣, 不過預設骰面為 d100, 使用平均值代替投骰"},
            {"help_dd",
                    BOLD+"重投上次投骰"+BOLD+", 用法: .dd"},
            {"help_ds",
                    BOLD+"儲存快速投骰"+BOLD+", 用法: 與 .rs 一樣, 不過預設骰面為 d100"},
            {"help_dist",
                    BOLD+"計算距離"+BOLD+", 用法: 與 .distance 一樣."},
            {"help_dist5",
                    BOLD+"計算距離"+BOLD+", 用法: 與 .distance 一樣, 不過距離乘 5."},
            {"help_distance",
                    BOLD+"計算距離"+BOLD+", 用法: .distance (縱向距離),(橫向距離)[,垂直距離][*倍數]"+
                    "传回絶对距离, 及在输入值可以被五除尽的前提下传回二维或三维战斗移动距离"},
            {"help_distance5",
                    BOLD+"計算距離"+BOLD+", 用法: 與 .distance 一樣, 不過距離乘 5."},
            {"help_pointbuy",
                    BOLD+"計算購點"+BOLD+", 用法: .pointbuy [力] [體] [敏] [智] [感] [魅]"},
    };
  }
}