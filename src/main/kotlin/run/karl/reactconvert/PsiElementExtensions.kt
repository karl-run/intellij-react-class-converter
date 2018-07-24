package run.karl.reactconvert

import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.IElementType

fun Project.makeNode(block: String): PsiElement {
    return PsiFileFactory.getInstance(this).createFileFromText(Language.findLanguageByID("Flow JS")!!, block)
}

fun PsiElement.walkToParent(): PsiElement {
    if (this.parent == null) {
        throw Exception("Oopsie woopsie, could not find a relevant parent.")
    }

    if (this of JSTypes.VARIABLE) return this

    return this.parent.walkToParent()
}

fun PsiElement.type(): IElementType = node.elementType

infix fun PsiElement.of(type: JSTypes): Boolean {
    return this.type().toString() == type.type;
}
