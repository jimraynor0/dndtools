package org.sheepy.process;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * A queue that executes runnables one by one.
 * By Ho Yiu Yeung "Sheepy" on 2006/5/6
 */
public class RunnableQueue extends ArrayBlockingQueue<Runnable> {
  private final RunThread execution_thread = new RunThread();

  /** Create a live runnable queue.
   * @param capacity Maximum capacity of the queue
   * @param fair If true, tasks will be executed in the order they comes in.
   *  */
  public RunnableQueue(int capacity, boolean fair) {
    super(capacity, fair);
    execution_thread.start();
  }

  /**
   * Resume executioin of Runnables after it is stopped.
   * By default the queue is live after construction.
   */
  public void start() {
    execution_thread.pause = false;
    execution_thread.notify();
  }

  /** Stop executioin of Runnables.  This method returns immediartely,
   * but execute thread will stop after currently processing runnable finished running.
   */
  public void pause() {
    execution_thread.pause = true;
  }

  /** Stop executioin of Runnables.  This method returns immediartely,
   * but execute thread will stop after currently processing runnable finished running.
   */
  public void stop() {
    execution_thread.stop = true;
    execution_thread.notify();
  }

  /** If true, this queue will execute containing tasks 
   * @return true if alive */
  public boolean isAlive() {
    return execution_thread.stop;
  }

  /** If not null, this queue is executing containing tasks 
   * @return Currently running task */
  public Runnable getExecutingTask() {
    return execution_thread.current;
  }

  private class RunThread extends Thread {
    private volatile boolean pause = false;
    private volatile boolean stop = false;
    private volatile Runnable current;

    public void run() {
      do {
        try {
          Runnable r = RunnableQueue.this.take();
          if (pause) wait();
          current = r;
          r.run();
          current = null;
        } catch (InterruptedException e) {}
      } while (!stop);
    }
  }
}
