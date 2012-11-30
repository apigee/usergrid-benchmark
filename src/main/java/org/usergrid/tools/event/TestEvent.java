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

import org.slf4j.Logger;
import org.usergrid.event.EventEntry;

/**
 * @author tnine
 *
 */
public class TestEvent implements EventEntry {

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


  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + entryId;
    result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
    result = prime * result + workerId;
    return result;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestEvent other = (TestEvent) obj;
    if (entryId != other.entryId)
      return false;
    if (hostName == null) {
      if (other.hostName != null)
        return false;
    } else if (!hostName.equals(other.hostName))
      return false;
    if (workerId != other.workerId)
      return false;
    return true;
  }


  public void logEvent(Logger logger){
    logger.info(toString());
  }
  
  public String toString(){
    return String.format("%s-%03d-%08d", new Object[] { hostName, workerId, entryId });
  }
  
  
  
}
