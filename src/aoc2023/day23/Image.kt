package aoc2023.day23

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun Set<Pos>.buildImage(h: Int, w: Int) {
    val img = BufferedImage(w * 10, h * 10, BufferedImage.TYPE_BYTE_GRAY)
    val g = img.createGraphics()
    repeat(w) { x ->
        repeat(h) { y ->
            g.color = if (y to x in this) Color.white else Color.black
            g.fillRect(x * 10, y * 10, 10, 10)
        }
    }
    ImageIO.write(img,"png", File("local/day23.png"))
}
