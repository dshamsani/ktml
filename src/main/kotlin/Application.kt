import builder.*
import shared.AttributeName

import java.io.File


fun main() {
    val builder = html (AttributeName.LANG to "en") {
        head {
            meta(AttributeName.CHARSET to "UTF-8")
            meta(AttributeName.VIEWPORT to "width=device-width, initial-scale=1.0")
            title {
                text("Hello, KTML!")
            }
            style {
                text("p { font-weight: bold; color: red; } h1 { color: green; }")
            }
        }
        body {
            h1 {
                text("Lorem Ipsum!")
            }
            p {
                text("is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the in.")
            }
        }
    }


    File("examples").mkdirs()
    File("examples/index.html").writeText(builder.render())
}