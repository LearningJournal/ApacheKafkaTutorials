name := "KafkaTest"

libraryDependencies ++= Seq(
"org.apache.kafka" % "kafka-clients" % "0.10.1.0"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri")
    exclude("org.slf4j", "slf4j-simple")
)