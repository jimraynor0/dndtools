package org.sheepy.process;

/**
 * Provides information for a prograssing task
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 21:17:39
 */
public interface ProgressingTask {
  /** Get name of task 
   * @return Task name */
  public CharSequence getTaskName();

  /** Get status message. 
   * @return Task status */
  public CharSequence getStatusMessage();

  /** Get minimum progress value. Default to be 0. 
   * @return min progress */
  public int getMinProgress();

  /** Get maximum progress value. Default to be 100.
   * If getMaxProgress() == getMinProgress(), when the progress will finish is not known.
   * @return max progress */
  public int getMaxProgress();

  /** Get currentMod progress value.
   * A value equal to or larger then getMaxProgress() means the task has completed,
   * unless getMaxProgress() == getMinProgress().
   * A value smaller then getMinProgress() means the task has failed. 
   * @return current progress */
  public int getProgress();

  /** Add given progress listener 
   * @param listener Listener to add */
  public void addProgressListener(ProgressListener listener);
  /** Remove given progress listener 
   * @param listener Listener to remove */
  public void removeProgressListener(ProgressListener listener);
}
