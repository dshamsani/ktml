import builder.*
import shared.Attribute
import shared.AttributeName


fun main() {
    val builder = html (AttributeName.LANG to "en") {
        head {
            meta(AttributeName.CHARSET to "UTF-8")
            meta(AttributeName.VIEWPORT to "width=device-width, initial-scale=1.0")
            title {
                text("Hello, KTML!")
            }
            style {
                text("font-weight: bold;")
            }
        }
        body {
            h1 {
                text("Lorem Ipsum!")
            }
            p {
                text("is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the in.")
            }
            script(AttributeName.SRC to "app.js")
        }
    }


    println(builder.render())
}