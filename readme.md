# Kafka Wingsuit Producer

This project is a simple Kafka producer application that reads flight data from a CSV file, converts it to Avro format, and sends it to a Kafka topic. It was created as a demonstration for getting started with Confluent Cloud.

**⚠️ Important: Demonstration Purposes Only**

This code is **NOT** built or intended for production use. It should only be considered for prototyping and experimental purposes. For production-ready solutions, please consult Confluent Professional Services.


**⚠️ Important: If you are using Java 23 then you will need the following jvm params **
-Djava.security.manager=allow

**⚠️ Important: This code works best using Java 17**

## Table of Contents

* [Project Overview](#project-overview)
* [Installation](#installation)
* [Usage](#usage)
* [Configuration](#configuration)

## Project Overview

The Kafka Wingsuit Producer reads flight telemetry data (latitude, longitude, altitude, etc.) from a CSV file. Each row in the CSV is transformed into an Avro message according to the `AvroWSMessage` schema and published to a designated Kafka topic. The producer is configured via a `client.properties` file for Kafka broker connection details and Schema Registry information.

## Installation

1.  **Prerequisites:**
    * Java Development Kit (JDK) 8 or higher.
    * Maven (for building the project).
    * An active Confluent Cloud account.
    * A Kafka topic created in your Confluent Cloud environment.
    * The `AvroWSMessage` schema registered in your Confluent Cloud Schema Registry.
    * A `client.properties` file (see [Configuration](#configuration) for details).
    * A CSV input file containing flight data (the path to this file will be specified in `client.properties`) Within the folder demoData is a collection of prepared data files to use.

2.  **Clone the Repository (if applicable):**
    ```bash
    git clone https://github.com/ChrisJudd-confluent/com.confluent.demo.wingsuit.kafkaProducer.git
    cd kafka-wingsuit-producer
    ```

3.  **Build the Project:**
    If you cloned the repository, use Maven to build the JAR file:
    ```bash
    mvn clean package
    ```
    The resulting JAR file will typically be located in the `target` directory.

## Usage

1.  **Prepare the `client.properties` File:**
    Ensure your `client.properties` file is configured correctly with your Confluent Cloud (or local Kafka/Schema Registry) details. See the [Configuration](#configuration) section for more information.

2.  **Prepare the Input CSV File:**
    Make sure the CSV file specified in `client.properties` exists and contains flight data in the expected format (time, lat, lon, hMSL, velN, velE, velD, hAcc, vAcc, sAcc, heading, cAcc, gpsFix, numSV).

3.  **Run the Producer:**
    Execute the JAR file from your terminal, ensuring the `client.properties` file is in the classpath (e.g., in the same directory):
    ```bash
    java -jar target/kafka-wingsuit-producer-1.0-SNAPSHOT.jar
    ```
    (Adjust the JAR file name based on your build output.)

    The producer will read the CSV file, convert each row to an Avro message, and send it to the Kafka topic specified in `client.properties`. The application will print progress information to the console.

## Configuration

The application is configured using a `client.properties` file. Here's an example of its contents:

```properties
# Confluent Cloud or Local Kafka Broker(s)
bootstrap.servers=pkc-your_cluster.europe-west1.gcp.confluent.cloud:9092

# Confluent Cloud API Key and Secret (for authentication)
security.protocol=SASL_SSL
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='YOUR_API_KEY' password='YOUR_API_SECRET';

# Schema Registry URL and credentials
schema.registry.url=https://psrc-your_sr.europe-west1.gcp.confluent.cloud
schema.registry.basic.auth.user.info=YOUR_SR_API_KEY:YOUR_SR_API_SECRET
schema.registry.basic.auth.credentials.source=USER_INFO

# Kafka Topic to send data to
confluent.demo.entryTopic=wingsuit-telemetry-topic

# Input CSV file path
confluent.demo.inputfile=/path/to/your/flight_data.csv