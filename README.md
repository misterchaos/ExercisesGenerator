
**项目Github地址**：[ExercisesGenerator](https://github.com/misterchaos/ExercisesGenerator)

**结对项目成员**：[丘丽珊](https://github.com/yozyyyqls) 3218007877 [黄钰朝](https://github.com/misterchaos) 3118005005

## 一、需求分析

| 需求描述                                                     | 是否实现 |
| ------------------------------------------------------------ | -------- |
| 控制生成题目的个数                                           | 是       |
| 控制题目中数值范围                                           | 是       |
| 计算过程不能产生负数，除法的结果必须是真分数，题目不能重复，运算符不能超过3个 | 是       |
| 生成的题目存入执行程序的当前目录下的Exercises.txt文件        | 是       |
| 题目的答案存入执行程序的当前目录下的Answers.txt文件          | 是       |
| 能支持一万道题目的生成                                       | 是       |
| 支持对给定的题目文件和答案文件，判定答案中的对错并进行数量统计 | 是       |
| 统计结果输出到文件Grade.txt                                  | 是       |
| 具有图形化的操作界面                                         | 是       |

## 二、开发计划

| 功能               | 描述                                             | 开发者         | 进度 |
| ------------------ | ------------------------------------------------ | -------------- | ---- |
| 生成题目           | 随机生成操作数和运算符，组成有效的四则运算表达式 | 丘丽珊         | 完成 |
| 计算结果           | 根据生成的表达式，计算生成正确的结果             | 丘丽珊         | 完成 |
| 批卷               | 根据指定的题目文件和答案文件，输出成绩结果       | 黄钰朝         | 完成 |
| UI界面设计         | 设计软件的GUI界面                                | 丘丽珊         | 完成 |
| UI界面实现         | 用Javafx实现GUI界面                              | 黄钰朝         | 完成 |
| 功能测试与故障修复 | 测试程序的功能，修复出现的故障                   | 丘丽珊，黄钰朝 | 完成 |
| 性能分析与优化     | 分析程序执行的性能，优化性能表现                 | 黄钰朝         | 完成 |

## 三、实现方案

### 3.1 项目结构

![Package generator](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412151521.png)

**结构说明**：

上图展示了程序中核心的类和方法，其中GenerateController负责生成题目的功能，CheckController负责批卷功能，两者都依赖于底层的GenerateService，而GenerateService中进行四则运算的功能依赖于OperationService

### 3.2 代码说明

#### 3.2.1 出题功能代码

```java
    /**
     * 生成指定数目包含答案的有效题目
     */
    @Override
    public void generateExercises(int exercisesNum, int numRange) throws IOException {
        int count = 0;
        while (count < exercisesNum) {
            Exercises exercises = generateQuestion(numRange);
            generateAnswer(exercises);
            //有效题目加入队列
            if (validate(exercises, exercisesSet)) {
                //生成可以输出的题目样式
                exercisesQueue.add(exercises);
                count++;
                //放入查重集合
                exercisesSet.add(exercises.getSimplestFormatQuestion());                exercisesSet.add(CalculateUtil.getEqualsExpression(exercises.getSimplestFormatQuestion()));
            }
        }

    }

    /**
     * 生成运算式
     */
    private Exercises generateQuestion(int numRange) {
        Exercises e = new Exercises();
        int signalNum = CalculateUtil.getRandomNum(1, 3);
        e.setOperatorNumber(signalNum);
        for (int i = 0; i < e.getOperatorNumber(); i++) {
            //添加运算符
            e.addValue(i, generateOperator());

        }
        for (int i = e.getOperatorNumber(); i < 2 * e.getOperatorNumber() + 1; i++) {
            //添加运算数
            e.addValue(i, generateNum(numRange));
        }
        return e;
    }

```

**实现思路**：

1. 随机生成运算符个数
2. 根据运算符个数随机生成运算符
3. 根据运算符个数和限定的数值范围随机生成操作数
4. 根据生成的表达式求解答案，求解过程发现表达式不合理就返回null
5. 上一步合法的表达式，根据查重集合检查是否重复，如果不合法，返回第1步
6. 上一步合法的表达式，加入待输出队列和查重集合，如果重复，返回第1步
7. 循环以上1-6，直到生成指定数量的题目

#### 3.2.3 批卷功能代码

```java
/**
* 解析题目文件和答案文件
*/
public List<Exercises> readFile(File exercisesFile, File answerFile) throws Exception {
        BufferedReader exercisesReader = FileUtil.getBufferedReader(exercisesFile);
        BufferedReader answerReader = FileUtil.getBufferedReader(answerFile);
        String question;
        List<Exercises> exercisesList = new LinkedList<>();
        while ((question = exercisesReader.readLine()) != null) {
            String answer = answerReader.readLine();
            //解析成Exercises对象
            Exercises exercises = CalculateUtil.parseExercises(question);
            //填入学生答案
            exercises.setStudentAnswer(CalculateUtil.parseAnswer(answer));
            exercisesList.add(exercises);
        }
        return exercisesList;
 }
/**
 * 批卷功能
 */
public Result checkAnswer(List<Exercises> exercises) throws IOException {
        Result result = new Result();
        for (Exercises e : exercises) {
            generateAnswer(e);
            //填入表格
            CheckController.CHECK_EXERCISES_OBSERVABLE_LIST.add(e);
            if (e.getAnswer().equalsIgnoreCase(e.getStudentAnswer())) {
                result.getCorrectList().add(e.getNumber());
            } else {
                result.getWrongList().add(e.getNumber());
            }
        }
        //将结果输出到文件
        writeCheckResultToFile(result);
        return result;
}
```

**实现思路**：

1. 读取题目文件和学生答案，逐行解析为题目和答案
2. 将解析出的题目和学生答案加入集合
3. 从集合取出题目，计算正确答案
4. 比较正确答案和学生答案
5. 若上一步答案正确，把题号加入正确题目集合，若错误，把题号加入错误题目集合
6. 循环以上1-5，直达文件读取完毕

#### 3.2.3 四则运算功能代码

```java
    /**
     * 生成答案
     */
    @Override
    public void generateAnswer(Exercises e) {
        Queue<String> queue = new LinkedList<>();
        ArrayList<String> eValueList = e.getValueList();

        //将所有运算数进队列
        for (int i = 2 * e.getOperatorNumber(); i > e.getOperatorNumber() - 1; i--) {
            queue.add(eValueList.get(i));
        }

        //取出每个运算符,再从队列取出两个数字进行运算，结果再放入队尾中，直到取完所有运算符，此时队列中的数字为最终答案
        for (int i = e.getOperatorNumber() - 1; i >= 0; i--) {
            String opSymbol = eValueList.get(i);

            //从队列取出两个数字
            String num1 = queue.remove();
            String num2 = queue.remove();
            //计算两数运算后结果
            String answer = OperatorEnum.getEnumByOpSymbol(opSymbol).op(num1, num2);

            //计算过程出现不符合条件的数值，就返回null
            if (answer == null) {
                e.setAnswer(null);
                return;
            }
            queue.add(answer);
        }
        e.setAnswer(queue.remove());
    }
```

**实现思路：**

1. 将题目的所有操作数加入队列
2. 如果还有剩余的运算符，取出题目的一个运算符，如果没有，跳到第7步
3. 从队列头部取出两个操作数
4. 将2和3取出的操作数和运算进行四则运算
5. 如果四则运算返回的结果为null，则结束，返回答案为null
6. 将4的结果加入到队列中，返回第2步
7. 取出队列中元素作为答案返回（此时只剩一个元素）

## 四、效能分析

### 4.1 程序效能

![Snipaste_2020-04-12_12-02-35](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412153939.png)

这是**先后两次执行生成50000道题目功能时程序的资源消耗情况**，可以得到以下结论：

- **内存占用**：刚开始执行功能时占用内存短暂上升，但在功能执行结束后很快触发了GC，内存得到回收
- **线程消耗**：第一次执行时创建了线程，第二次执行时没有创建新线程，说明线程池中线程得到重用
- **线程阻塞**：整个过程只出现一次短暂的线程阻塞
- **CPU占用**：整个任务过程中CPU负载较高，属于CPU密集型应用

### 4.2 性能优化

> 以下测试皆为程序执行生成10000道题目时的性能表现

#### 4.2.1 优化前：(执行过程消耗时间为：74s)

![image-20200411160540375](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152143.png) 

这是优化前的程序执行生成10000道题目的性能表现，可以看到程序中**性能消耗最大的函数是GenerateServiceImpl类的validate函数，其中执行List.contains方法的时间占用达到了96%**

#### 4.2.2 查重算法优化后：(执行过程消耗时间为：11s)

![image-20200412124126667](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152155.png)

由于查重算法中，把题目是否重复的判断写在equals方法中，每次比较都要重新分析题目的运算次序，并且validate方法中直接调用了List.contain方法，从源码来看，contain的内部是逐个遍历再调用equals方法，效率很低，因此改成每次生成题目后，解析出题目的最简式，用一个HashSet去保存题目的最简式，查重时调用Set.contain方法，其内部实现时哈希寻址，因此效率得到提高。改进查重方法后，**validate依然是消耗最大的函数，但占比已经下降到27%.**

#### 4.2.3 多线程并发优化后（执行过程消耗时间为：1s）

![](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412212539.png)

这次改进在查重算法改进的基础至上，将生成题目和输出到文件的方法进行分离，引入线程池和多线程并发执行，最终**将生成10000道题的时间消耗降低至1s**

## 五、测试报告

### 5.1 测试项：生成题目和答案是否符合要求

![image-20200412131120729](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152205.png)

**结果说明：**

以上为测试生成10道数值在2以内的题目的截图，可以看到题目符合要求且答案正确

### 5.2 测试项：批卷功能是否正确判断答案正确与否

修改上一步中输出的Answer.txt中奇数题号的答案，再使用批卷功能

![image-20200412131251036](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152208.png)

查看输出的Grade.txt文件：

![image-20200412131347877](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152305.png)

**结果说明：**

其中标出来奇数编号被修改的题目为错误题号，结果符合预期

### 5.3 测试项：能否支持一万道以上大量题目的生成

在出题模式下执行生成10000道数值范围在5以内的题目的功能

![image-20200412131513215](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152219.png)

查看生成的Exercises.txt文件

![image-20200412131544099](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152225.png)

查看生成的Answer.txt文件

![image-20200412131608853](https://cdn.jsdelivr.net/gh/misterchaos/img/image/20200412152228.png)

**结果说明：**

可以看到程序正确地执行了生成10000道题目的功能，消耗时间1s

## 六、PSP表格 

| PSP2.1                                  | Personal Software Process Stages        | 预估耗时（分钟） | 实际耗时（分钟） |
| --------------------------------------- | --------------------------------------- | ---------------- | ---------------- |
| Planning                                | 计划                                    | 30               | 30               |
| · Estimate                              | · 估计这个任务需要多少时间              | 30               | 30               |
| Development                             | 开发                                    | 1755             | 2145             |
| · Analysis                              | · 需求分析 (包括学习新技术)             | 130              | 180              |
| · Design Spec                           | · 生成设计文档                          | 60               | 35               |
| · Design Review                         | · 设计复审 (和同事审核设计文档)         | 5                | 5                |
| · Coding Standard                       | · 代码规范 (为目前的开发制定合适的规范) | 10               | 5                |
| · Design                                | · 具体设计                              | 200              | 120              |
| · Coding                                | · 具体编码                              | 1200             | 1500             |
| · Code Review                           | · 代码复审                              | 30               | 120              |
| · Test                                  | · 测试（自我测试，修改代码，提交修改）  | 120              | 180              |
| Reporting                               | 报告                                    | 85               | 130              |
| · Test Report                           | · 测试报告                              | 60               | 30               |
| · Size Measurement                      | · 计算工作量                            | 10               | 10               |
| · Postmortem & Process Improvement Plan | · 事后总结, 并提出过程改进计划          | 15               | 90               |
| 合计                                    |                                         | 1870             | 2305             |

## 七、总结 

### 7.1 项目小结

**获得的经验：**

1.实现的过程中到了数据结构的知识，加深对数据结构的理解

2.对项目进行性能优化，加深对多线程知识的理解

3.增加了协作开发的经验

**不足的地方**:

用Java来实现数据结构的效率不够高，GUI界面没有做响应式编程

### 7.2 结对感受

##### 丘丽珊：

1. 这次项目中我们通过互相协作高效地完成了结对项目的任务，**我在这次项目中学习到了很多新的知识，加深了对数据结构的理解，也获得了丰富的合作项目经验**
2. 项目中我负责的是运算式的生成、运算式存储等功能，以及承担部分测试程序的任务，**虽然有挑战但是不会觉得很困难，就算有也被队友在后期解决了**
3. **队友的开发效率很高，使得项目在几天之内就完成了，合作起来比较轻松**。

##### 黄钰朝：

1. 这次项目中我们通过互相协作高效地完成了结对项目的任务，**和别人协作开发的能力得到提高，丰富了合作项目的经验，也学习到了很多新的知识**
2. 我正好在这段时间忙于工作室安康打卡项目的开发，**队友认真负责，承担生成表达式和使用数据结构进行四则运算功能的开发(no error,no warning!)，使得项目进度没有落下**
3. 队友代码风格良好并且注释很详细，对接后很容易看懂实现思路，模块耦合度低，维护起来很轻松，写的工具类谜之好用，后期调用十分省事，并且**容易沟通，开发过程中遇到的问题能够及时地解决**
