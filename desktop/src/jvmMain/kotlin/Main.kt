import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.github.chillkev.minesweeper.common.MinesweeperView
import com.github.chillkev.minesweeper.common.model.GameModel
import com.github.chillkev.minesweeper.common.platform.DriverFactory
import java.awt.Desktop
import java.net.URI

@Composable
@Preview
fun Preview() {
    MinesweeperView(
        GameModel(sqlDriver = DriverFactory("").createDriver())
    ) { }
}

fun main() = singleWindowApplication(
    title = "Minesweeper",
    icon = BitmapPainter(useResource("ic_mine.png", ::loadImageBitmap)),
    state = WindowState(size = DpSize(960.dp, 720.dp)),
    resizable = false
) {
    MinesweeperView(
        GameModel(sqlDriver = DriverFactory("minesweeper").createDriver())
    ) { url ->
        Desktop.getDesktop().browse(URI(url))
    }
}
