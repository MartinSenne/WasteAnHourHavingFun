# Rock-Paper-Scissors 

(a.k.a. WasteAnHourHavingFun)

This is an implementation of the famous **Rock Paper Scissors** game in Scala (Console app) without using any external libraries or frameworks.

## Rules 
Two players participate in a game. A game is played in rounds. A round is determined by each of the two players showing a "weapon", the better one wins. See
[wiki](https://en.wikipedia.org/wiki/Rock-paper-scissors) for details on which weapon beats the opponents weapon. (e.g. rock beats scissors)

According to (German) rules given in [German wiki]([https://de.wikipedia.org/wiki/Schere,_Stein,_Papier) a game is won
 if one player has won two round, such has gained two wins. 

## Features

* The current implementation 
  * supports different play-modes (computer vs. computer, human vs. computer, human vs. human),
  * obeys the (German) rules of paper-scissors stone and
  * does not allow for a "draw" match, such that a match is continued as long as `n` rounds, none of themn "draw", have been played.
  
## Playing the game

After starting the game, you are first required to setup the player configuration. 
Select

* `a` for a computer vs computer match
* `b` for a human vs computer match
* `c` for a human vs human match
 
If human players are involved, they are required to enter their names. Please do so to proceed.
 
Gamesetup is done, each round is played asking you for *rock*, *paper* or *scissors*.

### Playmodes 
 
#### Computer vs. computer
 
Simple watch the computers bashing each other.
 
#### Human vs. computer
 
After the player configuration has been selected to be human vs. computer and your name was entered,
you can do your moves.
 
* `1` stands for rock
* `2` stands for paper
* `3` stands for scissors
 
Start with entering a number to perform your first move. After doing so, the choice of 
your computer opponent is displayed.
Subsequent entries of your moves will push the game forward, until at least two rounds are won by one player.
  
In the end, the winner and the final score is presented. An example game-flow is presented below.

#### Human vs. computer

In this mode, you can compete with another human player. Moves are done alternating between both of the two players.

### Example game flow


```
Select gametype: a) Computer vs. Computer, b) Human vs. Computer, c) Human vs. Human: b
Player name for Player A: Martin
Player A (Martin): Please select from 1) Rock, 2) Paper, 3) Scissors: 1
Player B (Computer Watson) made choice Scissors

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Scissors             1:0
Player A (Martin): Please select from 1) Rock, 2) Paper, 3) Scissors: 2
Player B (Computer Watson) made choice Rock

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Scissors             1:0
Paper                Rock                 1:0
===============================================
Winner is Martin.              Endresult: 2:0
```
------

## Development

### Build

Clone the repo [WasteAnHourHavingFun](https://github.com/MartinSenne/WasteAnHourHavingFun) from GitHub via

```
git clone git@github.com:MartinSenne/WasteAnHourHavingFun.git
```

Install sbt according to [sbt manual installation]([http://www.scala-sbt.org/release/tutorial/Manual-Installation.html)


#### Run the game

Start sbt from console via

```
sbt
```

then from with sbt shell execute

```
run
```

For details on input see below in section (guide).
 
#### Execute tests
 
 Start sbt from console via
 
 ```
 sbt
 ```
 
 then from with sbt shell execute
 
 ```
 test
 ``` 
 
 to run all tests.
 
#### Use test coverage - scoverage 

Code coverage is done via the plugin scoverage. Call from sbt

```
set coverageEnabled := true
```

to turn on code coverage. 
A nicely formatted report will be put into `WasteAnHourHavingFun/target/scala-2.11/scoverage-report/index.html` when running `test`.


### Extensibility

* Easy extensibility towards any modulo-based balanced game with an unequal number of weapons. (e.g. rock-paper-scissors-lizard-Spock).  
See (wiki article)[https://en.wikipedia.org/wiki/Rock-paper-scissors#Additional_weapons] for details.
* Easy extensibility towards a real "Client-Server"-based architecture, as `Game` (reflecting the backend)
can be easily adopted to be used from a frontend. In order to do so, wrap parameters for calls to `Game.submitMove` and 
its result `GameStatus` into appropriate JSON objects.
* Fully inplemented by using Model-View-Presenter paradigm.

### Current Deficiencies

Currently, the following deficiencies exist:

* Code-wise
  * Having `var state` in `Game` is ugly. Two possible approaches to improve that situation
    * Use an actor (and the Akka actor framework) to have the game actor encapsulate its mutable state
    



  



