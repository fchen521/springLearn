import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GuavaTest {
    public static void main(String[] args) throws InterruptedException, IOException {
       /* //集合创建：
        Map<String, Map<String, String>> map = Maps.newHashMap();
        List<List<Map<String, String>>> list = Lists.newArrayList();

        //集合初始化：
        Set<String> set = Sets.newHashSet("one","two","three");
        List<String> lists = Lists.newArrayList("a","b","c");

        //Splitter用来分割字符串
        String s = " adsf, sd,    fa ,,fa, sd, fasld,fa ";
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        System.out.println(splitter.split(s).toString());//[adsf, sd, fa, fa, sd, fasld, fa]

        //拼接
        String value = Joiner.on("-").skipNulls().join(lists);//跳过null值
        System.out.println(value);//a-b-c

        //表明你想匹配的一个确定的字符。
        boolean b = CharMatcher.is('a').matchesAllOf("aaa");//true
        System.out.println(b);

        int count = CharMatcher.is('b').countIn("ababa");//总共出现的次数 2
        int i = CharMatcher.is('b').indexIn("ababa");//第一次出现目标字符的位置 1
        int in = CharMatcher.is('b').lastIndexIn("ababa");//最后一次出现目标字符的位置 3
        String s1 = CharMatcher.anyOf("ab").removeFrom("ababac");//移除指定字符串c

        System.out.println("开始处理");
        Stopwatch started = Stopwatch.createStarted();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("结束时间"+started.stop());

        File SOURCE = new File("a.txt");
        File TARGET = new File("b.txt");
        Files.copy(SOURCE,TARGET); //copy 文件
        Files.move(SOURCE,TARGET);//移动文件*/

        AtomicInteger integer = new AtomicInteger(1);
        int i = integer.getAndSet(5);
        System.out.println(i);
    }
}


