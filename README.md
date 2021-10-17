# Appointment Scheduler Api

This is a simple service allowing users to schedule appointments.

## Api Usage

#### GET /appts/{userid}

 * **userid** : (string)  e.g. "antwil"
 
##### Response

List of all appointments in JSON format
* e.g. [{"userid":"antwil","date":"2021-10-17","time":"12:00", "duration":30}, ..., {...}]
 
#### POST /appts
Schedule a 30 minute appointment for the user at the given date and time. A user cannot have more than 1 appointment scheduled per day. A user cannot schedule appointments in the past.

##### Request body
* userid: (string) e.g. "antwil"
* date: (string) 
  * Format: "YYYY-MM-DD" e.g. "2021-10-17"
    * Date and time must be at least now or in the future, cannot schedule past appointments
* time: (string) 
  * Format: "HH:MM eg: "09:00"
    * Appointment must be scheduled on the hour or half-hour
    * I repeat, date and time must be at least now or in the future, cannot schedule past appointments

##### Response
The appointment made if successful
* e.g. {“userid”:“antwil”,“date”:“2021-12-25”,“time”:“12:00”, "duration":30}

## Project Setup

### Dependencies
* Maven: [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html)
* Project Lombok; [Download](https://projectlombok.org/download) and [install](https://projectlombok.org/setup/overview)
* Docker: [Installation instructions](https://docs.docker.com/engine/install/)

### Build
* `cd` into project root
* `mvn clean install`
* `cd docker`
* `docker build -t appt-api .`
* `docker run -d -p 8080:8080 --name <container-name> appt-api`

### Use
The api should be up and running now on `http://localhost:8080`, here are some sample `curl` commands to get you started
* `curl -v -H "Content-Type: application/json" -d {"userid":"antwil","date":"2021-12-25","time":"06:00"}" http://localhost:8080/appts`
* `curl -v -H "Content-Type: application/json" http://localhost:8080/appts/antwil`


## Project Considerations
Decided not to allow scheduling appointments in the past. Sure we allow weekends and holidays and 2am appointments, but this seemed like a simple and practical thing to add.

Decided to allow any kind of string to be passed in as userid. The account creation service can restrict the username format allowed :) Given more time on the project, I might have been more restrictive on the username format.

Used a simple Java Map to store the appointment details. Given more time, might have integrated with a proper data store.

