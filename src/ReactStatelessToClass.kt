package run.karl.reactconvert

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory

internal class ReactStatelessToClass : PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return isCorrectLanguage(element.language.id) && element.node.elementType.toString() == "JS:EQGT"
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val fakeFile = PsiFileFactory.getInstance(project).createFileFromText(Language.findLanguageByID("Flow JS")!!, "class veryGoodClass")

        element.replace(fakeFile)
        println("I am invoke")
    }

    override fun getFamilyName(): String = text

    override fun getText(): String = "Convert function to React class"

    fun isCorrectLanguage(id: String): Boolean = id == "Flow JS" || id == "JavaScript" || id == "JSX"
}