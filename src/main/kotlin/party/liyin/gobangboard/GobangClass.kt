package party.liyin.gobangboard


public data class Point(val x: Int, val y: Int)
public data class Move(val point: Point, var player: Player)
public data class BoardState(val win: Set<Point>?, val player: Player)
public data class BoardChange(val board: Array<IntArray>, val point: Point)
public enum class Player(val playerInt: Int) {
    Black(1), White(2), ERROR(-1), NONE(0), ALL(10);
}
