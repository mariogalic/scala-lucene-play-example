# Scala Lucene Play Framework example

## Endpoints

| Type of search           | HTTP GET Request                               |
| ------------------------ | ---------------------------------------------- |
| search by exact title    | `/conditions/exact?term=Arthritis`             |
| search by fuzzy title    | `/conditions/fuzzy?term=Ahritis`               |
| search by phrase title   | `/conditions/phrase?term=Rheumatoid Treatment` | 
| search by wildcard title | `/conditions/wildcard?term=Rheuma*`            | 
| search by full text      | `/conditions/fulltext?term=Arthritis`          | 

Responses content is `application/json` in the following JSON scheme:

```json
[
  {
    "title": "Arthritis - Living with arthritis",
    "text": "Living with arthritis Living with arthritis isn't easy and carrying out simple, everyday tasks can often be painful and difficult."
  },
  {
    "title": "Arthritis",
    "text": "Introduction Arthritis is a common condition that causes pain and inflammation in a joint."
  }
]

```

## Running

Executing `sbt run` will start the server at `http://localhost:9000`. Test it by hitting [http://localhost:9000/conditions/exact?term=arthritis](http://localhost:9000/conditions/exact?term=arthritis)

## Testing

Run tests with `sbt test`

Run coverage with `sbt clean coverage test coverageReport`

## Implementation

* Uses [Play Framework](https://github.com/playframework/playframework/) to provide REST API
* Uses [lucene4s](https://github.com/outr/lucene4s) to hold in-memory Lucene indexed search engine
* Example `lucene4s` usage: [SimpleSpec.scala](https://github.com/outr/lucene4s/blob/master/src/test/scala/tests/SimpleSpec.scala)
* `FileImporter` reads `conf/data.json` and deserialises into `List[Condition]` where `case class Condition(title: String, text: String)`
* `FileImporter` is designed to fail fast by throwing on any issue with the data import
* `ConditionsRepository` is a singleton providing read-only Lucene search engine functionality
* `ConditionsController` provides implementation of routes

