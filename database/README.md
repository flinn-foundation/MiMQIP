# MiMQIP Database
Quality improvement project to improve mental health treatment by translating evidence-based medication guidelines into daily clinical practices.
This application was orginaly developed for the Flinn Foundation in mid 2000s and is being refactored

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Load MySQL

### Installing
You need to load test data locally for testing.  This is currently done via a sql script '20130507-050001-flinn.sql'.
1. We will need to create a database
```
CREATE DATABASE MiMQIP;
```
1. We will need to add a user
```
CREATE USER 'mqip'@'localhost' IDENTIFIED BY 'mqip';
```
1. Grant that user access to the MiMQIP database
```
```

1. Run the script to create the tables and load data
```
SOURCE *absolute-path*/20130507-050001-flinn.sql
```


## Running the tests


## Deployment

Add additional notes about how to deploy this on a live system

## Built With