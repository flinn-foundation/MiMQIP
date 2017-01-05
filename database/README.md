# MiMQIP Database
Quality improvement project to improve mental health treatment by translating evidence-based medication guidelines into daily clinical practices.
This application was orginaly developed for the Flinn Foundation in mid 2000s and is being refactored

### Prerequisites
Load MySQL

## Getting Started
You need to load test data locally for testing.  This is currently done via a sql script '20130507-050001-flinn.sql'. The following can exicuted from the *mysql* command line.

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
GRANT ALL PRIVILEGES ON MiMQIP.* TO 'mqip'@'localhost' WITH GRANT OPTION;
```
1. Run the script to create the tables and load data (must use absolute
```
USE MiMQIP;
```
1. Run the script to create the tables and load data (*must use absolute path to the file*)
```
SOURCE ??????/20130507-050001-flinn.sql
```
## Deployment

Add additional notes about how to deploy this on a live system

## Built With
