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
package org.usergrid.tools.event;

import java.io.Serializable;

import org.slf4j.Logger;

/**
 * @author tnine
 *
 */
public class TestEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public String hostName;
  public int workerId;
  public int entryId;
  /**
   * @param hostName
   * @param index
   * @param threadNum
   */
  public TestEvent(String hostName, int workerId, int entryId) {
    super();
    this.hostName = hostName;
    this.workerId = workerId;
    this.entryId = entryId;
  }
 
  
  /**
   * @return the hostName
   */
  public String getHostName() {
    return hostName;
  }


  /**
   * @return the workerId
   */
  public int getWorkerId() {
    return workerId;
  }


  /**
   * @return the entryId
   */
  public int getEntryId() {
    return entryId;
  }


  public void logEvent(Logger logger){
    logger.info("{}-{}-{}", new Object[] { hostName, workerId, entryId });
  }
  
  
  
}
