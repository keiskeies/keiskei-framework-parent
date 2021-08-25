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

    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < Objects.requireNonNull(filePath).length; i++) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

            if (new File(oldPath + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

        }

    }

    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);
        ;

        byte[] buffer = new byte[2048];

        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
    }

    public static void go2Fly(ProjectInfo item, String path) {


        Map<String, String> tableModuleMap = new HashMap<>();
        List<String> treeTables = new ArrayList<>();
        for (ModuleInfo module : item.getModules()) {
            for (TableInfo table : module.getTables()) {
                tableModuleMap.put(table.getName(), module.getName());
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


        String startModuleName = item.getName() + "-start";
        String startModulePath = path + "/" + startModuleName;
        go2Fly("server/pom/startModulePom.xml", startModulePath + "/pom.xml", cfg);
        go2Fly("server/application.java", startModulePath +"/src/main/java/top/keiskeiframework/Application.java", cfg);
        go2Fly("server/conf/SwaggerAddition.java", startModulePath +"/src/main/java/top/keiskeiframework/conf/SwaggerAddition.java", cfg);
        go2Fly("server/conf/SwaggerConfig.java", startModulePath +"/src/main/java/top/keiskeiframework/conf/SwaggerConfig.java", cfg);
        go2Fly("server/resources/application.yml", startModulePath +"/src/main/resources/application.yml", cfg);
        go2Fly("server/resources/application-dev.yml", startModulePath +"/src/main/resources/application-dev.yml", cfg);
        go2Fly("server/resources/application-prod.yml", startModulePath +"/src/main/resources/application-prod.yml", cfg);


        go2Fly("server/pom/serverPom.xml", path + "pom.xml", cfg);
        go2Fly("server/build/build.sh", path + "build.sh", cfg);
        go2Fly("server/build/Dockerfile", path + "Dockerfile", cfg);
        for (ModuleInfo module : item.getModules()) {
            cfg.put("module", module);
            String modulePath = path + "/" + module.getName();
            go2Fly("server/pom/modulePom.xml", modulePath + "/pom.xml", cfg);
            String resultFilePath = modulePath + "/src/main/java/" + module.getPackageName().replace(".", "/") + "/";
            for (TableInfo table : module.getTables()) {
                table.setTableName(camelToUnderline(module.getPath() + table.getName()));

                cfg.put("table", table);
                cfg.put("serialVersionUID",getSerialVersionUID());

                go2Fly("server/entity.java", resultFilePath + "entity/" + table.getName() + ".java", cfg);
                go2Fly("server/repository.java", resultFilePath + "repository/" + table.getName() + "Repository.java", cfg);
                if (table.getBuildController()) {
                    go2Fly("server/controller.java", resultFilePath + "controller/" + table.getName() + "Controller.java", cfg);
                }
                go2Fly("server/service.java", resultFilePath + "service/I" + table.getName() + "Service.java", cfg);
                go2Fly("server/impl/serviceImpl.java", resultFilePath + "service/impl/I" + table.getName() + "ServiceImpl.java", cfg);
                for (FieldInfo field : table.getFields()) {
                    if (FieldInfoTypeEnum.DICTIONARY.equals(field.getType()) && !CollectionUtils.isEmpty(field.getFieldEnums())) {
                        cfg.put("field", field);
                        go2Fly("server/enum.java", resultFilePath + "enums/" + table.getName() + upFirst(field.getName()) + "Enum.java", cfg);
                    }
                }
            }
        }


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
        return str.substring(0,1).toUpperCase() + str.substring(1);
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
        return random.nextLong() + "";
    }
}
