import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.github.nian430.minesweeper.common.MinesweeperView
import com.github.nian430.minesweeper.common.model.GameModel
import com.github.nian430.minesweeper.common.platform.DriverFactory
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
    ) { repositoryUrl ->
        Desktop.getDesktop().browse(URI(repositoryUrl))
    }
}
