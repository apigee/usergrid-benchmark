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
package org.usergrid.log4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.FileAppender;

/**
 * Adds a host name right before the last "." of the file name
 * @author tnine
 *
 */
public class HostAwareFileAppender extends FileAppender {

  /* (non-Javadoc)
   * @see org.apache.log4j.FileAppender#activateOptions()
   */
  @Override
  public void activateOptions() {
    
    int lastIndex = fileName.lastIndexOf(".");
    

    String hostName;
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    
    fileName = fileName.substring(0, lastIndex) + "." + hostName + fileName.substring(lastIndex, fileName.length());
    
    super.activateOptions();
    
    
  }

}
