package run.karl.reactconvert

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import run.karl.reactconvert.JSTypes.*

data class Convert(
        var params: PsiElement? = null,
        var returnValue: PsiElement? = null
)

fun makeClass(className: String, params: String?, returnBlock: String?): String {
    return """
    class ${className} extends React.Component {
        render() {
            const ${params} = this.props;

            return ${returnBlock}
        }
    }
    """.trimIndent()
}

fun makeRender(returnValue: String?, params: String?): String {
    return """
        render() {
            ${makeReturnValue(params)}
            console.log("Wahoo");
        }
    """.trimIndent()
}

fun makeReturnValue(params: String?): String {
    if (params == null) return ""

    return """
        const {${params
            .removeSurrounding("(", ")")
            .removeSurrounding("{", "}")
    }} = this.props;
    """.trimIndent()
}

internal class ReactStatelessToClass : PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return isCorrectLanguage(element.language.id) && element of ARROW_FUNCTION
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val parent = element.walkToParent()
        var node: PsiElement? = element
        val sections = Convert()
        val extractUsefulSection: (PsiElement) -> Unit = {
            println(it)
            println(it.type())
            println(it.text)
            println()
            when {
                it of PARAM_LIST -> sections.params = it
                it of BLOCK -> sections.returnValue = it
                it of JSX -> sections.returnValue = it
            }
        }
        while (node != null) {
            node = node.prevSibling
            if (node == null) {
                break
            }

            extractUsefulSection(node)
        }

        node = element
        while (node != null) {
            node = node.nextSibling
            if (node == null) {
                break
            }

            extractUsefulSection(node)
        }

        try {
            element.replace(project.makeNode(makeRender(sections.returnValue?.text, sections.params?.text)))
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun getFamilyName(): String = text

    override fun getText(): String = "Convert function to React class"

    private fun isCorrectLanguage(id: String): Boolean = id == "Flow JS" || id == "JavaScript" || id == "JSX"
}