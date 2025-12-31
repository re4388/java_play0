package com.ben.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;


@SupportedAnnotationTypes("com.ben.annotation0.NotEmpty")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class NotEmptyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(NotEmpty.class)) {

            // 僅允許 String
            if (!element.asType().toString().equals("java.lang.String")) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "@NotEmpty 只能用在 String 型態！",
                        element
                );
            }
        }
        return true;
    }
}