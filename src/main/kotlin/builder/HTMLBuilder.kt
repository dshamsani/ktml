package builder

import entity.ElementNode
import entity.Node
import entity.TextNode
import shared.Attribute
import shared.Tags

class HTMLBuilder {
    private var root: Node? = null
    private var current: Node? = null
    private val stack = ArrayDeque<Node>()

    private fun applyAttributes(attributes: MutableList<Attribute>) =
        attributes.map { """ ${it.name.toString().lowercase()}="${it.value}"""" }.joinToString("")

    private fun indent(depth: Int) = "\t".repeat(depth)
    private fun doctype() = "<!DOCTYPE html>\n"
    private fun openTag(tag: String, depth: Int, attributes: MutableList<Attribute>) =
        "${indent(depth)}<$tag" + applyAttributes(attributes) + ">"

    private fun closeTag(tag: String, depth: Int) = "${indent(depth)}</$tag>\n"
    private fun selfClosingTag(tag: String, depth: Int, attributes: MutableList<Attribute>) =
        "${indent(depth)}<$tag" + applyAttributes(attributes) + " />\n"

    fun create(attributes: MutableList<Attribute> = mutableListOf()) {
        root = ElementNode(Tags.HTML, attributes)
        current = root
    }

    fun append(node: Node) {

        if (root == null || current == null) {
            throw RuntimeException("Initialize builder before append")
        }

        if (current is TextNode) {
            throw RuntimeException("TextNode cannot have children")
        }

        if (current is ElementNode && (current as ElementNode).firstChild == null) {
            (current as ElementNode).firstChild = node
            return
        }

        var tempCurrent = (current as ElementNode).firstChild

        while (tempCurrent?.nextSibling != null) {
            tempCurrent = tempCurrent.nextSibling
        }

        tempCurrent?.nextSibling = node
    }

    fun enter(node: Node) {
        if (root == null || current == null) {
            throw RuntimeException("Initialize builder before enter")
        }

        if (node is ElementNode && node.tag == Tags.HTML) {
            throw RuntimeException("Cannot enter root HTML element")
        }

        stack.addLast(current!!)
        current = node
    }

    fun pop() {
        if (root == null || current == null) {
            throw RuntimeException("Initialize builder before pop")
        }

        current = stack.removeLast()
    }

    fun render(): String {
        return renderNode(root, 0)
    }

    fun renderNode(node: Node?, depth: Int, isInlineContext: Boolean = false): String {
        if (node == null) return ""

        return when (node) {
            is TextNode -> if (!isInlineContext) indent(depth) + node.content + renderNode(
                node.nextSibling,
                depth
            ) + "\n" else node.content + renderNode(node.nextSibling, depth)

            is ElementNode -> {
                val tag = node.tag.name.lowercase()
                val siblings = renderNode(node.nextSibling, depth)

                when {
                    node.tag.isRaw -> openTag(tag, depth, node.attributes)  +
                            node.firstChild?.let { (it as? TextNode)?.content } +
                            closeTag(tag, depth) + siblings

                    node.firstChild == null -> selfClosingTag(tag, depth, node.attributes) + siblings
                    node.tag == Tags.HTML -> doctype() + openTag(
                        tag,
                        0,
                        node.attributes
                    ) + "\n" + renderNode(node.firstChild, depth + 1, node.tag.isInline) + closeTag(tag, 0) + siblings

                    node.firstChild is TextNode -> openTag(tag, depth, node.attributes) + renderNode(
                        node.firstChild,
                        0,
                        isInlineContext = true
                    ) + "</$tag>\n" + siblings

                    else -> openTag(tag, depth, node.attributes) + "\n" + renderNode(
                        node.firstChild,
                        depth + 1,
                        node.tag.isInline
                    ) + closeTag(tag, depth) + siblings
                }
            }
        }
    }


}