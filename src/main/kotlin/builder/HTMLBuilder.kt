package builder

import entity.ElementNode
import entity.Node
import entity.TextNode
import shared.Tags

class HTMLBuilder {
    private var root: Node? = null
    private var current: Node? = null
    private val stack = ArrayDeque<Node>()

    private fun indent(depth: Int) = "\t".repeat(depth)
    private fun doctype() = "<!DOCTYPE html>\n"
    private fun openTag(tag: String, depth: Int) = "${indent(depth)}<$tag>"
    private fun closeTag(tag: String, depth: Int) = "${indent(depth)}</$tag>\n"
    private fun selfClosingTag(tag: String, depth: Int) = "${indent(depth)}<$tag />\n"

    fun create() {
        root = ElementNode(Tags.HTML)
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

    fun renderNode(node: Node?, depth: Int): String {
        if (node == null) return ""

        return when (node) {
            is TextNode -> node.content + renderNode(node.nextSibling, depth)

            is ElementNode -> {
                val tag = node.tag.name.lowercase()
                val siblings = renderNode(node.nextSibling, depth)

                when {
                    node.firstChild == null -> selfClosingTag(tag, depth) + siblings
                    node.tag == Tags.HTML -> doctype() + openTag(tag, 0) + "\n" + renderNode(node.firstChild, depth + 1) + closeTag(tag, 0) + siblings
                    node.firstChild is TextNode -> openTag(tag, depth) + renderNode(node.firstChild,0) + "</$tag>\n" + siblings
                    else -> openTag(tag, depth) + "\n" + renderNode(node.firstChild, depth + 1) + closeTag(tag, depth) + siblings
                }
            }
        }
    }


}