package org.sheepy.data;

import java.util.Stack;

import org.sheepy.util.StringUtil;

/**
 * A simple XML Writter
 */
public class XMLWriter {
  /** Output buffer */
  private StringBuffer output;
  /** Header to be written */
  private String header = "<?xml version='1.0'?>";
  /** Footerr to be written */
  private String footer = null;
  /** Header written flag.  False mean nothing is written yet. */
  private boolean header_written;
  /** Open Element stack */
  private Stack<String> elements = new Stack<String>();

  /** Present indent level, start with zero */
  private int indent_lv;
  /** Open tag unclosed flag. */
  private boolean tagOpen;

  /** Indent to be used. */
  private String indent_str = "  ";
  /** Present indent */
  private String indent = "";

  /** SystemMessage line break */
  private static final String lineBreak = System.getProperty("line.separator");

  /**
   * Create a new XMLwriter with a string buffer of capacity 1000.
   */
  public XMLWriter() { output = new StringBuffer(1000); }

  /**
   * Create a new XMLwriter that output to given buffer.
   * Buffer is cleared on creation.  Create a new buffer if input is null.
   * @param output Buffer to output to
   */
  public XMLWriter(StringBuffer output) {
    this.output = output;
    if (output == null)
      output = new StringBuffer(1000);
    else
      output.setLength(0);
  }

  /**
   * Throw an IllegalStateException if header is already written.
   */
  private void checkNotBegin() {
    if (header_written) throw new IllegalStateException("Header already written");
  }

  /**
   * Throw an IllegalStateException if footer is already written.
   */
  private void checkNotEnd() {
    if (header_written && elements.isEmpty()) throw new IllegalStateException("XML file already built");
  }

  /**
   * Throw an IllegalStateException if element open tag is already closed.
   */
  private void checkTagOpen() {
    if (!tagOpen) throw new IllegalStateException("XML opening tag already closed.");
  }

  /**
   * Update indent to new indent level.
   * @param d value of indent level change, should be 1 or -1.
   */
  private void changeIndent(int d) {
    indent_lv = Math.max(0, indent_lv + d);
    if (indent_lv == 0) { indent = ""; return; }
    StringBuffer result = new StringBuffer(indent_lv*indent_str.length());
    for (int i=indent_lv; i > 0; i--) result.append(indent_str);
    indent = result.toString();
    if (indent == null) indent = "";
  }


  /**
   * Set header to be written when the body elements is being added.
   * @param header Header to be written.
   * @throws IllegalStateException if header is already written.
   */
  public void setHeader(String header) {
    checkNotBegin();
    this.header = header;
  }

  /**
   * Set footer to be written when the body element is being closed.
   * @param footer Footer to be written.
   * @throws IllegalStateException if footer is already written.
   */
  public void setFooter(String footer) {
    checkNotEnd();
    this.footer = footer;
  }

  /**
   * Set indent string to be used.
   * @param indent Indent string to be used.
   * @throws IllegalStateException if header is already written.
   */
  public void setIndent(String indent) {
    checkNotBegin();
    indent_str = indent;
  }

  /**
   * Start a new element.  Virtually NO name validation is done.
   * Write header if this is new element.
   * @param name Name of new element.
   * @throws IllegalArgumentException if name is null or empty.
   * @throws IllegalStateException if body already closed.
   */
  public void openElement(String name) {
    checkNotEnd();
    if (StringUtil.isEmpty(name)) throw new IllegalArgumentException("XML element name cannot be empty");
    if ((elements.size() == 0)&&(header != null))
      output.append(header).append(lineBreak);  // Adding body element
    else {
      if (tagOpen) {
        output.append(">");
        changeIndent(1);
      }
      output.append(lineBreak);  // Adding child element
    }
    output.append(indent).append('<').append(name);
    tagOpen = true;
    elements.push(name);
  }

  /**
   * Add an attribute to an unclosed element
   * @param name Name of attribute
   * @param value Value of attribute.
   * @throws IllegalStateException if element opening tag is already closed (by adding text)
   */
  public void addAttribute(String name, String value) {
    checkTagOpen();
    if (StringUtil.isEmpty(name)) throw new IllegalArgumentException("XML element name cannot be empty");
    if (value == null) value = "";
    final char c = (value.indexOf('"')<0)?'"':'\'';
    if (value.indexOf(c) >= 0) throw new IllegalArgumentException("Invalid attribute value: contains both \" and \'");
    output.append(' ').append(name).append('=').append(c).append(value).append(c);
  }

  /**
   * Add a text element to present element.
   * @param text Text to be added
   * @throws IllegalStateException if the body element is not in place or is finshed.
   */
  public void addText(String text) {
    checkNotBegin();
    checkNotEnd();
    if (tagOpen) {
      output.append(">");
      changeIndent(1);
      tagOpen = false;
    }
    output.append(lineBreak).append(indent).append(text);
  }

  /**
   * Close currentMod element and return to last element.
   * Write footer if body element is being closed.
   * @throws IllegalStateException if the body element is not in place or is finshed.
   */
  public void closeElement() {
    checkNotBegin(); checkNotEnd();
    String name = elements.pop();
    if (tagOpen) {
      output.append("/>");
    } else {
      changeIndent(-1);
      output.append(lineBreak).append(indent).append("</").append(name).append(">");
    }
    tagOpen = false;
    if (elements.isEmpty() && (footer != null)) output.append(footer);
  }

  /**
   * Get output string buffer.
   * @return output string buffer
   */
  public StringBuffer getOutput() {
    return output;
  }

  /**
   * Get filteredResult output string.  Automatically close all open elements.
   * @return result XML string
   */
  public String getResult() {
    while (!elements.isEmpty()) closeElement();
    return output.toString();
  }

}
