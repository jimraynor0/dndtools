package org.sheepy.data.file;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

/**
 * @author Ho Yiu Yeung
 */
public class FileNode implements IFileTreeNode {
  protected String name;
  protected File file;
  protected FolderNode parent;

  public FileNode(String s) {
    name = s;
  }

/* ---------- IFileTreeNode stuff ---------- */
  public IFileTreeNode[] getChildren() { return null; }

  public List<IFileTreeNode> getChildrenList() { return null; }

  public int getFileCount() { return 1; }

  public String toString() { return name; }

  public boolean isFile() { return true; }

  public File getFile() {
    if (file == null) file = new File(getName());
    return file;
  }

  public FolderNode getParent() { return parent; }

  public String getName() {
    if (parent != null) return parent.getName()+"/"+name;
    else return name;
  }

  public void setParent(FolderNode node) { parent = node; }

/* ---------- TreeNode stuff ---------- */
  public TreeNode getChildAt(int childIndex) { return null; }

  public int getChildCount() { return 0; }

  public int getIndex(TreeNode node) { return -1; }

  public boolean getAllowsChildren() { return false; }

  public boolean isLeaf() { return true; }

  public Enumeration children() { return null; }

  public int compareTo(Object o) {
    return o.toString().compareTo(name);
  }
}
