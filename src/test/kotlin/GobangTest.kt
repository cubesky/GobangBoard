import org.junit.Test
import party.liyin.gobangboard.GobangBoard

@Test
fun board() {
    val gobangboard = GobangBoard().startNewBoard()
    gobangboard.isEmpty()
}