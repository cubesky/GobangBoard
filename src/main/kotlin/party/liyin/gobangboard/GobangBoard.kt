@file:JvmName("GobangBoard")

package party.liyin.gobangboard

import java.util.function.Function

class GobangBoard {
    // x 行 y 列
    private val board = Array(15) { IntArray(15) }
    private var nowOnThink = Player.Black
    private var empty = true
    private var boardgamestate = BoardState(null, Player.NONE)
    private val historyMove = mutableListOf<Move>()
    private var historyfeature = true
    private val ruleChain = mutableListOf<Function<BoardChange, Boolean>>()
    private fun warpPlayer(player: Int) = when (player) {
        1 -> Player.Black
        2 -> Player.White
        else -> Player.ERROR
    }

    fun startNewBoard(): GobangBoard {
        (0..14).forEach { x -> (0..14).forEach { y -> board[x][y] = 0 } }
        nowOnThink = Player.Black
        empty = true
        historyfeature = true
        historyMove.clear()
        return this
    }

    fun place(x: Int, y: Int): Boolean {
        if (nowOnThink == Player.NONE) return false
        if (board[x][y] == 0) {
            ruleChain.forEach {
                if (!it.apply(BoardChange(board.clone(), Point(x, y)))) return false
            }
            board[x][y] = nowOnThink.playerInt
            val lastmove = Move(Point(x, y), nowOnThink)
            historyMove.add(lastmove)
            nowOnThink = when (nowOnThink) {
                Player.Black -> Player.White
                Player.White -> Player.Black
                else -> Player.ERROR
            }
            empty = false
            return true
        } else {
            return false
        }
    }

    fun undo(): Boolean {
        if (hasWin().player != Player.NONE) return false
        try {
            val lastmove = getLastMove()
            board[lastmove.point.x][lastmove.point.y] = 0
            nowOnThink = lastmove.player
            historyMove.removeAt(historyMove.size - 1)
            return true
        } catch (cant: NoSuchElementException) {
            return false;
        }
    }

    fun getLastMove(): Move {
        try {
            return historyMove.last()
        } catch (cant: NoSuchElementException) {
            return Move(Point(-1, -1), Player.NONE)
        }
    }
    fun getNextPlayer() = nowOnThink
    fun getBoard() = board
    fun getBoardGraph(): String {
        var graph = ""
        (0..14).forEach { x ->
            (0..14).forEach { y ->
                var prefix = " "
                var suffix = " "
                if (getLastMove().point.x == x && getLastMove().point.y == y) {
                    prefix = "["
                    suffix = "]"
                }
                graph += (prefix + when (board[x][y]) {
                    0 -> "·"
                    1 -> "●"
                    2 -> "○"
                    else -> "  "
                } + suffix)
            }
            graph += "\n"
        }
        return graph
    }

    fun getWinBoardGraph(): String {
        if (boardgamestate.player != Player.NONE) {
            var graph = ""
            (0..14).forEach { x ->
                (0..14).forEach { y ->
                    var prefix = " "
                    var suffix = " "
                    boardgamestate.win.orEmpty().forEach {
                        if (it.x == x && it.y == y) {
                            prefix = "("
                            suffix = ")"
                        }
                    }
                    graph += (prefix + when (board[x][y]) {
                        0 -> "·"
                        1 -> "●"
                        2 -> "○"
                        else -> "  "
                    } + suffix)
                }
                graph += "\n"
            }
            return graph
        } else {
            return "No Win On Board"
        }
    }

    fun isEmpty(): Boolean = empty
    fun setBoard(sBoard: Array<IntArray>) {
        startNewBoard()
        var count = 0
        (0..14).forEach { x ->
            (0..14).forEach { y ->
                board[x][y] = sBoard[x][y]
                if (board[x][y] != 0) count++
            }
        }
        if (count != 0) empty = false
        if (count % 2 == 0) {
            nowOnThink = Player.Black
        } else {
            nowOnThink = Player.White
        }
        historyfeature = false
    }

    fun getMoveHistory(): List<Move> {
        if (!historyfeature) throw IllegalStateException("Feature is disabled")
        return historyMove.toList()
    }

    fun hasWin(): BoardState {
        // 横向
        (0..14).forEach { x ->
            (0..10).forEach { y ->
                if (board[x][y] > 0) {
                    val playerOnThis = board[x][y]
                    if ((y..y + 4).filter { board[x][it] == playerOnThis }.count() == 5) {
                        nowOnThink = Player.NONE
                        val winarray = mutableSetOf<Point>()
                        (y..y + 4).forEach { winarray.add(Point(x, it)) }
                        boardgamestate = BoardState(winarray, warpPlayer(playerOnThis))
                        return boardgamestate
                    }
                }
            }
        }
        // 纵向
        (0..10).forEach { x ->
            (0..14).forEach { y ->
                if (board[x][y] > 0) {
                    val playerOnThis = board[x][y]
                    if ((x..x + 4).filter { board[it][y] == playerOnThis }.count() == 5) {
                        nowOnThink = Player.NONE
                        val winarray = mutableSetOf<Point>()
                        (x..x + 4).forEach { winarray.add(Point(it, y)) }
                        boardgamestate = BoardState(winarray, warpPlayer(playerOnThis))
                        return boardgamestate
                    }
                }
            }
        }
        // 斜向1 左上至右下
        (0..10).forEach { x ->
            (0..10).forEach { y ->
                if (board[x][y] > 0) {
                    val playerOnThis = board[x][y]
                    val pd = mutableListOf<Int>()
                    (x..x + 4).forEach { xx ->
                        val yy = y + (xx - x)
                        pd.add(board[xx][yy])
                    }
                    if (pd.filter { it == playerOnThis }.count() == 5) {
                        nowOnThink = Player.NONE
                        val winarray = mutableSetOf<Point>()
                        (x..x + 4).forEach { xx ->
                            val yy = y + (xx - x)
                            winarray.add(Point(xx, yy))
                        }
                        boardgamestate = BoardState(winarray, warpPlayer(playerOnThis))
                        return boardgamestate
                    }
                }
            }
        }
        // 斜向2 右上至左下
        (0..10).forEach { x ->
            (4..14).forEach { y ->
                if (board[x][y] > 0) {
                    val playerOnThis = board[x][y]
                    val pd = mutableListOf<Int>()
                    (x..x + 4).forEach { xx ->
                        val yy = y - (xx - x)
                        pd.add(board[xx][yy])
                    }
                    if (pd.filter { it == playerOnThis }.count() == 5) {
                        nowOnThink = Player.NONE
                        val winarray = mutableSetOf<Point>()
                        (x..x + 4).forEach { xx ->
                            val yy = y - (xx - x)
                            winarray.add(Point(xx, yy))
                        }
                        boardgamestate = BoardState(winarray, warpPlayer(playerOnThis))
                        return boardgamestate
                    }
                }
            }
        }
        var counts = 0
        (0..14).forEach { x ->
            (0..14).forEach { y ->
                if (board[x][y] > 0) counts++
            }
        }
        if (counts == 15 * 15) {
            boardgamestate = BoardState(null, Player.ALL)
            return boardgamestate
        }
        boardgamestate = BoardState(null, Player.NONE)
        return boardgamestate
    }

    fun clearRuleChain() {
        if (isEmpty()) {
            ruleChain.clear()
        } else {
            throw IllegalStateException("Game is start, you can not edit rules")
        }
    }

    fun addRule(rule: Function<BoardChange, Boolean>) {
        if (isEmpty()) {
            ruleChain.add(rule)
        } else {
            throw IllegalStateException("Game is start, you can not edit rules")
        }
    }
}
