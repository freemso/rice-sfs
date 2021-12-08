# Rice Simple Feature Store (rice-sfs)

This is the course project of [COMP 436/536 - Secure and Cloud Computing](https://www.cs.rice.edu/~angchen/classes/COMP436536.html).

This project is advised by [Prof. Chen, Ang](https://www.cs.rice.edu/~angchen/).

This is a maven multi-module project. Please see [pom.xml](./pom.xml) for details.

Project Structure:

- dynamodb: scripts to run DynamoDB locally
- example: notebooks on how to use the system
- sfs-common: common models and clients
- sfs-ingest: ingestion module, Spark jobs
- sfs-registry: feature registry, Spring MVC service
- sfs-serving: feature serving, Spring WebFlux service

