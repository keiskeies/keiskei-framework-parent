package top.keiskeiframework.common.aop;

import top.keiskeiframework.common.annotation.data.Chartable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 19:00
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("top.keiskeiframework.*")
public class MyAnnotaionProcessor extends AbstractProcessor {
    public MyAnnotaionProcessor() {
        super();
    }

    @Override
    public boolean process(Set annotations, RoundEnvironment roundEnv) {
        for (Element elem : roundEnv.getElementsAnnotatedWith(Chartable.class)) {
            addToString(elem);
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        }
        return true;
    }
}
