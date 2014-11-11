package org.sheepy.process;



/**
 * A complete implementation of ProgressingTask.
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 21:13:15
 */
abstract public class Task extends AbstractProgressingTask {
  private CharSequence taskName;
  private CharSequence statusMessage;
  private int minProgress;
  private int maxProgress = 100;
  private int progress;

  public CharSequence getTaskName() {
    return taskName;
  }
  public void setTaskName(CharSequence taskName) {
    this.taskName = taskName;
  }

  public CharSequence getStatusMessage() {
    return statusMessage;
  }
  public void setStatusMessage(CharSequence statusMessage) {
    this.statusMessage = statusMessage;
    fireStatusChanged(this);
  }
  public void setStatusMessage(CharSequence statusMessage, Object sender) {
    this.statusMessage = statusMessage;
    fireStatusChanged(sender);
  }

  public int getMinProgress() {
    return minProgress;
  }
  public int getMaxProgress() {
    return maxProgress;
  }
  public void setProgressRange(int minProgress, int maxProgress) {
    if ((minProgress == this.minProgress) && (this.maxProgress == maxProgress)) return;
    this.minProgress = minProgress;
    this.maxProgress = maxProgress;
    fireProgressRangeChanged(this);
  }
  public void setProgressRange(int minProgress, int maxProgress, Object sender) {
    if ((minProgress == this.minProgress) && (this.maxProgress == maxProgress)) return;
    this.minProgress = minProgress;
    this.maxProgress = maxProgress;
    fireProgressRangeChanged(sender);
  }

  public int getProgress() { return progress; }
  public void setProgress(int currentProgress) {
    this.progress = currentProgress;
    fireProgressChanged(this);
  }
  public void setProgress(int currentProgress, Object sender) {
    this.progress = currentProgress;
    fireProgressChanged(sender);
  }

  /** Increase progress. Accept negative number. 
   * @param step step to increase */
  protected void increaseProgress(int step) {
    setProgress(getProgress() + step);
  }

  public static boolean isSuccess(ProgressingTask t) {
    int max = t.getMaxProgress();
    return (max > t.getMinProgress()) && (t.getProgress() >= max);
  }

  public static boolean isFailure(ProgressingTask t) {
    return t.getProgress() < t.getMinProgress();
  }
}
