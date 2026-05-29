# ktml

A Kotlin DSL library for building HTML documents programmatically using an OOP approach and a Left-Child Right-Sibling (LCRS) binary tree structure.

## Overview

Instead of writing raw HTML strings, ktml lets you build a DOM tree through Kotlin classes and lambda blocks. The result is a clean, formatted HTML file.

## How it works

ktml uses an **LCRS binary tree** under the hood:
- Each node has a `firstChild` (left) and `nextSibling` (right)
- This allows representing N-ary trees (any number of children) as a binary tree
- Rendering is done through recursive tree traversal

## Usage

```kotlin
import builder.*
import shared.AttributeName
import java.io.File

fun main() {
    val builder = html(AttributeName.LANG to "en") {
        head {
            meta(AttributeName.CHARSET to "UTF-8")
            meta(AttributeName.VIEWPORT to "width=device-width, initial-scale=1.0")
            title {
                text("Hello, ktml!")
            }
            style {
                text("p { color: red; }")
            }
        }
        body {
            h1 {
                text("Lorem Ipsum!")
            }
            p {
                text("Simply dummy text.")
            }
            script(AttributeName.SRC to "app.js")
        }
    }

    File("examples").mkdirs()
    File("examples/index.html").writeText(builder.render())
}
```

## Output

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta viewport="width=device-width, initial-scale=1.0" />
        <title>Hello, ktml!</title>
        <style>p { color: red; }</style>
    </head>
    <body>
        <h1>Lorem Ipsum!</h1>
        <p>Simply dummy text.</p>
        <script src="app.js" />
    </body>
</html>
```

## Features

- **Typesafe tags** — all HTML tags via `Tags` enum, no typos possible
- **Typesafe attributes** — all attributes via `AttributeName` enum
- **Infix attribute syntax** — `AttributeName.CHARSET to "UTF-8"`
- **Prettify** — automatic indentation based on block/inline element rules
- **Raw tags** — `<style>` and `<script>` content rendered as-is
- **Self-closing tags** — automatically detected from tree structure (no children = self-closing)
- **O(1) append** — via `lastChild` pointer, no traversal needed

## Project structure

```
src/main/kotlin/
├── entity/
│   ├── Node.kt          — sealed class, base type
│   ├── ElementNode.kt   — HTML tag node
│   └── TextNode.kt      — leaf text node
├── builder/
│   ├── HTMLBuilder.kt   — tree builder + renderer
│   └── LambdaReceiver.kt — DSL extension functions
└── shared/
    ├── Tags.kt          — all HTML tags enum
    └── AttributeName.kt — all HTML attributes enum
```

## Requirements

- JDK 21+
- Gradle 9+

## Run

```bash
./gradlew runApp
```

Output will be written to `examples/index.html`.
