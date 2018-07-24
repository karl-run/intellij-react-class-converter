package run.karl.reactconvert

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import run.karl.reactconvert.JSTypes.*

internal class ReactStatelessToClass : PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return isCorrectLanguage(element.language.id) && element of ARROW_FUNCTION
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val parent = element.walkToParent()
        println("Hey bois" + parent)
        var node: PsiElement? = element
        while (true) {
            node = node?.prevSibling
            if (node == null) {
                break
            }

            println(node.type())

            when {
                node of ARROW_FUNCTION -> println("Hey is arrow" + node.text)
                node of ATTRIBUTE_LIST -> println("Is alist: " + node.text)
                node of PARAM_LIST -> println("Is plist: " + node.text)
                else -> println("Unknown thingy: " + node.text)
            }
        }
        parent.children[0].replace(project.makeNode("class Cooliosis"))
        println("I am invoke")
    }

    override fun getFamilyName(): String = text

    override fun getText(): String = "Convert function to React class"

    private fun isCorrectLanguage(id: String): Boolean = id == "Flow JS" || id == "JavaScript" || id == "JSX"
}