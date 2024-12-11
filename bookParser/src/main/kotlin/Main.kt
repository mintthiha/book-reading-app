import org.jsoup.Jsoup
import java.io.File

/**
 * For the correct functioning of this parser, please ensure your html formatted book is placed in the resource folder.
 * Additionally, if your book is separated into multiple HTML files, please ensure they are numbered accordingly.
 * Note that this parser only works for one book at a time.
 */

/**
 * Reference for StringBuilder: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-string-builder/
 *
 * Reference fot mutable List: https://www.geeksforgeeks.org/kotlin-mutablelistof/
 *
 * Reference for toIntOrNull used if the files doesn't have a number we assume it's 0:
 * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-int-or-null.html
 *
 * Reference for parsing a html file:
 * https://jsoup.org/apidocs/org/jsoup/Jsoup.html#parse(java.lang.String,java.lang.String)
 *
 * Reference for methods used in handling spacing, tags, and elements:
 * https://jsoup.org/cookbook/
 * More specifically: https://jsoup.org/cookbook/extracting-data/dom-navigation
*/

fun main() {
    val content = File("src/main/resources")

    val allFiles = content.listFiles()
    val bookHTMLFiles = mutableListOf<File>()

    if (allFiles != null) {
        for (file in allFiles) {
            if (file.name.endsWith(".html")) {
                bookHTMLFiles.add(file)
            }
        }
    }

    if (bookHTMLFiles.isEmpty()) {
        println("No files found in the resource directory")
        return
    }

    //sorting such that if there are multiple files, we know in which order to display them
    //assuming the files are numbered
    val sortedFiles = bookHTMLFiles.sortedBy { file ->
        val fileName = file.name
        var numberPart = ""

        for (char in fileName) {
            if (char.isDigit()) {
                numberPart += char
            }
        }

        numberPart.toIntOrNull() ?: 0
    }

    val fullBookContent = StringBuilder()
    var imageCounter = 1
    var tableCounter = 1

    for (file in sortedFiles) {
        val document = Jsoup.parse(file, "UTF-8")
        val elements = document.body().children()

        for (element in elements) {

            if ( element.tagName() in listOf("h1", "h2", "h3", "h4", "h5", "h6")) {
                fullBookContent.append("\n").append(element.text()).append("\n\n")

            } else if ( element.tagName() == "p") {
                fullBookContent.append(element.text()).append("\n\n")

            } else if ( element.tagName() == "img") {
                fullBookContent.append("\n[Image #$imageCounter placeholder]\n\n")
                imageCounter++

            } else if ( element.tagName() == "table") {
                fullBookContent.append("\n[Table #$tableCounter placeholder]\n\n")
                tableCounter++
            }
        }
    }

    println(fullBookContent.toString())
}
//diffrent h's , make sure all the headings are handled