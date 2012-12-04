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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.benchmark.commands.EventServiceTestBase;
import org.usergrid.event.Queue;
import org.usergrid.tools.event.TestEvent;

/**
 * @author tnine
 * 
 */
public abstract class QueueBase extends EventServiceTestBase {

  protected static final Logger logger = LoggerFactory.getLogger(QueueBase.class);

  /*
   * (non-Javadoc)
   * 
   * @see org.usergrid.benchmark.commands.TestBase#createOptions()
   */
  @SuppressWarnings("static-access")
  @Override
  public Options createOptions() {

    Option topicName = OptionBuilder.withArgName("queue").hasArg().isRequired(true)
        .withDescription("The name of the queue to use").create("queue");

    Options options = super.createOptions();
    options.addOption(topicName);
    return options;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.usergrid.benchmark.commands.TestBase#doWork(org.apache.commons.cli.
   * CommandLine, java.lang.String, int, int)
   */
  @Override
  protected void doWork(CommandLine line, String hostName, int workers, int count) throws Exception {
   
    String queueName = line.getOptionValue("queue");

    Queue<TestEvent> event = eventService.getQueue(queueName);
    

    doWork(line, event, hostName, workers, count);
  }

  /**
   * Perform the work
   * 
   * @param line
   * @param queue
   * @param hostName
   * @param workers
   * @param count
   * @throws Exception
   */
  protected abstract void doWork(CommandLine line, Queue<TestEvent> queue, String hostName, int workers, int count)
      throws Exception;

}
