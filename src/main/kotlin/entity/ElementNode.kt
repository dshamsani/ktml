package entity

import shared.Attribute
import shared.Tags


data class ElementNode(val tag: Tags, val attributes: MutableList<Attribute> = mutableListOf()) : Node() {
    var firstChild: Node? = null
    var lastChild: Node? = null
}