package dndbot.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.Colors;

import dndbot.irc.IRC;

/**
 * Database and reference module.
 * Database provides a persistent key/value mechanism.
 * Reference provides D&D terms lookup and random table rolling.
 * 
 * Created by Ho Yiu Yeung on Apr 03, 2007
 */
public class Database extends AbstractBotModule {
  private static final int LIST_LIMIT = 20;
  private Connection reference;
  private PreparedStatement termLookup;
  private PreparedStatement randomFind;
  private PreparedStatement randomLoad1;
  private PreparedStatement randomLoad2;
  private PreparedStatement randomLookup;
  
  
  public Database(IRC irc) {
    super(irc);
    reference = (Connection) irc.data.get("db_conn_reference");
    if (reference != null) 
      try { prepareReference();
      } catch (SQLException e) { e.printStackTrace(); }
//    randomTable = (Connection) irc.data.get("db_conn_random");
  }


  public String getCommandPattern() {
    return "dict?|rand(?:om)?";
  }

  public String[] getCommand() {
    return new String[]{"dic", "dict", "rand", "random"};//, "db", "dbh"};
  }

  public void onCommand(ModuleEvent evt) {
    if (evt.parameter.length() <= 0) return;
    String destination = evt.getLocation();// evt.command.endsWith("h") ? evt.sender : evt.resultChannel(evt.login);
    if (destination == null) destination = evt.sender;
    String ref;
    if (evt.command.startsWith("dic")) {
      ref = getReference(evt.parameter.toLowerCase());
    } else if (evt.command.startsWith("rand")) {
      ref = rollRandom(evt.parameter, null);
    } else {
      ref = "";
    }
    if (ref.length() > 0 && !ref.startsWith(Colors.RED)) evt.processed = true; 
    evt.sendMessage(evt.sender, ref);
  }
  
  
  
