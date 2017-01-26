name := "AvroTest"

val repositories = Seq(
  "confluent" at "http://packages.confluent.io/maven/",
  Resolver.sonatypeRepo("public")
)

libraryDependencies ++= Seq(
"org.apache.avro" % "avro" % "1.8.1",
"io.confluent" % "kafka-avro-serializer" % "3.1.1",
"org.apache.kafka" % "kafka-clients" % "0.10.1.0"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri")
    exclude("org.slf4j", "slf4j-simple")
)

resolvers += "confluent" at "http://packages.confluent.io/maven/"
