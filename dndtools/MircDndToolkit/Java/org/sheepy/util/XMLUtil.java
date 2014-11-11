package org.sheepy.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Ho Yiu Yeung
 */
public class XMLUtil {
  /**
   * Convert an XML NodeList to a java.util.List of node.
   * @param list NodeList to convert to collection
   * @return An empty list if list is null
   */
  public static List<Node> nodeListCollection(NodeList list) {
    if (list == null) return new ArrayList<Node>(0);
    List<Node> result = new ArrayList<Node>(list.getLength());
    for (int i=0; i < list.getLength(); i++) result.add(list.item(i));
    return result;
  }

  /**
   * Get a java.util.List of elements by tag name
   * @param parent Parent element
   * @param tag Tag name of child element
   * @return A list of elements, possibily an empty one.
   */
  public static List<Element> getElementsByTagName(Element parent, String tag) {
    NodeList list = parent.getElementsByTagName(tag);
    if (list == null || list.getLength() == 0) return new ArrayList<Element>(0);
    List<Element> result = new ArrayList<Element>(list.getLength());
    for (int i=0; i < list.getLength(); i++) result.add((Element)list.item(i));
    return result;
  }


  /** Get the first child parent with given tag name.
   *
   * @param parent Parent element
   * @param id Tag namd of child element to get
   * @return Null if no such child
   */
  public static Element getElementByTagName(Element parent, String id) {
    NodeList l = parent.getElementsByTagName(id);
    return l.getLength() > 0 ? (Element)l.item(0) : null;
  }

  /**
   * Get Text content of a child element, if any
   * @param parent Parent element
   * @param id Tag namd of text content of child element to get
   * @return child.getTextContent(), or null if child cannot be found.
   */
  public static String getTextByTagName(Element parent, String id) {
    Element m = getElementByTagName(parent, id);
    return m != null ? m.getTextContent() : "";
  }
}
