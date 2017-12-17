package party.liyin.gobangboard


data class Point(val x: Int, val y: Int)
data class Move(val point: Point, var player: Player)
data class BoardState(val win: Set<Point>?, val player: Player)
data class BoardChange(val board: Array<IntArray>, val point: Point)
enum class Player(val playerInt: Int) {
    Black(1), White(2), ERROR(-1), NONE(0), ALL(10);
}
