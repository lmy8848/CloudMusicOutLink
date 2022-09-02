## 个人笔记
> 关于泛型
> ---
>> 什么是泛型？为什么要使用泛型？
> 1：泛型概述
>
> 泛型主要提高了代码的重用性，例如，可以将泛型看成是一个可回收的箱子，如果在箱子上面贴上苹果的标签，那么该箱子就可以用来运送苹果，
> 如果给箱子贴上西瓜的标签，那么该箱子就可以用来运送西瓜。
>
> 2：泛型参数 T
>
> 泛型参数T可以看做是一个占位符，它不是一种类型，它仅仅代表了某种可能的类型，类型参数T可以在使用时用类型来代替
+ (1).泛型又称参数化类型，是jdk5.0出现的新特性，解决数据类型的安全性问题
>
+ (2).在类声明或实例化时，只要指定好需要的具体的类型即可
---
## 读取文件中的学生信息，计算出考试的平均分
> 在硬盘中新建一个txt文件，文件中的内容如下：
> + 姓名:张三丰,成绩:89
> + 姓名:张无忌,成绩:68
> + 姓名:杨过,成绩:84
> + 姓名:谢逊,成绩:61

>提示：

> 先要读取文件内容，思考一个问题：使用哪个方法读取文件？
> 使用Files工具类的readAllLines方法。
> 思考第二个问题：读取后，拿到字符串 ( "姓名:张三丰,成绩:89" )，如何获得成绩？
> 考虑字符串的截取方法或拆分方法
> 截取方法：截取最后两位字符；考虑一个问题：如果成绩是100分呢？考虑第二个问题：如果满分是300分呢
> 拆分方法：先用逗号拆分，拆分成数组，包含两个元素，再对第二个元素，使用冒号拆分，即可拿到成绩的字符串
> 拆分方法：使用冒号拆分，即可拿到成绩的字符串
> 将字符串转换成数字，用于计算
> 取得行数，表示有多少学生成绩，用于计算平均值
```java  
package com.web.Jse.IOStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
public class ReaderFiles {
    public static void main(String[] args) throws Exception {
        int average;
        Path uri = Path.of("data.txt");
        DecimalFormat df = new DecimalFormat("0.00");
        Path path = uri.toAbsolutePath();
        Files.createFile(uri);//创建文件
        List<String> list = Files.readAllLines(path);
        average = list.stream().map(s -> s.split(",")[1].split(":")[1]).mapToInt(Integer::parseInt).sum();
        System.out.println(df.format((double) average/list.size()));

    }
}
``` 
## 线程安全类
+ > 第一代线程安全集合类
+ Vector、Hashtable
+ 是怎么保证线程安排的：使用synchronized修饰方法
+ 缺点：效率低下
+ > 第二代线程非安全集合类
+ ArrayList、HashMap
+ 线程不安全，但是性能好，用来替代Vector、Hashtable
+ 使用ArrayList、HashMap,需要线程安全怎么办呢？
+ Collections.synchronizedList(list);Collections.synchronizedMap(m);
+ 底层使用synchronized代码块锁虽然也是锁住了所有的代码，但是锁在方法里边，并所在方法外边性能可以理解
+ 为稍有提高吧。毕竟进方法本身就要分配资源的
+ 第三代线程安全集合类
+ 在大量并发情况下如何提高集合的效率和安全呢？
+ java.util.concurrent.*
+ ConcurrentHashMap:
+ CopyOnWriteArrayList
+ CopyOnWriteArraySet:注意不是CopyOnWriteHashSet*
+ 底层大都采用Lock锁(1.8的ConcurrentHashMap不使用Lock锁)，保证安全的同时，性能也很高。