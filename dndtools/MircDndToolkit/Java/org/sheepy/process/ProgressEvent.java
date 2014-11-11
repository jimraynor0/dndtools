package org.sheepy.process;

/**
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 21:10:00
 */
public class ProgressEvent {
  /** Current progress */
  public int progress;
  /** Deviation of progress from last notification */
  public int deviation;
  /** Progressing task */
  public ProgressingTask task;
  /** Sender of this event */
  public Object sender;

  /** Create an empty progress event */
  public ProgressEvent() {}

  /** Create and initialise a progress event 
   * @param task Event task
   * @param sender Object triggering the event */
  public ProgressEvent(ProgressingTask task, Object sender) {
    this.task = task;
    if (task != null) progress = task.getProgress();
    this.sender = sender;
  }

  /** Create and initialise a progress event 
   * @param task Event task
   * @param progress Current progress
   * @param sender Object triggering the event */
  public ProgressEvent(ProgressingTask task, int progress, Object sender) {
    this.task = task;
    if (task != null) deviation = progress - task.getProgress();
    this.progress = progress;
    this.sender = sender;
  }
}
