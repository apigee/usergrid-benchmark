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

import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.usergrid.event.EventService;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.reporting.ConsoleReporter;

/**
 * @author tnine
 * 
 */
public abstract class EventServiceTestBase extends ToolBase {

  protected static final Logger logger = LoggerFactory.getLogger(EventServiceTestBase.class);

  

  @Autowired
  protected EventService eventService;

  @Override
  @SuppressWarnings("static-access")
  public Options createOptions() {

    Option hostOption = OptionBuilder.withArgName("hazelcast").hasArg().isRequired(true)
        .withDescription("hazelcast hosts").create("hazelcast");

    Option workers = OptionBuilder.withArgName("workers").hasArg().isRequired(true).withDescription("Max workers")
        .create("workers");

    Option count = OptionBuilder.withArgName("count").hasArg().isRequired(true)
        .withDescription("Max count of messages to send").create("count");
    
    Option wait = OptionBuilder.withArgName("wait").hasArg(false).isRequired(false)
        .withDescription("Max count of messages to send").create("wait");   

    Options options = new Options();
    options.addOption(hostOption);
    options.addOption(workers);
    options.addOption(count);
    options.addOption(wait);

    return options;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.usergrid.tools.ToolBase#runTool(org.apache.commons.cli.CommandLine)
   */
  @Override
  public void runTool(CommandLine line) throws Exception {
    // set the hazelcast cluster property BEFORE starting spring
    System.setProperty("HZ_MEMBERS", line.getOptionValue("hazelcast"));

    startSpring();

    int workers = Integer.parseInt(line.getOptionValue("workers"));

    String hostName = InetAddress.getLocalHost().getHostName();
    
    int count = Integer.parseInt(line.getOptionValue("count"));
    

    logger.info("Starting workers");

    doWork(line, hostName, workers, count);
    
    //wait until the user pressed CTRL +C
    if(line.hasOption("wait")){
      new Semaphore(0).acquire();
    }
  }
  
  protected void writeTimerData(final Timer t){
    ConsoleReporter reporter = new ConsoleReporter(Metrics.defaultRegistry(), System.out, new MetricPredicate(){

      @Override
      public boolean matches(MetricName name, Metric metric) {
        return metric == t;
      }});
    
    reporter.run();
  }

  /**
   * Perform the work
   * @param line
   * @param topic
   * @param hostName
   * @param workers
   * @throws Exception
   */
  protected abstract void doWork(CommandLine line, String hostName, int workers, int count)
      throws Exception;

}
