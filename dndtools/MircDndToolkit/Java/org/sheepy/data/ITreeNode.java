package org.sheepy.data;

import java.util.List;

/**
 * Craeted by Ho Yiu YEUNG on 2008年4月9日
 */
public interface ITreeNode<NodeType, EventListener> {
  public NodeType get();
  public void set(NodeType value);
  public void add(ITreeNode child);
  public void remove(ITreeNode child);
  public List<ITreeNode> list();

  public void setParent(ITreeNode parent);
  public ITreeNode getParent();

  public void addListener(EventListener listener);
  public void removeListener(EventListener listener);
}
