package io.github.kanshanos.jackson.ext.core.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 名字处理工具类，用于姓名脱敏显示
 * 如：马云 -> 马*，马化腾 -> 马**，诸葛孔明 -> 诸葛**
 */
public class NameDesensitizedUtils {

    private static final Set<String> DOUBLE_SURNAME = ImmutableSet.of(
        "欧阳", "太史", "端木", "上官", "司马", "东方", "独孤", "南宫", "万俟", "闻人",
        "夏侯", "诸葛", "尉迟", "公羊", "赫连", "澹台", "皇甫", "宗政", "濮阳", "公冶",
        "太叔", "申屠", "公孙", "慕容", "仲孙", "钟离", "长孙", "宇文", "司徒", "鲜于",
        "司空", "闾丘", "子车", "亓官", "司寇", "巫马", "公西", "颛孙", "壤驷", "公良",
        "漆雕", "乐正", "宰父", "谷梁", "拓跋", "夹谷", "轩辕", "令狐", "段干", "百里",
        "呼延", "东郭", "南门", "羊舌", "微生", "公户", "公玉", "公仪", "梁丘", "公仲",
        "公上", "公门", "公山", "公坚", "左丘", "公伯", "西门", "公祖", "第五", "公乘",
        "贯丘", "公皙", "南荣", "东里", "东宫", "仲长", "子书", "子桑", "即墨", "淳于",
        "达奚", "褚师", "吴铭", "纳兰", "归海", "刘付"
    );

    private static final String PADDING_CHAR = "*";
    private static final int MAX_LENGTH = 5;

    /**
     * 姓名脱敏处理方法
     *
     * @param name 原始姓名
     * @return 脱敏后的姓名
     */
    public static String process(String name) {
        if (StringUtils.isBlank(name)) {
            return StringUtils.EMPTY;
        }

        String lastName = extractLastName(name);
        int maskedLength = Math.min(name.length(), MAX_LENGTH);

        return StringUtils.rightPad(lastName, maskedLength, PADDING_CHAR);
    }

    /**
     * 提取姓（支持复姓）
     *
     * @param name 姓名
     * @return 提取到的姓
     */
    private static String extractLastName(String name) {
        if (name.length() > 2) {
            for (String doubleSurname : DOUBLE_SURNAME) {
                if (name.startsWith(doubleSurname)) {
                    return doubleSurname;
                }
            }
        }
        return name.substring(0, 1); // 单姓
    }

    public static void main(String[] args) {
        List<String> names = Lists.newArrayList("马云", "马化腾", "李彦宏宏宏宏宏宏宏宏", "诸葛", "诸葛亮", "诸葛孔明", "刘付", "刘付阳");
        names.forEach(name -> System.out.println(name + " -> " + process(name)));
    }
}
