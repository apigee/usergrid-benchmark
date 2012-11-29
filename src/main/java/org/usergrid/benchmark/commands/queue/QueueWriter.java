/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.benchmark.commands.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.event.ScheduledQueue;
import org.usergrid.tools.event.TestEvent;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;

/**
 * A benchmark util used to read all data from a topic and dump it to a file for
 * analysis
 * 
 * @author tnine
 * 
 */
public class QueueWriter extends QueueBase {

  protected static final Logger writeLogger = LoggerFactory.getLogger("write");
  
  protected static final Timer writesTimer = Metrics.newTimer(QueueWriter.class, "writes", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

  /*
   * (non-Javadoc)
   * 
   * @see org.usergrid.tools.TopicBase#doWork(org.usergrid.event.Topic,
   * java.lang.String, int)
   */
  @Override
  protected void doWork(CommandLine line, ScheduledQueue<TestEvent> queue, String hostName, int workers, int count)
      throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(workers);

    List<Future<Void>> futures = new ArrayList<Future<Void>>();

    logger.info("Starting workers");

    for (int i = 0; i < workers; i++) {

      futures.add(executor.submit(new QueueWorker(queue, hostName, i, count)));
    }

    // wait for futures to complete
    for (Future<Void> future : futures) {
      future.get();
      logger.info("Worker complete");
    }
    
   
    writeTimerData(writesTimer);

  }

  private class QueueWorker implements Callable<Void> {

    private ScheduledQueue<TestEvent> queue;
    private String hostName;
    private int threadIndex;
    private int count;

    /**
     * @param testTopic
     * @param hostName
     * @param threadNum
     * @param index
     */
    public QueueWorker(ScheduledQueue<TestEvent> queue, String hostName, int threadIndex, int count) {
      this.queue = queue;
      this.hostName = hostName;
      this.threadIndex = threadIndex;
      this.count = count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Void call() throws Exception {

      for (int i = 0; i < count; i++) {
        TestEvent event = new TestEvent(hostName, threadIndex, i);
        event.logEvent(writeLogger);
       
        TimerContext timer = writesTimer.time();
        queue.schedule((long)(2000*Math.random()), TimeUnit.MILLISECONDS, event);
        timer.stop();
      }

      return null;
    }

  }
}
