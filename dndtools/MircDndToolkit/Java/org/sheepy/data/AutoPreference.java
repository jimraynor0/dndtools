package org.sheepy.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * A class that helps manage preferences.
 *
 * Usage: subclass with public fields as settings, which will be loaded from and saved into preference with same name,
 * using default field value as default preference value
 *
 * Created by Ho Yiu YEUNG on May 15, 2007 at 12:30:59 PM
 */
public class AutoPreference {
  /** Preference node class */
  protected Class prefNode;
  /** If true, use system node */
  protected boolean isSystem;
  /** Preference file, set on load and used on save */
  protected File prefFile;
  /** Preference object, created in initPreference */
  protected Preferences pref;

  /** Store default field values which becomes default preference values */
  protected Map<String, Object> defaultValues;
  /** Preference fields */
  protected Field[] fields;

  /**
   * Create a user preference with the object's class as node
   */
  public AutoPreference() {
    prefNode = getClass();
  }

  /**
   * Create a user preference loading from given file
   */
  public static AutoPreference create(Class cls, File f) {
    AutoPreference preference = null;
    try {
      preference = (AutoPreference)cls.newInstance();
      if (preference != null) {
        preference.checkInit();
        if (f != null)
          try {
            preference.loadFromFile(f);
          } catch (InvalidPreferencesFormatException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return preference;
  }

  /**
   * Create a preference object
   * @param node Node of preference, null if root is to be used
   * @param system If true, use system node, otherwise use user node
   */
  public AutoPreference(Class node,  boolean system) {
    prefNode = node;
    isSystem = system;
  }

  /**
   * Make sure the preference is initialised
   */
  private void checkInit() {
    if (pref == null) initPreference();
  }


  /**
   * Initialise preference
   */
  private void initPreference() {
    Class node = prefNode;
    if (!isSystem)
      if (node != null)
        pref = Preferences.userNodeForPackage(node);
      else
        pref = Preferences.userRoot();
    else
    if (node != null)
      pref = Preferences.systemNodeForPackage(node);
    else
      pref = Preferences.userRoot();

    // Set default preference values
    Field[] fs = getClass().getFields();
    List<Field> fl = new ArrayList<Field>(fs.length);
    defaultValues = new HashMap<String, Object>(fs.length, 1.0f);
    for (Field f : fs) {
      if (f.getName().startsWith("_")) continue;
      try {
        f.setAccessible(true);
        Object value = f.get(this);
        // Set default field values when null
        if (value == null) {
          if (f.getType().equals(Boolean.class)) {
            value = false;
          } else if (f.getType().equals(byte[].class)) {
            value = new byte[0];
          } else if (f.getType().equals(Double.class)) {
            value = 0.0;
          } else if (f.getType().equals(Float.class)) {
            value = 0.0f;
          } else if (f.getType().equals(Integer.class)) {
            value = 0;
          } else if (f.getType().equals(Long.class)) {
            value = 0l;
          }
          if (value != null) f.set(this, value);
        }
        defaultValues.put(f.getName(), value);
        fl.add(f);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    fields = fl.toArray(new Field[0]);

    // Sync with pref
    updateSelf();
  }

  /**
   * Update preference on gc
   * @throws Throwable
   */
  protected void finalize() throws Throwable {
    try {
      updatePreference();
    } finally {
      super.finalize();
    }
  }

  /**
   * Load preference from file
   * @param file Preference file
   * @throws InvalidPreferencesFormatException If the file is in invalid format
   * @throws IOException If something wrong happens while reading
   */
  public void loadFromFile(File file) throws InvalidPreferencesFormatException, IOException {
    checkInit();
    if (file != null) {
      prefFile = file;
      if (file.exists()) {
        InputStream in = null;
        try {
          in = new BufferedInputStream(new FileInputStream(file));
          Preferences.importPreferences(in);
        } finally {
          if (in != null) in.close();
        }
        updateSelf();
      }
    }
  }

  /**
   * Save preference to the file last loaded from
   */
  public void saveToFile() throws BackingStoreException, IOException {
    if (prefFile == null) throw new IllegalStateException("Config not loaded from any file");
    saveToFile(prefFile);
  }

  /**
   * Save preference to given file
   * @param config
   */
  public void saveToFile(File config) throws IOException, BackingStoreException {
    OutputStream out = null;
    updatePreference();
    try {
      out = new BufferedOutputStream(new FileOutputStream(config));
      pref.exportSubtree(out);
    } finally {
      if (out != null) {
        out.flush();
        out.close();
      }
    }
  }

  public void clear() throws BackingStoreException {
    pref.clear();
    updateSelf();
  }

  /**
   * Synchronise with preference by updating self
   */
  public void updateSelf() {
    checkInit();
    for (Field f : fields) {
      try {
        if (f.getType().equals(String.class)) {
          f.set(this, pref.get(f.getName(), (String)defaultValues.get(f.getName())));
        } else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
          f.set(this, pref.getBoolean(f.getName(), (Boolean)defaultValues.get(f.getName())));
        } else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
          f.set(this, pref.getDouble(f.getName(), (Double)defaultValues.get(f.getName())));
        } else if (f.getType().equals(Float.class) || f.getType().equals(float.class)) {
          f.set(this, pref.getFloat(f.getName(), (Float)defaultValues.get(f.getName())));
        } else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
          f.set(this, pref.getInt(f.getName(), (Integer)defaultValues.get(f.getName())));
        } else if (f.getType().equals(Long.class) || f.getType().equals(long.class)) {
          f.set(this, pref.getLong(f.getName(), (Long)defaultValues.get(f.getName())));
        } else if (f.getType().equals(byte[].class)) {
          f.set(this, pref.getByteArray(f.getName(), (byte[])defaultValues.get(f.getName())));
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Synchronise with preference by updating preference
   */
  public void updatePreference() {
    checkInit();
    for (Field f : fields) {
      try {
        Object value = f.get(this);
        if (value == null) {
          pref.remove(f.getName());
        } else if (f.getType().equals(String.class)) {
          pref.put(f.getName(), (String)f.get(this));
        } else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
          pref.putBoolean(f.getName(), (Boolean)f.get(this));
        } else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
          pref.putDouble(f.getName(), (Double)f.get(this));
        } else if (f.getType().equals(Float.class) || f.getType().equals(float.class)) {
          pref.putFloat(f.getName(), (Float)f.get(this));
        } else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
          pref.putInt(f.getName(), (Integer)f.get(this));
        } else if (f.getType().equals(Long.class) || f.getType().equals(long.class)) {
          pref.putLong(f.getName(), (Long)f.get(this));
        } else if (f.getType().equals(byte[].class)) {
          pref.putByteArray(f.getName(), (byte[])f.get(this));
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
