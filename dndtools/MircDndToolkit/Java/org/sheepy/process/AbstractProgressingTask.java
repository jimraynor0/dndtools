package org.sheepy.process;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * A basic implementation of ProgressingTask, taking care of listeners and events.
 * Created by Ho Yiu Yeung on 05-Apr-2005 at 19:57:00
 */
public abstract class AbstractProgressingTask extends TimerTask implements ProgressingTask {
  private List<ProgressListener> progressListenerList;

  public boolean cancel() {
    return super.cancel();
  }

  public void addProgressListener(ProgressListener listener) {
    if (progressListenerList == null) progressListenerList = new ArrayList<ProgressListener>(1);
    if (progressListenerList.contains(listener)) return;
    progressListenerList.add(listener);
  }

  public void removeProgressListener(ProgressListener listener) {
    if (progressListenerList == null) return;
    progressListenerList.remove(listener);
  }

  protected void fireProgressRangeChanged(Object sender) {
    if (progressListenerList == null) return;
    ProgressEvent event = new ProgressEvent(this, sender);
    for (ProgressListener l : progressListenerList) l.progressRangeChanged(event);
  }

  protected void fireProgressChanged(Object sender) {
    if (progressListenerList == null) return;
    ProgressEvent event = new ProgressEvent(this, getProgress(), sender);
    for (ProgressListener l : progressListenerList) l.progressChanged(event);
  }

  protected void fireStatusChanged(Object sender) {
    if (progressListenerList == null) return;
    ProgressEvent event = new ProgressEvent(this, getProgress(), sender);
    for (ProgressListener l : progressListenerList) l.statusChanged(event);
  }

  public CharSequence getTaskName() { return "(Anonymous task)"; }

  public CharSequence getStatusMessage() {
    if (Task.isSuccess(this)) return "Done";
    else if (Task.isFailure(this)) return "Failed";
    else return "Running";
  }

  public int getMinProgress() { return 0; }

  public int getMaxProgress() { return 100; }
}
