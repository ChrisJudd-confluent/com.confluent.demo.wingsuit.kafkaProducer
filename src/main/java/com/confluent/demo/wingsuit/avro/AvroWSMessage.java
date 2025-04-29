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
import org.apache.avro.SchemaBuilder;

public class AvroWSMessage {
	//the schema we want to use use to define messages and send them onto the topic
	Schema ws_schema = null;
	
//standard Constructor for my Avro Message - returns a schema in Flysight format
public AvroWSMessage(){
	ws_schema = SchemaBuilder.record("flightevent").namespace("com.confluent.demo.wsdata")
	.fields()
	.requiredString("time")
	.requiredDouble("lat")
	.requiredDouble("lon")
	.requiredDouble("hMSL")
	.requiredDouble("velN")
	.requiredDouble("velE")
	.requiredDouble("velD")
	.requiredDouble("hAcc")
	.requiredDouble("vAcc")
	.requiredDouble("sAcc")
	.requiredDouble("heading")
	.requiredDouble("cAcc")
	.requiredInt("gpsFix")
	.requiredInt("numSV")
	.requiredLong("event_time")
	.endRecord();
	
}

public Schema getWSSchema() {
	// TODO Auto-generated method stub
	return this.ws_schema;
}


}//end of class
