package org.usergrid.benchmark.commands;

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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ClassUtils;

public abstract class ToolBase {

  protected static final Logger logger = LoggerFactory.getLogger(ToolBase.class);

  /**
     * 
     */
  protected static final String PATH_REPLACEMENT = "USERGIRD-PATH-BACKSLASH";

  public void startTool(String[] args) {
    CommandLineParser parser = new GnuParser();
    CommandLine line = null;
    try {
      line = parser.parse(createOptions(), args);
    } catch (ParseException exp) {
      printCliHelp("Parsing failed.  Reason: " + exp.getMessage());
    }

    if (line == null) {
      return;
    }

    try {
      runTool(line);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public void printCliHelp(String message) {
    System.out.println(message);
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("java -jar usergrid-benchmark-0.0.1-SNAPSHOT.jar " + getToolName(), createOptions());
    System.exit(-1);
  }

  public String getToolName() {
    return ClassUtils.getShortName(this.getClass());
  }

  public Options createOptions() {
    //create empty options by default
    return new Options();
  }

  public void startSpring() {

		// copy("/testApplicationContext.xml", TMP);

		String[] locations = { "benchmark-context.xml" };
		ApplicationContext ac = new ClassPathXmlApplicationContext(locations);

		AutowireCapableBeanFactory acbf = ac.getAutowireCapableBeanFactory();
		acbf.autowireBeanProperties(this,
				AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		acbf.initializeBean(this, "tool");

	}

  public abstract void runTool(CommandLine line) throws Exception;

}
