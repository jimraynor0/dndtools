package org.sheepy.data.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Builder of file tree.
 * @author Ho Yiu Yeung
 */
public class FileTreeBuilder {
  /**
   * Build a default file tree.
   * @param s Root path
   * @param filter File filter to use
   * @return Built tree
   */
  public static FolderNode buildDefaultFileTree(String s, FileFilter filter) {
    return new FileTreeBuilder().buildFileTree(s, filter);
  }

  /**
   * Create a folder node
   * @param s Name of the folder
   * @return Created folder node
   */
  protected FolderNode createFolderNode(String s) {
    return new FolderNode(s);
  }

  /**
   * Create a file node
   * @param s Name of the file
   * @return Created file node
   */
  protected FileNode createFileNode(String s) {
    return new FileNode(s);
  }

  /**
   * Parse specified directory, or directory of given file.
   * @return Root tree node for specified directory.
   * @param s Directory to filterCode.
   * @param filter File filter to user
   */
  public FolderNode buildFileTree(String s, FileFilter filter) {
    if (s == null) return null;
    File file = new File(s);
    if (!file.isDirectory()) file = new File(file.getPath());
    FolderNode root = createFolderNode(file.getAbsolutePath());
    buildFileList(root, file, filter);
    return root;
  }

  /**
   * Recursively build a java core.source tree.
   * @param root Root directory object to build on.
   * @param rfile Root file to use
   * @param filter File filter to use
   */
  private void buildFileList(FolderNode root, File rfile, FileFilter filter) {
    File[] f = rfile.listFiles(filter);
    if ((f == null) || (f.length == 0)) return;
    root.children = new ArrayList<IFileTreeNode>(f.length);
    for (File file : f)
      if (file.isDirectory()) {
        FolderNode n = createFolderNode(file.getName());
        buildFileList(n, file, filter);
        if (n.getFileCount() > 0) root.addNode(n);
      }
    for (File file : f)
      if (file.isFile())
        root.addNode(createFileNode(file.getName()));
  }
}
