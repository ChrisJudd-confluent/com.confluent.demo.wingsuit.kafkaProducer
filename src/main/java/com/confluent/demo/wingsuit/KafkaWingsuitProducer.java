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



package com.confluent.demo.wingsuit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class KafkaWingsuitProducer {
/**
 * Entry into the class
 * @param args
 */
  public static void main(String[] args) {
    try {
      
      final Properties config = readConfig("client.properties");
      String topic = config.getProperty("confluent.demo.entryTopic");
      System.out.println(".....Starting");

      produce(topic, config);
      
    }
    catch (Exception e) {
    	e.printStackTrace();
    }//end of catch
  }

  /**
   * Method that reads the props file and returns a property object we can use
   * @param configFile - location of the props file to parse
   * @return - A Properties object
   * @throws IOException
   */
  public static Properties readConfig(final String configFile) throws IOException {
    // reads the client configuration from client.properties
    // and returns it as a Properties object
    if (!Files.exists(Paths.get(configFile))) {
      throw new IOException(configFile + " not found.");
    }

    final Properties config = new Properties();
    try (InputStream inputStream = new FileInputStream(configFile)) {
      config.load(inputStream);
    }

    return config;
  }//end of read config

  /**
   * Method that produces messages 
   * @param topic
   * @param config
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws IOException
   */
  public static void produce(String topic, Properties config) throws InterruptedException, ExecutionException, IOException {
      // sets the message serializers
      config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
      config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      config.put("schema.registry.url", config.getProperty("schema.registry.url")); // Add schema registry URL.
      config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
      System.out.println("Properties before Producer creation: " + config); // Debugging line

      Producer<String, GenericRecord> producer = new KafkaProducer<>(config);

      //Ok now lets go get our schema and read a nice file
      String flightData= config.getProperty("confluent.demo.inputfile");
      List<GenericRecord> flightRecords = readCsvAndCreateAvroRecords(flightData, new com.confluent.demo.wingsuit.avro.AvroWSMessage().getWSSchema());

      for (GenericRecord record : flightRecords) {
          System.out.println("......Sending record to topic "+topic);
          System.out.println(record.toString());
          System.out.println("Kafka Producer Configuration: " + config);
          ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<>(topic, record.get("time").toString(), record); // Use time as key
          Future<RecordMetadata> future = producer.send(producerRecord); // Get the Future
          RecordMetadata metadata = future.get(); // Block and get the result
          System.out.println("Message sent successfully. Offset: " + metadata.offset());
          //producer.send(producerRecord);
          //now sleep for 200ms just like flysight does
          Thread.sleep(200);
          System.out.println("record sent....");
      }//end of for loop
      System.out.println("....done");
      // closes and flushes the producer connection
      producer.flush();
      producer.close();
  }//end of produce method
  
  /**
   * This method reads a CSV files and then produces a schema based on an avro schema for data input
   * @param csvFile - the CSV file we will be reading / parsing into AVRO
   * @param schema - the Avro schema we want to parse against
   * @return returns a Generic Record ready for use 
   * @throws IOException
   */
  public static List<GenericRecord> readCsvAndCreateAvroRecords(String csvFile, Schema schema) throws IOException {
      List<GenericRecord> records = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
          String line;
          boolean firstLine = true; // Skip header row if present
          while ((line = br.readLine()) != null) {
              if (firstLine) {
                  firstLine = false;
                  continue; // Skip the header row
              }
              String[] values = line.split(",");

              if (values.length == 14) { // Ensure all 13 columns are present
                  try {
                      GenericRecord record = new GenericData.Record(schema);
                      record.put("time", values[0].trim());
                      record.put("lat", Double.parseDouble(values[1].trim()));
                      record.put("lon", Double.parseDouble(values[2].trim()));
                      record.put("hMSL", Double.parseDouble(values[3].trim()));
                      record.put("velN", Double.parseDouble(values[4].trim()));
                      record.put("velE", Double.parseDouble(values[5].trim()));
                      record.put("velD", Double.parseDouble(values[6].trim()));
                      record.put("hAcc", Double.parseDouble(values[7].trim()));
                      record.put("vAcc", Double.parseDouble(values[8].trim()));
                      record.put("sAcc", Double.parseDouble(values[9].trim()));
                      record.put("heading", Double.parseDouble(values[10].trim()));
                      record.put("cAcc", Double.parseDouble(values[11].trim()));
                      record.put("gpsFix", Integer.parseInt(values[12].trim()));
                      record.put("numSV", Integer.parseInt(values[13].trim()));
                      record.put("event_time", Instant.now().toEpochMilli());
                      records.add(record);
                  } catch (NumberFormatException e) {
                      System.err.println("Invalid number format in line: " + line);
                  }
              } else {
                  System.err.println("Invalid CSV line (expected 14 columns), skipping: " + line);
              }
          }
      }
      return records;
  }//end of readCsvAndCreateAv

}//end of class