import builder.HTMLBuilder
import entity.ElementNode
import entity.TextNode
import shared.Tags


fun main() {
    val builder = HTMLBuilder()
    builder.create()

    val head = ElementNode(Tags.HEAD)
    val meta = ElementNode(Tags.META)

    val body = ElementNode(Tags.BODY)
    val h1 = ElementNode(Tags.H1)
    val p = ElementNode(Tags.P)


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

    println(builder.render())
}