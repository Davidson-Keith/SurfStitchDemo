This project was the solution for a job interview test as follows:

1. Solution Implementation

You have 1 database table named Product with the following columns: 
• ProductID
• ProductName
• ProductDescription

New products are created via messages consumed off a message broker. Please provide a solution in the language/tech stack of your choice that consumes a message from a queue & saves this message to the Product database table. The message body will be in JSON format & have the same 3 properties as the database columns shown above. The queue is expected to fill up extremely fast, so sequential processing of messages from the queue is not feasible. Please provide evidence of testing, error handling & logging. Finally, describe in your own words how you would deploy this application.

Deliver the solution for review in a way you see fit.

Please note:
• You are NOT expected to configure an actual message broker - you can mock and/or stub the
code that would interact with the queue if you deem it necessary
• You are NOT expected to configure a SQL database, again, you can mock and/or stub the code
that would interact with the database if you deem it necessary
• You should spend no longer than a few hours on this - if you take longer than 6-8 hours you are
doing too much


My Notes
- In a real software task, as I delve into the problem, any questions I have, I would normally go to the stakeholder(s)
for clarification. However, due to the nature of this task, I will make assumptions instead, and treat those
assumptions as the answers as if I had asked the stakeholders. The following are such assumptions:
  - I will assume the ProductID is to be the primary key of the Product table.
  - The message body contains the 3 values of the Product table columns, including the ProductID, and thus the primary
  key. So if there are messages sent with duplicate ProductID values, then there is a conflict in the primary key
  constraint. So I will assume that when a duplicate occurs, the last message received, is NOT to be added to the
  DB. Instead the details are to be logged. There are many other ways to deal with this, but this is the way I will go
  for the purpose of this exercise.
    Due to this assumption, and due to the multiple threads writing to the DB, there can be a problem where multiple
  threads may be simultaneously checking if the same ProductID already exists, both could return saying it doesn't, and
  then both threads attempt to insert a row with that ProductID, thus causing a primary key restraint violation. To
  overcome this, I have used a java.util.concurrent.locks.ReentrantLock to make the checking for the ProductID and the
  insertion into an atomic action. An alternate way to do this might be to put the read and insert into a function in
  the DB. There also may be a way to do it using Hibernate functionality, but I don't know it, couldn't find it with my
  brief research of it, and don't have time to do any more extensive research. So I have gone with the ReentrantLock
  for this exercise, even though it might not be the most efficient or wise solution.

- The DB is created, and all it's crud operations are written, using Spring Boot, an embedded H2 DB, JPA/Hibernate, and
Project Lombok. A whole swathe of boilerplate and configuration dealt with by magic.

- JSON parsing uses the Jackson Project.

- Deployment
Spring Boot applications can be simply packaged into a self-running jar by running Maven's package phase. The resulting
jar is: target/surfstitchdemo-0.0.1-SNAPSHOT.jar

Thus, the simplest deployment is as simple as copying this jar to anywhere you like, and running the following command
in a terminal:
java -jar surfstitchdemo-0.0.1-SNAPSHOT.jar

Alternatively, this could be wrapped up into an exe file for Windows, an application bundle for Mac, or a shell script.

You could also package it up with something more sophisticated such as Docker, allowing easy deployment to the cloud.

Also note that the logback.xml logging configuration file is currently wrapped up in the target jar, but could easily be
extracted to allow changes to the logging configuration without rebuilding the jar.
