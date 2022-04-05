package top.keiskeiframework.file.enums;

import lombok.Getter;

/**
 * <p>
 * 文件魔数枚举
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/11/16 14:25
 */
@Getter
public enum FileTypeEnum {
    //

    JPEG("JPEG", "FFD8FF"),
    JPG("JPG", "FFD8FF"),
    PNG("PNG", "89504E47"),
    GIF("GIF", "47494638"),
    TIFF("TIF", "49492A00"),
    BMP("BMP", "424D"),
    DWG("DWG", "41433130"),
    PSD("PSD", "38425053"),
    RTF("RTF", "7B5C727466"),
    XML("XML", "3C3F786D6C"),
    HTML("HTML", "68746D6C3E"),
    EML("EML", "44656C69766572792D646174653A"),
    DBX("DBX", "CFAD12FEC5FD746F "),
    OLE2("OLE2", "0xD0CF11E0A1B11AE1"),
    XLS("XLS", "D0CF11E0"),
    DOC("DOC", "D0CF11E0"),
    DOCX("DOCX", "504B0304"),
    XLSX("XLSX", "504B0304"),
    MDB("MDB", "5374616E64617264204A"),
    WPB("WPB", "FF575043"),
    EPS("EPS", "252150532D41646F6265"),
    PS("PS", "252150532D41646F6265"),
    PDF("PDF", "255044462D312E"),
    QDF("qdf", "AC9EBD8F"),
    QDB("qbb", "458600000600"),
    PWL("PWL", "E3828596"),
    ZIP("ZIP", "504B0304"),
    RAR("RAR", "52617221"),
    WAV("WAV", "57415645"),
    AVI("AVI", "41564920"),
    RAM("RAM", "2E7261FD"),
    RM("RM", "2E524D46"),
    RMVB("RMVB", "2E524D46000000120001"),
    MPG("MPG", "000001BA"),
    MOV("MOV", "6D6F6F76"),
    ASF("ASF", "3026B2758E66CF11"),
    ARJ("ARJ", "60EA"),
    MID("MID", "4D546864"),
    MP4("MP4", "00000018667479706D70"),
    MP3("MP3", "49443303000000002176"),
    FLV("FLV", "464C5601050000000900"),
    GZ("GZ", "1F8B08"),
    CSS("CSS", "48544D4C207B0D0A0942"),
    JS("JS", "696B2E71623D696B2E71"),
    VSD("VSD", "d0cf11e0a1b11ae10000"),
    WPS("WPS", "d0cf11e0a1b11ae10000"),
    TORRENT("TORRENT", "6431303A637265617465"),
    JSP("JSP", "3C2540207061676520"),
    JAVA("JAVA", "7061636B61676520"),
    CLASS("CLASS", "CAFEBABE0000002E00"),
    JAR("JAR", "504B03040A000000"),
    MF("MF", "4D616E69666573742D56"),
    EXE("EXE", "4D5A9000030000000400"),
    ELF("ELF", "7F454C4601010100"),
    WK1("WK1", "2000604060"),
    WK3("WK3", "00001A0000100400"),
    WK4("WK4", "00001A0002100400"),
    LWP("LWP", "576F726450726F"),
    SLY("SLY", "53520100"),
    ;

    /**
     * 文件后缀名
     */
    private String key;

    /**
     * 文件魔数
     */
    private String value;

    FileTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取文件后缀名对应的魔数
     *
     * @param key 文件后缀名
     * @return 文件魔数
     */
    public static String get(String key) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getKey().equalsIgnoreCase(key)) {
                return fileTypeEnum.getValue();
            }
        }
        return null;
    }
}
