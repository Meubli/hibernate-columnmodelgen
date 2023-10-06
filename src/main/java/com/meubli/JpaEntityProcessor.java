package com.meubli;

import com.google.auto.service.AutoService;
import com.meubli.model.MetaEntity;
import com.meubli.model.MetaEntityBuilder;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.ExecutableType;
import javax.persistence.Table;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes(Constants.ENTITY)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class JpaEntityProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnvironment = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {

                Optional<String> table = populateTable(element);

                if (table.isEmpty()) {
                    this.processingEnvironment.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            String.format("La table de l'entité %s n'est pas spécifiée.", element.getSimpleName().toString())
                    );
                    return false;
                }

                MetaEntity metaEntity = new MetaEntityBuilder()
                        .setClassSimpleName(element.getSimpleName().toString())
                        .setTable(table.get())
                        .setMapFieldColumn(populateColumns(element))
                        .setPackageName(processingEnvironment.getElementUtils().getPackageOf(element).getQualifiedName().toString())
                        .createMetaEntity();

                ClassWriter classWriter = new ClassWriter();
                try {
                    classWriter.writeClass(metaEntity, processingEnvironment);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        }

        return true;
    }

    private Optional<String> populateTable(Element element) {
        Optional<String> table = Optional.empty();
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeElement typeElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
            if (typeElement.getQualifiedName().contentEquals(Constants.TABLE)) {

                table = getAnnotationParamValue("name", annotationMirror);
            }
        }
        return table;
    }

    private Map<String, String> populateColumns(Element element) {
        Map<String, String> columnsMap = new HashMap<>();

        this.processingEnvironment.getMessager().printMessage(
                Diagnostic.Kind.NOTE,element.getEnclosedElements().toString()
        );

        for (Element enclosedElement : element.getEnclosedElements()) {
            Optional<AnnotationMirror> annotationMirror = doesElementHaveAnyAnnotation(enclosedElement, Constants.COLUMN);
            if (annotationMirror.isPresent()) {
                String field = enclosedElement.getSimpleName().toString();

                Optional<String> column = getAnnotationParamValue("name", annotationMirror.get());

                column.ifPresent(c -> {
                    columnsMap.put(field, c);
                });
            }
        }

        return columnsMap;
    }

    private Optional<AnnotationMirror> doesElementHaveAnyAnnotation(Element element, String annotationQualifiedName) {

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeElement typeElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
            if (typeElement.getQualifiedName().contentEquals(annotationQualifiedName)) {
                return Optional.of(annotationMirror);
            }
        }

        return Optional.empty();
    }

    private <T> Optional<T> getAnnotationParamValue(String param, AnnotationMirror annotationMirror) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementsValue = annotationMirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementsValue.entrySet()) {
            if (entry.getKey().getSimpleName().contentEquals(param) && entry.getValue() != null) {
                return Optional.of((T)entry.getValue().getValue());
            }
        }
        return Optional.empty();
    }

}
