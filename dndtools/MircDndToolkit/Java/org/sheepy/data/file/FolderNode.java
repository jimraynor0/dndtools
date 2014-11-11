package org.sheepy.data.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.sheepy.data.collection.IterEnumeration;

/**
 * @author Ho Yiu Yeung
 */
public class FolderNode implements IFileTreeNode {
  protected String name;
  protected File file;
  protected FolderNode parent;
  protected List<IFileTreeNode> children;

  public FolderNode(String name) {
    this.name = name;
  }

  public boolean isFile() { return false; }

  public File getFile() {
    if (file == null) file = new File(getName());
    return file;
  }

  public TreeNode getChildAt(int childIndex) {
    return (children==null)?null:children.get(childIndex);
  }

  public int getChildCount() {
    return (children==null)?0:children.size();
  }

  public FolderNode getParent() { return parent; }

  public void setParent(FolderNode node) {
    parent = node;
    file = null;
  }

  public void addNode(IFileTreeNode node) {
    if (node == null) return;
    if (children==null) children = new ArrayList<IFileTreeNode>();
    children.add(node);
    node.setParent(this);
  }

  public String getName() {
    if (parent != null)
      return parent.getName()+"/"+name;
    else
      return name;
  }

  public int getIndex(TreeNode node) {
    return (children==null)?0:children.indexOf(node);
  }

  public boolean getAllowsChildren() { return false; }

  public boolean isLeaf() {
    return (children==null)?true:(children.size()<=0);
  }


  public static final IFileTreeNode[] NodeTemplate = new IFileTreeNode[0];
  public static final List<IFileTreeNode> EmptyNodeList = new ArrayList<IFileTreeNode>(0);

  /**
   * Return an enumeration of children.  For use with old components only.
   * @deprecated
   */
  public Enumeration children() {
    return (children != null)?new IterEnumeration(children.iterator()):IterEnumeration.getEmptyEnumeration();
  }

  public IFileTreeNode[] getChildren() {
    return (children != null)?children.toArray(NodeTemplate):null;
  }

  public List<IFileTreeNode> getChildrenList() {
    return (children != null)?children:EmptyNodeList;
  }

  public int getFileCount() {
    if (children == null) return 0;
    int result = 0;
    for (IFileTreeNode n : children) result += n.getFileCount();
    return result;
  }

  public String toString() { return name; }

  public int compareTo(Object o) {
    return o.toString().compareTo(name);
  }
}