  /** Connect to given database 
   * @param db Name of database
   * @return Connection
   * @throws SQLException 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws ClassNotFoundException */
  private Connection connect(final String db) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    Connection conn = DriverManager.getConnection("jdbc:derby:"+db);
    if (conn == null) throw new RuntimeException(res.getString("errConnectFail"));
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){ public void run() {
      try { DriverManager.getConnection("jdbc:derby:"+db+";shutdown=true");
      } catch (SQLException e) { e.printStackTrace(); }
      }}));
    return conn;
  }
  
  /** Connect to reference database 
   * @throws SQLException 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws ClassNotFoundException */
  private synchronized void connectReference() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (reference != null && !reference.isClosed()) return;
    reference = connect(res.getString("db_reference"));
    reference.setReadOnly(true);
    prepareReference();
    irc.data.put("db_conn_reference", reference);
  }
  
  /**
   * Prepare reference SQL statement
   * @throws SQLException
   */
  private void prepareReference() throws SQLException {
    termLookup = reference.prepareStatement("SELECT*FROM\"term\"WHERE\"term\"LIKE?");
    randomFind = reference.prepareStatement("SELECT DISTINCT(\"table\")FROM\"random_table\"WHERE\"table\"LIKE?");
    randomLoad1 = reference.prepareStatement("SELECT\"id\",\"chance\"FROM\"random_table\"WHERE\"table\"LIKE?AND\"chance\">0");
    randomLoad2 = reference.prepareStatement("SELECT\"id\",-\"chance\"FROM\"random_table\"WHERE\"table\"LIKE?AND\"chance\"<0");
    randomLookup = reference.prepareStatement("SELECT\"result\"FROM\"random_table\"WHERE\"id\"=?");
  }

  /** Lookup reference 
   * @param key Key to lookup
   * @return Error message or lookup filteredResult */
  private String getReference(String key) {
    key = key.replace('*', '%').replace('?', '_');
    Statement s = null;
    ResultSet rs = null;
    try {
      connectReference();
      synchronized (termLookup) {
        termLookup.setString(1, (!key.contains("%") && !key.contains("_")) ? key+"%" : key);
        rs = termLookup.executeQuery();
      }
      if (!rs.next()) return res.getString("errTermNotFound");
      String name = rs.getString("term");
      String type = rs.getString("type");
      if (!name.equals(key) &&  rs.next()) {
        // Multiple term
        StringBuilder buf = new StringBuilder();
        int i = 1;
        do {
          buf.append(name).append(" (").append(type).append(")\n");
          name = rs.getString("term");
          type = rs.getString("type");
        } while (++i <= LIST_LIMIT && rs.next());
        if (i > LIST_LIMIT) {
          return res.getString("errTooManyTerms");
        } else {
          buf.append(name).append(" (").append(type).append(")\n");
          return buf.toString();
        }
      } else {
        // Single term
        rs.close();
        s = reference.createStatement();
        rs = s.executeQuery("SELECT*FROM\"term_"+type+"\"WHERE\"name\"LIKE'"+name+"'");
        if (!rs.next()) return res.getString("errInvalidData");
        return Colors.BOLD+name.toUpperCase()+Colors.BOLD+" ("+type+")\n"+rs.getString("desc").replace("\\r\\n", "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Colors.RED+e.toString();
    } finally {
      try {
        if (rs != null) rs.close();
        if (s != null) s.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  /** Roll random table 
   * @param s Table to roll
   * @param display_table_name Table name to show, if null then use queried table name
   * @return Error message or lookup filteredResult */
  private String rollRandom(String s, String display_table_name) {
    s = s.replace('*', '%').replace('?', '_');
    ResultSet rs = null;
    try {
      connectReference();
      synchronized (randomFind) {
        randomFind.setString(1, (!s.contains("%") && !s.contains("_")) ? s+"%" : s);
        rs = randomFind.executeQuery();
      }
      if (!rs.next()) return res.getString("errTableNotFound");
      String tablename = rs.getString(1);
      if (!s.equalsIgnoreCase(tablename) && rs.next()) {
        // Multiple table match
        int i = 1;
        StringBuffer buf = new StringBuffer();
        do {
          buf.append(tablename).append("\n");
          tablename = rs.getString(1);
        } while (++i <= LIST_LIMIT && rs.next());
        if (i > LIST_LIMIT) {
          return res.getString("errTooManyTable");
        } else {
          buf.append(tablename).append("\n");
          return buf.toString();
        }
      } else {
        rs.close();
        // Now load and roll the table
        synchronized (randomLoad1) {
          randomLoad1.setString(1, tablename);
          rs = randomLoad1.executeQuery();
        }
        String result = loadTable(rs);
        rs.close();
        if (result == null) result = res.getString("randomNone"); // Empty filteredResult
        if (result.startsWith(Colors.RED)) return result; // Error
        // Now load and roll secondary table, if any
        synchronized (randomLoad2) {
          randomLoad2.setString(1, tablename);
          rs = randomLoad2.executeQuery();
        }
        String result2 = loadTable(rs);
        rs.close();
        if (result2 != null && result2.startsWith(Colors.RED)) return result2; // Error
        if (display_table_name == null) display_table_name = tablename;
        if (result.startsWith("ROLL_"))
          return rollRandom(result.substring(4), display_table_name);
        else if (result2 == null)
          return MessageFormat.format(res.getString("random_result"), display_table_name.toUpperCase(), result);
        else
          return MessageFormat.format(res.getString("random_dual_result"), display_table_name.toUpperCase(), result, result2);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (rs != null) try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return "";
  }
  
  class RandomTableRow {
    private final int chance;
    private final int id;
    private RandomTableRow(int chance, int id) {
      super();
      this.chance = chance;
      this.id = id;
    }
  }


  private String loadTable(ResultSet rs) throws SQLException {
    ResultSet r = null;
    try {
      List<RandomTableRow> table = new ArrayList<RandomTableRow>();
      int total = 0;
      while (rs.next()) {
        total += rs.getInt(2);
        table.add(new RandomTableRow(total, rs.getInt(1))); // Chance -> Id
      }
      // Table loaded. Now roll filteredResult
      int roll = irc.rand.nextInt(100) + 1;
      for (RandomTableRow e : table)
        if (roll <= e.chance) {
          roll = -e.id;
          break;
        }
      if (roll > 0) return null; // No matches
      // Load filteredResult
      synchronized (randomLookup) {
        randomLookup.setInt(1, -roll);
        r = randomLookup.executeQuery();
      }
      if (!r.next()) return res.getString("errInvalidData");
      String result = r.getString(1);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (r != null) r.close();
    }
  }
  
 }
