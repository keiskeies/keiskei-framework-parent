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
 * <br>
 * public Entity (String index, Long indexNumber) {
 * <br>
 *     super.index = index;
 * <br>
 *     super.indexNumber = indexNumber;
 * <br>
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
    private JCTree.JCExpression stringType;
    private JCTree.JCExpression longType;

    @Override
    public synchronized void init(ProcessingEnvironment javacProcessingEnvironment) {
        javacProcessingEnvironment = unwrap(javacProcessingEnvironment);

        super.init(javacProcessingEnvironment);


        this.elementUtils = (JavacElements) processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();

        this.trees = JavacTrees.instance(processingEnv);

        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.stringType = generateJcExpression("java.lang.String");
        this.longType = generateJcExpression("java.lang.Long");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Chartable.class);
        if (null != set && set.size() > 0) {
            set.forEach(element -> {
                JCTree jcTree = trees.getTree(element);
                jcTree.accept(new TreeTranslator() {
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        if (ElementKind.CLASS.equals(element.getKind())) {
                            messager.printMessage(Diagnostic.Kind.NOTE, "++++++++ build chart child start :" + element.getSimpleName());
                            // 解决写入文件长度不一致问题
                            treeMaker.pos = jcClassDecl.pos;
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
                            JCTree.JCMethodDecl jcMethodDecl = treeMaker.MethodDef(
                                    treeMaker.Modifiers(Flags.PUBLIC),
                                    names.fromString("<init>"),
                                    treeMaker.TypeIdent(TypeTag.VOID),
                                    List.nil(),
                                    paramList,
                                    List.nil(),
                                    body,
                                    null
                            );
                            jcClassDecl.defs = jcClassDecl.defs.prepend(jcMethodDecl);
                            messager.printMessage(Diagnostic.Kind.NOTE, "++++++++ build chart child end :" + element.getSimpleName());
                        }
                        super.visitClassDef(jcClassDecl);
                    }
                });

            });
        }

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
