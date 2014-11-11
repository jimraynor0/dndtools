package org.sheepy.process;

/**
 * An interface for components that provides status feedback mechanism.
 * An object can use the methods to update status or progress.
 * Displaying multiple status can be done by distinguishing senders.
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 21:02:46
 */
public interface ProgressListener {
  /**
   * Update currentMod status.  The message may display in status bar,
   * thread monitor, or whatever.  How the message is used is not specified.
   * @param event Progress event
   */
  public void statusChanged(ProgressEvent event);

  /**
   * Update progress of task.
   * Progess below valid range implies job canceled, and
   * progress above valid range implies job completed.
   * @param event Progress event
   */
  public void progressChanged(ProgressEvent event);

  /**
   * Triggered when range of the progress has changed.
   * This event should be triggered before an associated progressChanged event, if any.
   * Minimum must be smaller then maximum, otherwise behaviour is unspecified.
   * @param event Progress event
   */
  public void progressRangeChanged(ProgressEvent event);
}
