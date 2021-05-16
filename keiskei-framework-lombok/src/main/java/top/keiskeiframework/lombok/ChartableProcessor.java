package top.keiskeiframework.lombok;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * <p>
 * 图表实体类添加 （index, indexNumber）的构造方法
 *
 * public Entity (String index, Long indexNumber) {
 *     super.index = index;
 *     super.indexNumber = indexNumber;
 * }
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/16 01:09
 */
@SupportedAnnotationTypes("top.keiskeiframework.lombok.Chartable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ChartableProcessor extends AbstractProcessor {

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    private JavacElements elementUtils = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        ProcessingEnvironment javacProcessingEnvironment = unwrap(processingEnv);

        this.elementUtils = (JavacElements)processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();

        assert javacProcessingEnvironment != null;
        this.trees = JavacTrees.instance(javacProcessingEnvironment);

        Context context = ((JavacProcessingEnvironment) javacProcessingEnvironment).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        JCTree.JCExpression stringType = generateJcExpression("java.lang.String");
        JCTree.JCExpression longType = generateJcExpression("java.lang.Long");
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Chartable.class);
        set.forEach(element -> {
            if (ElementKind.CLASS.equals(element.getKind())) {
                messager.printMessage(Diagnostic.Kind.NOTE, "++++++++ build chart child start :" + element.getSimpleName());
                JCTree jcTree = trees.getTree(element);
                jcTree.accept(new TreeTranslator() {
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {

                        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
                        // 方法内容
                        statements
                                .append(treeMaker.Exec(treeMaker.Assign(
                                        treeMaker.Select(treeMaker.Ident(names.fromString("super")), names.fromString("index")),
                                        treeMaker.Ident(names.fromString("index"))))
                                )
                                .append(treeMaker.Exec(treeMaker.Assign(
                                        treeMaker.Select(treeMaker.Ident(names.fromString("super")), names.fromString("indexNumber")),
                                        treeMaker.Ident(names.fromString("indexNumber"))))
                                );

                        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());

                        // 生成入参
                        JCTree.JCVariableDecl index = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("index"), stringType, null);
                        JCTree.JCVariableDecl indexNumber = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("indexNumber"), longType, null);

                        List<JCTree.JCVariableDecl> paramList = List.of(index, indexNumber);

                        jcClassDecl.defs = jcClassDecl.defs.prepend(
                                treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), names.fromString("<init>"), treeMaker.TypeIdent(TypeTag.VOID), List.nil(), paramList, List.nil(), body, null)
                        );
                        super.visitClassDef(jcClassDecl);
                    }
                });
                messager.printMessage(Diagnostic.Kind.NOTE, "++++++++ build chart child end :" + element.getSimpleName());
            }
        });

        return true;
    }


    /**
     * 解决idea预编译失败
     */
    public static ProcessingEnvironment unwrap(ProcessingEnvironment processingEnv) {
        if (Proxy.isProxyClass(processingEnv.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(processingEnv);
            try {
                Field field = invocationHandler.getClass().getDeclaredField("val$delegateTo");
                field.setAccessible(true);
                Object o = field.get(invocationHandler);
                if (o instanceof ProcessingEnvironment) {
                    return (ProcessingEnvironment) o;
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "got " + o.getClass() + " expected instanceof com.sun.tools.javac.processing.JavacProcessingEnvironment");
                    return null;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                return null;
            }
        } else {
            return processingEnv;
        }
    }

    /**
     * 获取指定类
     */
    public JCTree.JCExpression generateJcExpression(final String fullNameOfTheClass) {

        String[] fullNameOfTheClassArray = fullNameOfTheClass.split("\\.");

        JCTree.JCExpression expr = treeMaker.Ident(elementUtils.getName(fullNameOfTheClassArray[0]));
        for (int i = 1; i < fullNameOfTheClassArray.length; i++) {
            expr = treeMaker.Select(expr, elementUtils.getName(fullNameOfTheClassArray[i]));
        }
        return expr;
    }
}
