package org.sheepy.data.file;

import java.io.File;
import java.util.List;

import javax.swing.tree.TreeNode;

/**
 * Basic element of a OldFileTreeNode
 * @author Ho Yiu Yeung
 */
public interface IFileTreeNode extends TreeNode, Comparable {
  public boolean isFile();
  public File getFile();
  public FolderNode getParent();
  public void setParent(FolderNode node);

  /**
   * Get children node
   * @return array of children node, or null if children is not possible.
   */
  public IFileTreeNode[] getChildren();
  /**
   * Get children node list
   * @return list of children node, or null if children is not possible.
   */
  public List<IFileTreeNode> getChildrenList();

  public int getFileCount();
  /**
   * Get full name of what the node represent
   * @return full absolute or relative name of what the node represent
   */
  public String getName();

  //public String toString();
}
