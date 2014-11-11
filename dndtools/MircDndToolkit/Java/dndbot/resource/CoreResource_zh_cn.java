package dndbot.resource;

import static org.jibble.pircbot.Colors.BOLD;
import static org.jibble.pircbot.Colors.DARK_GRAY;
import static org.jibble.pircbot.Colors.RED;

import java.util.ListResourceBundle;

import dndbot.irc.IRC;

public class CoreResource_zh_cn extends ListResourceBundle {

  protected Object[][] getContents() {
    return new Object[][]{
            {"list", "这是" + IRC.version + ", 指令清单详见 "+BOLD+".help"},

            {"setNone", "移除 \"{0}\" 设定"},
            {"setDone", "\"{0}\" 设定为 \"{1}\""},

            {"on", "爬过来, 奉上骰子"},
            {"off", "奉命静静蹲到一角去"},
            {"gc", "开始回收"},
            {"gcDone", "回收完毕, 回收了 {0} 位元组"},
            {"resetting", "重设中..."},
            {"resetted", "重设完毕"},
            {"reconnecting", "依 {0} 之令重连线中..."},
            {"reconnected", "恢复连线"},
            {"shutdown", "退出中"},
            {"private_help", "你可以用私讯向我求助而不需要在频道大喊"},

            {"errNoCmd", RED+"找不到指令"},
            {"errNoHelp", RED+"找不到 {0} 指令的说明"},
            {"errNotInChannel", RED+"请先邀请我进入频道, 或指定一个你我都在场的频道."},
            {"errNotAdmin", RED+"此指令限管理员使用.  请私讯 \".set admin (管理员密码)\" 来确认你的管理员资格."},
            {"errOldJre", RED+"爪哇执行环境不够新, 建议使用版本 6. 一些功能可能无法运作.\n" +
                    RED+"下载最新的爪哇执行环境 (JRE) 请到 http://java.sun.com/javase/downloads/index.jsp"},

            {"help_summary",
                    BOLD+"用户及系统指令"+BOLD+": .set .here .nick .me .gc .reconnect .reset .shutdown"},
            {"help_help",
                    BOLD+"显示说明"+BOLD+", 用法: .help (指令)\n" +
                    "指令用法表示： [...] 代表可选参数, (...) 代表必要参数"},


            {"help_set",
                    BOLD+"个人设定"+BOLD+", 用法: .set (选项) (设定值)\n" +
                    "频道/个人设定 colour - 把此设定为 off 会移除一切颜式及格式输出\n" +
                    "频道设定  disabled - 设定为非空白会关闭大部份指令的处理\n" +
                    "设定 admin - 把此设定为管理员密码以执行管理员指令\n" +
                    "设定 DM - 把此设定为 DM 昵称对方即可接收秘密投骰"},
            {"help_nick",
                    BOLD+"设定昵称"+BOLD+", 用法: .nick (新昵称) "+DARK_GRAY+"(限管理员私信)\n"},
            {"help_me",
                    BOLD+"作出动作"+BOLD+", 用法: .me (行动) "+DARK_GRAY+"(限管理员私信)\n"+
                    "动作输出频道可以用 .here 设定"},
            {"help_gc",
                    BOLD+"回收记忆体"+BOLD+", 用法: .gc "+DARK_GRAY+"(限管理员私信)"},
            {"help_reconnect",
                    BOLD+"断线并重连线"+BOLD+", 用法: .reconnect "+DARK_GRAY+"(限管理员私信)"},
            {"help_reset",
                    BOLD+"重设机器人"+BOLD+", 用法: .reset "+DARK_GRAY+"(限管理员私信)"},
            {"help_shutdown",
                    BOLD+"终结机器人"+BOLD+", 用法: .shutdown [讯息] "+DARK_GRAY+"(限管理员私信)"},
    };
  }
}