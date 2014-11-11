package org.sheepy.swing;

import static org.sheepy.util.SwingUtil.BORDER_LOWERED;
import static org.sheepy.util.SwingUtil.SCROLL_AS_NEEDED;
import static org.sheepy.util.SwingUtil.SCROLL_BOTH;
import static org.sheepy.util.SwingUtil.decorateComp;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.sheepy.stream.DocumentWriter;
import org.sheepy.stream.JTextComponentReader;
import org.sheepy.stream.ReadWriteConnector;
import org.sheepy.stream.ReaderInputStream;
import org.sheepy.stream.WriterOutputStream;

/**
 * A panel that capture console output and provides text field for simple console input.
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class JConsolePanel extends JPanel {
  private final JTextArea txtOutput = new JTextArea();
  private final JTextField txtInput = new JTextField();
  private final Reader r = new JTextComponentReader(txtInput);
  private final Writer w = new DocumentWriter(txtOutput);
//  private final Writer err = new DocumentWriter((AbstractDocument) txtOutput.getDocument(), null);

  /** Createa a console and replace System in and System out with this. */
  public JConsolePanel() {
    initUI(null, null);
    System.setIn(new ReaderInputStream(r));
    System.setOut(new PrintStream(new WriterOutputStream(w)));
  }

  /** Connect given output stream to console and input stream to text field using default encoding 
   * @param p Process to hook to */
  public JConsolePanel(final Process p) {
    this(p.getInputStream(), p.getOutputStream());
    new Thread() {
      public void run() {
        try {
          p.waitFor();
          JConsolePanel.this.txtInput.setEnabled(false);
        } catch (InterruptedException e) {
        }
      }
    }.start();
  }

  /** Connect given output stream to console and input stream to text field using given encoding 
   * @param in Output stream
   * @param out Input stream
   * @param incharset Input charset
   * @param outcharset Output charset
   * @throws UnsupportedEncodingException */
  public JConsolePanel(InputStream in, OutputStream out, String incharset, String outcharset) throws UnsupportedEncodingException {
      this(new InputStreamReader(in, incharset), new OutputStreamWriter(out, outcharset));
  }

  /** Connect given output stream to console and input stream to text field using default encoding 
   * @param in InputStream
   * @param out OutputStream */
  public JConsolePanel(InputStream in, OutputStream out) {
      this(new InputStreamReader(in), new OutputStreamWriter(out));
  }

  /** Connect given writer to console and reader to text field 
   * @param in Input
   * @param out Output */
  public JConsolePanel(Reader in, Writer out) {
    initUI(in, out);
  }

  /**
   * Initialise panel
   * @param in Input
   * @param out Output
   */
  private void initUI(Reader in, Writer out) {
    setLayout(new BorderLayout());
    add(decorateComp(txtOutput, SCROLL_BOTH + SCROLL_AS_NEEDED + BORDER_LOWERED));
    add(txtInput, BorderLayout.SOUTH);
    txtOutput.setEditable(false);
    if (in != null) new ReadWriteConnector(in, new DocumentWriter(txtOutput));
    if (out!= null) new ReadWriteConnector(r, out);
  }
}
