package mgi.types.config.draw

import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.exists


fun BufferedImage.toFile(path: Path) {
    if (path.parent?.exists() == false)
        path.parent.toFile().mkdirs()
    ImageIO.write(this, "png", path.toFile())
}

