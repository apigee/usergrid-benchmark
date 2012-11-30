package org.usergrid.tools.event;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.event.EntryListener;

import com.yammer.metrics.core.Timer;

public class EventListener implements EntryListener<TestEvent> {
  
  private static final Logger logger = LoggerFactory.getLogger(EventListener.class);
  
  private CountDownLatch latch;
  private Timer readsTimer;
  private Logger readLogger;
  
  
  public EventListener(CountDownLatch latch, Timer readsTimer, Logger readLogger){
    this.latch = latch;
    this.readsTimer = readsTimer;
    this.readLogger = readLogger;
  }
  /*
   * (non-Javadoc)
   * 
   * @see org.usergrid.event.EntryListener#onMessage(java.io.Serializable)
   */
  @Override
  public void onMessage(TestEvent message) {
    message.logEvent(readLogger);
    logger.info("Received {}", message);
    
    //not a true op, so just click the timer
    readsTimer.time().stop();
    latch.countDown();
  }

}