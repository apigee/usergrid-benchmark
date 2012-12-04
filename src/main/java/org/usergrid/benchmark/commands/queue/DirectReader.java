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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.usergrid.event.Queue;
import org.usergrid.tools.event.EventListener;
import org.usergrid.tools.event.TestEvent;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;

/**
 * A benchmark util used to read all data from a topic and dump it to a file for
 * analysis
 * 
 * @author tnine
 * 
 */
public class DirectReader extends QueueBase {

  protected static final Logger readLogger = LoggerFactory.getLogger("read");

  protected static final Timer readsTimer = Metrics.newTimer(DirectReader.class, "reads", TimeUnit.MILLISECONDS,
      TimeUnit.SECONDS);

  
  @Autowired
  private HazelcastInstance instance;
  
  /*
   * (non-Javadoc)
   * 
   * @see org.usergrid.tools.TopicBase#doWork(org.usergrid.event.Topic,
   * java.lang.String, int)
   */
  @Override
  protected void doWork(CommandLine line, Queue<TestEvent> queue, String hostName, int workers, int count)
      throws Exception {

    CountDownLatch latch = new CountDownLatch(count * workers);
    
//     queue.observe(new EventListener(latch, readsTimer, readLogger, queue));
    
    
    
    EventListener listener = new EventListener(latch, readsTimer, readLogger, queue);
    
    IQueue<TestEvent> hzQueue = instance.getQueue(line.getOptionValue("queue"));
    
    while(latch.getCount() != 0){
      
      listener.onMessage(hzQueue.take());
    }
        
    latch.await();

    writeTimerData(readsTimer);

  }

}
