# Jello
(Like Trello, but not)

This is an example application to demonstrate some of the APIs available under the Jakarta EE Umbrella.

## High Level Overview

TODO

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

(Log in with any email, as long as the password is also that email)

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

TODO: Add some scenarios

## Presentations

If anyone is looking for a talk to give at your local meetups or conferences you are welcome to use all or parts of this demo and presentation. 
You can make it your own or use it as is. You are also welcome to improve the talk and demo and contribute back.

### Known presentations

* [Oracle Code One 2019](https://www.oracle.com/code-one/):  [[Google Slides](http://bit.ly/jakartaee-slides)] - by [Phillip Kruger](https://twitter.com/phillipkruger) (Sept 2019)

<iframe src="https://docs.google.com/presentation/d/e/2PACX-1vQuc9ez8WLZfdkO8YgqRfo4erP2hBfnF_cgA3TikkrSIRoDJQDeP8IreNDj12NDOILGKzPtIy1jVo3-/embed?start=true&loop=true&delayms=3000" frameborder="0" width="960" height="569" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>