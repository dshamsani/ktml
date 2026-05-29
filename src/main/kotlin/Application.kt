import builder.HTMLBuilder
import entity.ElementNode
import entity.TextNode
import shared.Attribute
import shared.AttributeName
import shared.Tags


fun main() {
    val builder = HTMLBuilder()
    builder.create(mutableListOf(Attribute(
        name = AttributeName.LANG,
        value = "en-US"
    )))

    val head = ElementNode(Tags.HEAD)
    val meta = ElementNode(Tags.META, mutableListOf(Attribute(
        name = AttributeName.CHARSET,
        value = "utf-8"
    )))

    val body = ElementNode(Tags.BODY)
    val h1 = ElementNode(Tags.H1)

    val p = ElementNode(
        Tags.P,
        mutableListOf(
            Attribute(
                name = AttributeName.CLASS,
                value = "first-class second-class third-class"
            ),
            Attribute(
                name = AttributeName.ID,
                value = "paragraph"
            )
        )
    )


    val headerText = TextNode("Hello ktml")
    val paragraphText = TextNode("Text from builder")

    builder.append(head)
    builder.append(body)
    builder.enter(body)
    builder.append(h1)
    builder.enter(h1)
    builder.append(headerText)
    builder.pop()
    builder.append(p)
    builder.enter(p)
    builder.append(paragraphText)
    builder.enter(head)
    builder.append(meta)

    // TEST

    val div = ElementNode(Tags.DIV)
    val p2 = ElementNode(Tags.P)
    val span = ElementNode(Tags.SPAN)

    builder.enter(body)
    builder.append(div)
    builder.enter(div)
    builder.append(p2)
    builder.enter(p2)
    builder.append(TextNode("Hello world"))
    builder.pop()
    builder.append(TextNode("Hello world 2"))

    builder.enter(body)
    builder.append(span)
    builder.enter(span)
    builder.append(TextNode("Hello world"))
    builder.pop()
    builder.append(TextNode("Hello world"))

    println(builder.render())
}