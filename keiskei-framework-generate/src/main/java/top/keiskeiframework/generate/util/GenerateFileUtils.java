package top.keiskeiframework.generate.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.generate.entity.FieldInfo;
import top.keiskeiframework.generate.entity.ProjectInfo;
import top.keiskeiframework.generate.entity.ModuleInfo;
import top.keiskeiframework.generate.entity.TableInfo;
import top.keiskeiframework.generate.enums.FieldInfoTypeEnum;
import top.keiskeiframework.generate.enums.TableInfoTypeEnum;

import java.io.*;
import java.net.BindException;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 文件生成工具
 * </p>
 * @since 2020/12/27 21:51
 */
public class GenerateFileUtils {

    public static void copyDir(String resource, String target) throws IOException {
        if (!resource.endsWith("/")) {
            resource += "/";
        }
        if (!target.endsWith("/")) {
            target += "/";
        }
        File resourceDir = new File(resource);
        if (!resourceDir.exists()) {
            return;
        }
        File targetDir = new File(target);
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                throw new BindException("存放的目标路径：[" + target + "] 创建失败...");
            }
        }
        File[] resourceFiles = resourceDir.listFiles();
        if (null == resourceFiles || resourceFiles.length == 0) {
            return;
        }

        for (File childResourceFile : resourceFiles) {
            if (childResourceFile.isFile()) {
                File childTargetFile = new File(target + childResourceFile.getName());
                try (
                        FileChannel inc = new FileInputStream(childResourceFile).getChannel();
                        FileChannel ouc = new FileOutputStream(childTargetFile).getChannel();
                ) {
                    ouc.transferFrom(inc, 0, childResourceFile.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (childResourceFile.isDirectory()) {
                if (childResourceFile.getName().startsWith(".") || childResourceFile.getName().endsWith("node_modules")) {
                    continue;
                }
                String dir1 = resource + childResourceFile.getName();
                String dir2 = target + childResourceFile.getName();
                copyDir(dir1, dir2);
            }
        }
    }

    public static void go2Fly(ProjectInfo item, String basePath) {


        Map<String, String> tableModuleMap = new HashMap<>();
        List<String> treeTables = new ArrayList<>();
        for (ModuleInfo module : item.getModules()) {
            for (TableInfo table : module.getTables()) {
                tableModuleMap.put(table.getName(), module.getPath());
                if (TableInfoTypeEnum.TREE.equals(table.getType())) {
                    treeTables.add(table.getName());
                }
            }
        }


        Map<String, Object> cfg = new HashMap<>(6);
        cfg.put("item", item);
        cfg.put("name", item.getName());
        cfg.put("author", item.getAuthor());
        cfg.put("comment", item.getComment());
        cfg.put("modules", item.getModules());
        cfg.put("since", DateTimeUtils.timeToString(LocalDateTime.now()));
        cfg.put("tableModuleMap", tableModuleMap);
        cfg.put("treeTables", treeTables);

        String serverPath = basePath + "/server";


        String startModuleName = item.getName() + "-start";
        String startModulePath = serverPath + "/" + startModuleName;


        go2Fly("server/pom/startModulePom.xml", startModulePath + "/pom.xml", cfg);
        go2Fly("server/application.java", startModulePath + "/src/main/java/top/keiskeiframework/Application.java", cfg);
        go2Fly("server/resources/application.yml", startModulePath + "/src/main/resources/application.yml", cfg);
        go2Fly("server/resources/application-dev.yml", startModulePath + "/src/main/resources/application-dev.yml", cfg);
        go2Fly("server/resources/application-prod.yml", startModulePath + "/src/main/resources/application-prod.yml", cfg);

        String docModuleName = item.getName() + "-doc";
        String docModulePath = serverPath + "/" + docModuleName;

        go2Fly("server/pom/docModulePom.xml", docModulePath + "/pom.xml", cfg);
        go2Fly("server/config/SwaggerAddition.java", docModulePath + "/src/main/java/top/keiskeiframework/doc/config/SwaggerAddition.java", cfg);
        go2Fly("server/config/SwaggerConfig.java", docModulePath + "/src/main/java/top/keiskeiframework/doc/config/SwaggerConfig.java", cfg);

        go2Fly("server/pom/serverPom.xml", serverPath + "/pom.xml", cfg);
        go2Fly("server/.gitignore", serverPath + "/.gitignore", cfg);
        go2Fly("server/build/build.sh", serverPath + "/build.sh", cfg);
        go2Fly("server/build/Dockerfile", serverPath + "/Dockerfile", cfg);
        for (ModuleInfo module : item.getModules()) {
            String modulePath = serverPath + "/" + module.getName();
            cfg.put("module", module);
            go2Fly("server/pom/modulePom.xml", modulePath + "/pom.xml", cfg);
            String resultFilePath = modulePath + "/src/main/java/" + module.getPackageName().replace(".", "/") + "/";
            for (TableInfo table : module.getTables()) {
                table.setTableName(camelToUnderline(module.getPath() + table.getName()));

                cfg.put("table", table);
                cfg.put("serialVersionUID", getSerialVersionUID());

                go2Fly("server/entity.java", resultFilePath + "entity/" + table.getName() + ".java", cfg);
                go2Fly("server/repository.java", resultFilePath + "repository/" + table.getName() + "Repository.java", cfg);
                if (table.getBuildController()) {
                    go2Fly("server/controller.java", resultFilePath + "controller/" + table.getName() + "Controller.java", cfg);
                }
                go2Fly("server/service.java", resultFilePath + "service/I" + table.getName() + "Service.java", cfg);
                go2Fly("server/impl/serviceImpl.java", resultFilePath + "service/impl/" + table.getName() + "ServiceImpl.java", cfg);
                for (FieldInfo field : table.getFields()) {
                    if (FieldInfoTypeEnum.DICTIONARY.equals(field.getType()) && !CollectionUtils.isEmpty(field.getFieldEnums())) {
                        cfg.put("field", field);
                        go2Fly("server/enum.java", resultFilePath + "enums/" + table.getName() + upFirst(field.getName()) + "Enum.java", cfg);
                    }
                }
            }
        }

        String adminPath = basePath + "/admin/";
        String routerPath = adminPath + "src/router";
        for (ModuleInfo module : item.getModules()) {
            cfg.put("module", module);

            for (TableInfo table : module.getTables()) {
                cfg.put("table", table);
                String tablePath = adminPath + "src/views/" + unpFirst(module.getPath()) + "/" + unpFirst(table.getName());
                go2Fly("pages/page.vue", tablePath + "/index.vue", cfg);
            }
            go2Fly("pages/router.js", routerPath + "/modules/" + unpFirst(module.getPath()) + ".js", cfg);
        }
        go2Fly("pages/routerIndex.js", routerPath + "/index.js", cfg);
    }

    public static void go2Fly(String templateFile, String resultFile, Map<String, Object> cfg) {
        File docFile = new File(resultFile);
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            String templatePath = GenerateFileUtils.class.getResource("/ftl/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            Template template = configuration.getTemplate("/" + templateFile + ".ftl");
            docFile.getParentFile().mkdirs();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            template.process(cfg, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String upFirst(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    public static String unpFirst(String str) {
        return str.substring(0, 1).toLowerCase(Locale.ROOT) + str.substring(1);
    }


    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static String getSerialVersionUID() {
        Random random = new Random();
        return random.nextInteger() + "";
    }
}
