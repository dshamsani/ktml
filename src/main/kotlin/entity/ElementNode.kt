package entity

import shared.Attribute
import shared.Tags


class ElementNode(val tag: Tags) : Node() {
    var firstChild: Node? = null
    val attributes = mutableListOf<Attribute>()
}