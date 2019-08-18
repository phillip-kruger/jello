# Jello
Like Trello, but not

## Build

To build and test the applications:

```shell
mvn clean install
```

## Run

To run the applications:

### Card System
This is the main system that host the Jello board. You can start the application 
using maven:

```shell
cd jello-cards/
mvn clean install -Pwildfly
```

This will download (fisrt time only), install and start a Wildfly application server and deploy the app.

You can see the Web GUI here: http://localhost:8080/cards

### Audit System
The audit system receive change notifications from the Queue and then store it in cache.
It also has a Websocket GUI to display the audit messages in realtime on the Screen.
You can start the application using maven:

```shell
cd jello-audit/
mvn clean install -Pwildfly
```

You can see the Web GUI here: http://localhost:8080/audit/app

(Click on connect to start receiving messages)

## Demo

Once both systems is running, create new cards using the GUI in the card system, and see the log entries in the audit system.
