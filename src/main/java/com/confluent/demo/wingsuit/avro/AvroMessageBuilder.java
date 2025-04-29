/**
 * Copyright 2025 Confluent Inc. All Rights Reserved
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code was written as part of a Demonstration for how to quickly get started with 
 * Confluent Cloud.  This code is NOT built / intended to be used for any Production purposes
 * and should only be considered for prototyping / experimental uses only
 * 
 * Any questions on this please reach out to Confluent Professional Services in your
 * respective area
 */

package com.confluent.demo.wingsuit.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/**
 * Class that builds an AVRO message based on the flysight data and drops it into the topic 
 */
public class AvroMessageBuilder {

private Schema wsSchema = null;
private GenericRecord flightEvent =null;

	public AvroMessageBuilder(){}//end of constructor
	
	public void initNewWSSchema()
	{
		AvroWSMessage newWSSchema = new AvroWSMessage();
		this.wsSchema= newWSSchema.getWSSchema();
	}//end of getNewWSSchema
	
	/**
	 * This method basically takes the output of the parsed flysight message in CSV format and builds a GenericRecord out of it.
	 * @param time
	 * @param lat
	 * @param lon
	 * @param hMSL
	 * @param velN
	 * @param velE
	 * @param velD
	 * @param hAcc
	 * @param vAcc
	 * @param sAcc
	 * @param heading
	 * @param cAcc
	 * @param gpsFix
	 * @param numSV
	 * @param event_time
	 */
	public void setWSRecordMessage(String time, double lat, double lon, double hMSL, double velN, double velE, double velD, double hAcc, double vAcc, double sAcc, double heading, double cAcc, int gpsFix, int numSV, long event_time)
	{
		
		this.flightEvent = new GenericData.Record(wsSchema);
		//now lets populate the message
		flightEvent.put("time", time);
		flightEvent.put("lat", lat);
		flightEvent.put("lon", lon);
		flightEvent.put("hMSL", hMSL);
		flightEvent.put("velN", velN);
		flightEvent.put("velE", velE);
		flightEvent.put("velD", velD);
		flightEvent.put("hAcc", hAcc);
		flightEvent.put("vAcc", vAcc);
		flightEvent.put("sAcc", sAcc);
		flightEvent.put("heading", heading);
		flightEvent.put("cAcc", cAcc);
		flightEvent.put("gpsFix", gpsFix);
		flightEvent.put("numSV", numSV);
		flightEvent.put("event_time", event_time);
	}//end of setWSRecordMessage
	
	//very simple returns the flightEvent object, typically after we have called setWSRecordMessages done.
	public GenericRecord getWSRecordMessage()
	{
		return this.flightEvent;
	}//end of getWSRecordMessage
	
	
}//end of class
