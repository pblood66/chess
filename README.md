# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Sequence Diagram
[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h2u6W5WVqlDlWOpeoQADW6EdztOgIuZQiUKRUEiqiVWELwJlhSOlxg1w6d0m5VWT0DfszObQbam+0dlDr8GQ5nKACYnE5us2hmKxjB248pl2M9n0P3VgczJxTF5fP4AtB2OSYAAZCDRJIBNIZLJjvL14qN6p1JqtAzqBJoWei0azC8bwcAcDYgoUNaNnOtwLl8QFLH0ALnLWyZOvKMAIFePKwpe16ouisTYmC6oumGbpkhSBq0i2C6AforxLCaRLhhaHIwNyvIGoKwowP+hgAGQwPB7yhsxbojsRqZ2gK2h5iRBJkSy5Set6QYBkGIYJuakZsdGMDpvGinaah4L6UGPa5pJI6QcWuE8hWVaYDZEkvsWPRTHxuxfGuFl9p8MCga5I7ZOOMBTjOvQeTRoxeZ25kbn5qwBaY6AcPu3h+IEXgoOgF5Xr4zC3ukmSYCFT5FNQr7SAAoue1X1NVzQtF+qg-t0PkJUO4FnECjYdb2TnIcOJnoZh9gFTh+W+vhGJEXKhikWJSkwOSYDprC6a+UxTLiTp5QcTGGnaEKYSbZ1WkRiNUkGbJVmLTty0cCg3CZOtN3yNtZoRoUlolsMEA0GZcayRdLkpgqSoqndBZDcWirKoNvUgqhUGBZVyOFGVYCTtOs47ql6WHoEkK2ue0IwAA4gurJFfepWPswKPFhUFP1U19gLu18UDWBbI2eU-W5s5V3lMgsQKMqVOjKoOHQlLagzYRckLQpS2kitFLrYLaCfSxe3sTyh3A-IJ1A+uA2g0mz7g2bgrKxqRnuhhcvU7Cuu7T9HIHfpyqUwuhlq5d1toVJYtgAAPPLjL5PbVs9UW5Rk7E8uqA5CDVrDYMlKL0KR-72gx7zcdY6U4XdGHeejNHu5pZ4GVHtgPhQNg3DwLqmR+6MKTFQ+OSM8Hr61A07Oc8E3PoLOHOjAAcguaPHDDSMKtrk8LrPoxIUjbKScp7coPLsJwHv8uK1iseqw96urVr4866Jl-fey+2G7bx08WdFuO1npnvcA58yI7D0x8FywinigdeKBZhgIAJLSHdomT2aYbSdxQAHB+390IwOkDAWOi8E5ty9JkE+ahHLCyZlcKYWDxjJSLqcEuYVcaRT6FQ8o+M9x1yJgESwz1MLJBgAAKQgDyFBgQdAIFAFmemfdt6uXKNUSkH4WhgK5t2BKs4W7AG4VAOAEBMJQCgQuWB89hoQVhgLW+6jxFaJ0XogxowjGIyLDIm2AArIRaAD6CJ5MQtEs1-4XQomtNS2t4HaUQQbXkv9Tba3vl9DB10jryH8YAjWYAD5YNCY-X63so5vzCFg2JesRYoIdP-HeMAQCpBQCALMNjoCwlyfIQCVjKB1KgNiZJgdyh+C0EQkBv9mmaNabo6AdiUCwMyaxcolJsC9MMHxQZ1iRmwGEY04ApsCmW2KfDKG81rJmJgDsxxKFyFNmMRjUcfccYRR6Gw2uB5MoBC8Jo0cXpYDAGwC3Qg8REjdzpljZx2dKg1Tqg1Jqxgup8wOcckxFUQ7KW4HgN2nSH4Ireciy24TIj-UBhLBA0koBoLifrbFANDB4tfkkzFT8-pkp9vilOhTdrbMhrg0xS9DmsrIQPOGrLaGYwZtcvGKU9xAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
