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
package org.usergrid.benchmark.commands;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.event.EntryListener;
import org.usergrid.event.Topic;
import org.usergrid.tools.event.TestEvent;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;

/**
 * A benchmark util used to read all data from a topic and dump it to a file for analysis
 * 
 * @author tnine
 * 
 */
public class TopicReader extends TopicBase {

  protected static final Logger readLogger = LoggerFactory.getLogger("read");
  
  protected static final Timer readsTimer = Metrics.newTimer(TopicReader.class, "reads", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

  
  /*
   * (non-Javadoc)
   * 
   * @see org.usergrid.tools.TopicBase#doWork(org.usergrid.event.Topic,
   * java.lang.String, int)
   */
  @Override
  protected void doWork(CommandLine line, Topic<TestEvent> topic, String hostName, int workers, int count) throws Exception {

    CountDownLatch latch  = new CountDownLatch(count*workers);
    
    topic.subscribe(new TopicListener(latch));
    
    latch.await();
    
    writeTimerData(readsTimer);

  }

  private class TopicListener implements EntryListener<TestEvent> {

    private CountDownLatch latch;
    
    private TopicListener(CountDownLatch latch){
      this.latch = latch;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.usergrid.event.EntryListener#onMessage(java.io.Serializable)
     */
    @Override
    public void onMessage(TestEvent message) {
      message.logEvent(readLogger);
      //not a true op, so just click the timer
      readsTimer.time().stop();
      latch.countDown();
    }

  }
}
