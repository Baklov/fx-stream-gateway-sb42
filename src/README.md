# FX Stream Gateway SB42

## Overview

FX Stream Gateway е Spring Boot приложение, което събира валутни курсове
от външен provider, записва ги в база данни и ги публикува към message
broker. Системата предоставя JSON и XML API за заявки към текущи и
исторически курсове.

## Architecture

Основни компоненти:

-   XML API
-   JSON API
-   Rates Collector (Scheduled job)
-   Statistics Collector
-   Message Broker (Kafka)
-   RDBS (PostgreSQL)
-   Redis (duplicate request guard)
-   External services (например fixer.io)

## Main Features

-   Scheduled refresh на валутни курсове
-   Persist на snapshot-и в база данни
-   Publish на rate events към Kafka
-   JSON API
-   XML API
-   Duplicate request detection чрез Redis
-   History заявки по период

## Technologies

-   Java 25
-   Spring Boot 4
-   Spring Data JPA
-   PostgreSQL
-   Apache Kafka
-   Redis
-   Jackson (JSON / XML)

## How to Run

### Prerequisites

Трябва да имаш:

-   PostgreSQL
-   Redis
-   Kafka

### Default Ports

PostgreSQL: localhost:5432

Redis: localhost:6379

Kafka: localhost:19092

Application: http://localhost:8090

### Start Application

Run:

FxStreamGatewaySb42Application

или:

mvn spring-boot:run

## JSON API Examples

### Current Rate

POST /api/current

Request:

{ "requestId": "1001", "timestamp": 1776808356000, "client": "test",
"currency": "EUR" }

### History

POST /api/history

Request:

{ "requestId": "1002", "timestamp": 1776808356000, "client": "test",
"currency": "EUR", "periodHours": 1 }

## XML API Examples

### Current

POST /xml_api/command

`<command id="11">`{=html}
`<get consumer="test" currency="EUR" />`{=html} `</command>`{=html}

### History

`<command id="14">`{=html}
`<history consumer="test" currency="EUR" period="1" />`{=html}
`</command>`{=html}

## Duplicate Request Behavior

Системата използва Redis за предотвратяване на повторни заявки.

При повторен requestId ще получиш:

409 CONFLICT

## Project Structure

fx-stream-gateway-sb42/ ├─ src/ ├─ pom.xml └─ README.md

## Notes

-   Използвай уникален requestId при тестове
-   Scheduler refresh работи на интервал
-   Kafka publish се извършва при всеки refresh
