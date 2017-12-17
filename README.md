# Gobang Board
A gobang game board management library written in Kotlin.  
It can also use for Java. (Need Kotlin Runtime Library)

> Need JVM 1.8 and up  

## Install
You can use `CubeSky Repo` at [https://cubesky-mvn.github.io/](https://cubesky-mvn.github.io)  

## API

First of all, you need a `GobangBoard` 

```kotlin
val gobangboard = GobangBoard().startNewBoard()
```

You can use these method to manage Gobang Board.

### If board is Empty
```kotlin
gobangboard.isEmpty()
```

It will return if the board is empty with boolean value.

### Get next move player
```kotlin
gobangboard.getNextPlayer()
```

This will return `Player.Black` or `Player.White`  
If this play is over, it will return `Player.NONE`

### Get board array
```kotlin
gobangboard.getBoard()
```

This will return a two-dimension 15*15 int array as a board.  
Integer 0 is Empty  
Integer 1 is Player Black  
Integer 2 is Player White

### Wrap int as Player
```kotlin
gobangboard.wrapPlayer(integer: Int)
```

This will return `Player.Black` when input integer 1, return `Player.White` when intput interger 2, others will return `Player.ERROR`

### Get board text graph
If you are use this when you debugging, you can use this method to output board as String Graph.  
It will show last move stone in `[ ]`
```kotlin
gobangboard.getBoardGraph()
```

### Get board text graph when someone is win
If you are use this when you debugging, you can use this method to output board as String Graph.  
It will show why this player is win on related stone in `( )`
```kotlin
gobangboard.getWinBoardGraph()
```

### Place on the board
```kotlin
gobangboard.place(x: Int, y: Int)
```

This will place stone on the board, it will automatically use `getNextPlayer()` to place the stone.  
This will return a boolean true if place is success, boolean false if place is failed.

### If someone is win the game
```kotlin
gobangboard.hasWin()
```

This will return a `BoardState`
In `BoardState` it will return a array on `win` field (or `getWin()` method in java) when somebody is win the game, this array is the why this Player is win. If this play is not end, this will be `null`.  
BoardState `player` field will return `Player.Black` or `Player.White` when someone is win the game.  
If nobody win, it will return `Player.NONE`  
If nobody win and the board is full, it will return `Player.ALL`  
You should call this method when you place a stone.  

### Get last move
```kotlin
gobangboard.getLastMove()
```

This will return a `Move` bean. `Move.point` is the last move point, `Move.player` is the last move player.  

### Get history move
```kotlin
gobandboard.getMoveHistory()
```

This will return a `Move` array base on Move History.  
**Attention:** If you are using `setBoard` method, this feature will disabled. When you call it, it will return an `IllegalStateException`  

### Undo last move
```kotlin
gobandboard.undo()
```

This will undo the last move, if nothing can undo or this game is end, this will return `false`, if undo is success, this will return `true`  

### Force set board state

> This method is not recommend to use, it may cause some problem.  

```kotlin
gobandboard.setBoard()
```

Input a two-dimension 15*15 int array act as a board. Player Black is 1, Player White is 2   
**Attention:** If you use this method, `getMoveHistory` will disabled.

## Rules
This library only include base rules (five in a row). If you want to add other rules, you can use these methods.  
These rules will only called when the base rule is return true  
**Attention:** these method can only called when the board is empty, or it will throw an `IllegalStateException`  

### Add a rule
```kotlin
gobangboard.addRule(Function<BoardChange, Boolean>)
```

This method will add a rule in the game. Give it a function, input is a clone of the board with no placed at this move and the point of this move, so you can not edit board at this time.  
Return if this player can place at this time.

### Clear rule chain
```kotlin
gobangboard.clearRuleChain()
```

This will restore rules to default.


## Dependency

If you are using `Kotlin`, this will no dependency.   
If you are using `Java` , this will depend `Kotlin Standard Library JRE 8 (kotlin-stdlib-jre8)`  

## License
This library is under GPLv3