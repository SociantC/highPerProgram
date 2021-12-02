# 线程运行原理

## 查看进程线程方法

### windows

- 任务管理器查看进程和线程数，可用来杀死进程
- `tasklist`查看进程
- `taskkill`杀死进程

### linux

- `ps -fs`查看所有进程
- `ps -fT -p <PID>`查看某个进程PID的所有线程
- `kill`杀死进程
- `top`按大写 H 切换是否显示线程
- `top -H -p <PID>`查看某个进程PID的所有线程

### java

- `jps`查看所有java进程
- `jstack <PID>`查看某个java进程的所有线程状态
- `jconsole`查看某个java线程中线程的运行情况 (图形界面)



**`jconsole`远程监控配置**

- 运行

```shell
java -Djava.rmi.server.hostname=`ip地址` -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=`连接端口` -Dcom.sun.management.jmxremote.ssl=是否安全连接 -Dcom.sun.management.jmtremote.authenticate=是否认证 javamain
```

- 修改 `/etc/hosts`将127.0.0.1映射至主机名
- （如认证访问，需以下步骤）
    - 复制 `jmxremote.password`文件
    - 修改 `jmxremote.password`和 `jmxremote.access`文件权限为600(文件所有者可读写)
    - 连接时填入 `controlRole` `R&D`

## 线程上下文切换（Thread Context Switch）

因为以下原因导致CPU不再执行当前的线程，需要执行另一个线程的代码

- 线程的CPU时间片用完
- 垃圾回收
- 有更高优先级的线程需要执行
- 线程自己调用了sleep、yield、wait、join、park、synchronized、lock等方法

当Context Switch发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，java中对应的概念就是程序计数器（Program Counter Register），`作用是记住下一条JVM指令的之星地址，是线程私有的`

- 线程状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
- Context Switch频繁发生影响性能

## 线程方法

![image-20211109171849748](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211109171849748.png)

![image-20211109172222743](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211109172222743.png)

## 两阶段终止模式

在线程T1中优雅终止T2线程

```java
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        Thread.sleep(3500);
        twoPhaseTermination.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination{
    private Thread monitor;

    public void start(){
        monitor = new Thread(()->{
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    log.debug("handle end");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    currentThread.interrupt();  // 重新设置打断标记
                }

            }
        });

        monitor.start();
    }


    public void stop(){
        monitor.interrupt();
    }
}
```

## 主线程和守护线程

默认情况下，java进程需要等待所有线程运行结束，才会结束，有一种特殊的线程叫做守护线程，只要其它非守护线程结束了，及时守护线程的代码没有执行完，也会强制结束。

```java
log.debug("start running");
Thread t1 = new Thread(()->{
    log.debug("thread start running");
    sleep(2);
    log.debug("thread complete running");
    
},"daemon");
// 设置为守护线程
t1.setDaemon(true);
t1.start();
sleep(1);
log.debug("complate running")；
```

# 并发编程

## synchronized

### 应用互斥

- 阻塞式解决：synchronized、lock
- 非阻塞式解决：原子变量

synchronized 即【对象锁】，采用互斥的方式让同一时刻至多只有一个线程能持有【对象锁】，其他线程再想获取这个【对象锁】时候就会被阻塞住，这样就能保证拥有锁的线程可以安全的执行临界区内的代码，不用担心上下文切换

> 注意
>
> 虽然java中互斥和同步都可以采用synchronized关键字完成，但它们有区别的
>
> - **互斥是保证临界区内的竞态条件发生，同一时刻只有一个线程执行临界区代码**
> - **同步是由于线程执行的前后、顺序不同，需要一个线程等待其它线程运行到某个点**

### synchronized

```java
synchronized(对象)	// 线程1调用、线程2（blocked)
{
//	临界区	
}
```





```java
@Slf4j(topic = "c.Test")
public class Test {
    static int i=0;
    static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
//        int i=0;
        Thread t1 = new Thread(() -> {
            for (int j=0;j<10000;j++){
                synchronized (lock) {
                    i++;
                }
            }

            log.debug("i result:{}",i);
        },"t1");

        Thread t2 = new Thread(()->{
            for (int j=0;j<10000;j++){
                synchronized (lock){
                    i--;
                }

            }
            log.debug("i result:{}",i);
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("final:{}",i);
    }
}
```

**synchronized实际上是用<font  color="red">对象锁保证了临界区内代码的原子性</font>，临界区内的代码对外是不可分割的，不会被线程切换打断**

### 方法上的synchronized

等价于synchronized(this){}

```java
class Test{
    public synchronized void test(){}
}
// 等价于
class Test{
    public void test(){
        synchronized(this){}
    }
}
```

```java
class Test{
    public synchronized static void test(){}
}
// 等价于
class Test{
    public static  void test(){
        synchronized(Test.class){		// 静态方法，对类加锁
        
        }
    }
}
```

## 常见的线程安全类

- String
- Integer
- StringBuffer
- Random
- Vector
- Hashtable
- java.util.concurrent包下的类

以上类的线程安全指的是，多个线程调用他们同一个实例的某个方法时，是线程安全的，也可以理解成

- 他们的每个方法是原子的
- 但需注意，他们多个方法的组合不是原子的

## 变量的线程安全分析

```java
public abstract class Test5 {
    public void bar(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    public abstract void foo(SimpleDateFormat simpleDateFormat);

    public static void main(String[] args) {
        new Test5() {
            @Override
            public void foo(SimpleDateFormat simpleDateFormat) {
                String dateStr = "1998-10-12 00:00:00";
                new Thread(()->{
                    try {
                        simpleDateFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }.bar();
    }
}
```

foo的行为是不确定的，可能导致 不安全的发生，称之为 **外星方法**

# Monitor（锁）

被翻译成 **监视器** 或者 **管程**

每个java对象都可以关联一个Monitor对象，如果使用synchronized给对象上锁（重量级）之后，该对象的 Mark Word 中就被设置为指向 Monitor 对象的指针。

![image-20211115223559269](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211115223559269.png)

- 刚开始Monitor中Owner为null
- 当Thread-2执行synchronized (obj) 就会将Monitor的所有者Owner置为Thread-2，Monitor中只有一个Owner
- 在Thread-2上锁的过程中，如果Thread-3，Thread-4，Thread-5也来执行synchronized (obj)，就会进入`EntryList BLOCKED`


~~~ruby
		|-----------------------------------------------------------------|--------------------|
		|                       Mark Word (64 bit)				    	  |		    State	   |
		|-----------------------------------------------------------------|--------------------|
		| unused:25 | hashCode:31 | unused:1 | age:4 | biased_lock:0 | 01 |		   Normal	   |
		|-----------------------------------------------------------------|--------------------|
		| thread:54 |   epch:31   | unused:1 | age:4 | biased_lock:1 | 01 |		   Biased	   | 
		|-----------------------------------------------------------------|--------------------|
		|                     ptr_to_lock_record:62                  | 00 | Lightweight Locked |
		|-----------------------------------------------------------------|--------------------|
		|                 ptr_to_heavyweight_monirot:62              | 10 | Heavyweight Locked |
		|-----------------------------------------------------------------|--------------------|
		|                                                            | 11 |	   Marked for GC   |
		|-----------------------------------------------------------------|--------------------|
~~~

`ptr_to_heavyweight_monirot`：重量级锁指针

`ptr_to_lock_record`：指向锁记录的指针

## 1.轻量级锁

使用场景：如果一个对人虽然有多线程访问，但多线程的访问时间是错开的（没有竞争），那么可以使用轻量级锁来优化。

轻量级锁对使用者是透明的，即语法仍然是 `synchronized`

```java
// 两个方法同步块，利用同一个对象加锁
static final Object obj = new Object();
public static void method1(){
	synchronized(obj){
		method2();
	}
}

public static void method2(){
	synchronized(obj){
	// 同步块B
    // 锁重入    
	}
}
```

## 2.锁膨胀

如果在尝试加7轻量级锁的过程中，CAS无法成功，这时一种情况就是有其他线程为此对象加上了轻量级锁（有竞争），这时需要进行锁膨胀，将轻量级锁变成重量级锁。

~~~java
static Object obj = new Object();public static void method1(){    synchronized(obj){            }}
~~~

- Thread-1 进行轻量级加锁时，Thread-0 已经对该对象加了轻量级锁

![image-20211117133731019](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211117133731019.png)

- 这时 Thread-1 加轻量级锁失败，进入锁膨胀流程
    - 为Object申请Monitor锁，让Object指向重量级锁地址
    - 然后自己进入Monitor的`EntryList BLOCKED`

![image-20211117134429260](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211117134429260.png)

- 当Thread-0退出同步块解锁时，使用 `cas` 将Mark Word的值恢复给对象头，失败，这时会进入重量级解锁流程，即按照Monitor地址找到Monitor对象，设置Owner为null，唤醒`EntryList`中BLOCKED线程

## 3.自旋优化

重量级锁竞争的时候，可以使用自旋来进行优化，如果当前线程自旋成功（即这时持锁线程已经退出同步块，释放了锁），这时当前线程就可以避免阻塞。

自旋重试成功：

![image-20211118133447955](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211118133447955.png)

自旋重试失败：

![image-20211118133605328](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211118133605328.png)

- java6以后自旋是自适应的，比如对象刚刚一次自旋操作成功，那么本次自旋成功可能性就高，就多自旋几次；否则，少自旋或不自旋。
- 自旋占用CPU时间，多核CPU自旋才能发挥优势
- java7以后不能控制是否开启自旋功能

## 4.偏向锁

> 轻量级锁没有竞争时，每次重入仍然执行CAS操作

java6后引入偏向锁进一步优化，只有第一次使用CAS将线程ID设置成对象的Mark Word头，之后发现这个线程ID是自己的表示没有竞争，不能重新CAS，以后只要不发生竞争，这个对象就归线程所有。

```java
static final Object obj = new Object();public static void m1(){	synchronized(obj){        // 同步块A        m2();    }}public static void m2(){    synchronized(obj){        // 同步块B        m3();    }}public static void m3(){    synchronized(obj){        // 同步块C    }}
```

![image-20211118134859379](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211118134859379.png)

![image-20211118134905636](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211118134905636.png)

- 偏向锁默认开启，是延迟加载的，可以加`JVM`参数 `-XX:BiasedLockingStartupDelay=0`

### 撤销对象的可偏向状态

- 调用对象的hashcode会导致偏向锁被撤销
    - 轻量级锁会在锁记录中记录hashcode
    - 重量级锁会在Monitor中记录hashCode
- 其他线程使用对象
    - 当有其他线程使用偏向锁对象时，会将偏向锁升级为轻量级锁
- 调用wait/notify(wait notify只有重量级锁有)
- 批量重定向



# wait notify

![image-20211121125101531](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211121125101531.png)

```
synchronized(lock){    while(条件不成立){		lock.wait();    }}synchronized(lock){	lock.notifyAll();}
```

## 同步模式之保护性暂停

Guarded Suspension 用在一个线程等待另一个线程的执行结果

- 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个GuardedObject
- 如果有结果不断从一个线程到另一个线程，那么可以使用消息队列
- JDK中，join的实现，Future的实现，采用的就是此模式
- 因为要等待另一方的结果，因此归类到同步模式

## join

```java
public final synchronized void join(long millis)    throws InterruptedException {        long base = System.currentTimeMillis();        long now = 0;        if (millis < 0) {            throw new IllegalArgumentException("timeout value is negative");        }        if (millis == 0) {            while (isAlive()) {                wait(0);            }        } else {            while (isAlive()) {                long delay = millis - now;                if (delay <= 0) {                    break;                }                wait(delay);                now = System.currentTimeMillis() - base;            }        }    }
```

# park unpark

~~~java
// 暂停当前线程LockSupport.park();// 恢复某个线程的运行LockSupport.unpark(已暂停线程对象)
~~~

与Object的wait&notify相比

- wait、notify和notifyAll必须配合Object Monitor一起使用，而unpark不必；
- `park & unpark` 是以线程为单位来【阻塞】【唤醒】线程，而notify智能随机唤醒一个等待线程，`notifyAll`是唤醒所有等待线程，就不准确。

![image-20211125090808895](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211125090808895.png)

# 现场状态转换

![image-20211125112403831](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211125112403831.png)

## 1. NEW-->RUNNABLE

~~~java
new Thread(()->{}).start();
~~~



## 2. RUNNABLE<-->WAITING

线程用 synchronized(obj) 获取对象锁后

1. 调用 **obj.wait()** 方法时，线程从 RUNNABLE --> WAITING
2. 调用 **obj.notify()** ，**obj.notifyAll()** ，**t.interrupt()** 时
    1. 竞争锁成功，线程从 WAITING --> RUNNABLE
    2. 竞争锁失败，线程从 WAITING --> BLOCKED

## 3. RUNNABLE<-->WAITING

- 线程调用 t.join() 方法时，当前线程从 RUNNABLE --> WAITING
    - 注意当前线程在t线程对象的监视器上等待
- t线程运行结束，或调用当前线程的 interrupt() 方法时

## 4. RUNNABLE<-->WAITING

- 线程调用LockSupport.park() 方法会让当前线程从 RUNNABLE --> WAITING
- 调用 LockSupport.unpark(目标线程) 或调用了线程的interrupt() ，会让目标线程从 WAITING --> RUNNABLE

## 5. RUNNABLE<-->TIMED_WAITING

t线程用 synchronized(obj) 获取了对象锁后

- 调用 obj.wait(long n) 方法时， t 线程从RUNNABLE --> TIMED_WAITING
- t线程等待时间超过了 n 毫秒，或调用 obj.notify() ， obj.notifyAll() ，t.interrupt() 时
    - 竞争锁成功，线程从 WAITING --> RUNNABLE
    - 竞争锁失败，线程从 WAITING --> BLOCKED



## 6. RUNNABLE<-->TIMED_WAITING

- 当前线程 调用 t.join(long n) 方法时，当前线程从 RUNNABLE --> TIMED_WAITING

  (注意当前线程在t线程对象的监视器锁上等待)

- 当前线程等待时间超过了n毫秒，或者t线程运行结束，或调用了当前线程的interrupt()，TIMED_WAITING--> RUNNABLE

## 7. RUNNABLE<-->TIMED_WAITING

当前线程调用Thread.sleep(long n) 当前线程从 RUNNABLE --> TIMED_WAITING

当前线程等待时间超过了n毫秒，当前线程从TIMED_WAITING --> RUNNABLE

## 8. RUNNABLE<-->TIMED_WAITING

当前线程调用 `LockSupport.parkNanos(long nanos)` 或 `LockSupport.parkUntil(long mills)`时，当前线程从 RUNNABLE --> TIMED_WAITING

调用`LockSupport.unpark(目标线程)` 或调用了线程的interrupt()，或是等待超时，会让目标线程从TIMED_WAITING --> RUNNABLE

## 9. RUNNABLE<-->BLOCKED

synchronized获取对象锁时如果竞争失败，从 RUNNABLE --> BLOCKED

持obj锁线程的同步代码块执行完毕，会唤醒该对象上所有BLOCKED的线程重新竞争，如果t线程竞争成功，恢复为RUNNABLE



## 10. RUNNABLE-->TERMINATED

RUNNABLE线程运行完毕，进入TERMINATED

# 线程活跃性

## 死锁

## 定位

检测死锁使用jconsole工具，或者使用jps定位进行id，再用jstack定位死锁

```
jps
```

```
jstack
```

## 饥饿

![image-20211129141050758](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20211129141050758.png)



# ReentrantLock

相对于synchronized它具备如下特点：

- 可中断
- 设置超时时间
- 可以设置为公平锁
- 支持多个条件变量

和synchronized一样， 都支持可重入。

```java
// 获取锁,在对象级别保护临界区reentrantLock.lock();try{	// 临界区} finally{	reentrantLock.unlock();}
```



## 可重入

是指同一个线程如果首次获得了这把锁，那么因为它是这个锁的拥有者，因此有权利再次获取这把锁，如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住。

## 可打断

在等待锁过程中，其他线程可以使用interrupt方法进行打断。

```
lock.lockInteruptibly();
```

## 锁超时

立刻失败

```java
public class TestDeadLock {    public static void main(String[] args) {        Chopsticks c1 = new Chopsticks("1");        Chopsticks c2 = new Chopsticks("2");        Chopsticks c3 = new Chopsticks("3");        Chopsticks c4 = new Chopsticks("4");        Chopsticks c5 = new Chopsticks("5");        new Philospher("a",c1,c2).start();        new Philospher("b",c2,c3).start();        new Philospher("c",c3,c4).start();        new Philospher("d",c4,c5).start();        new Philospher("e",c5,c1).start();    }}@Slf4j(topic = "c.Philospher")class Philospher extends Thread{    Chopsticks left;    Chopsticks right;    public Philospher(String name, Chopsticks left, Chopsticks right) {        super(name);        this.left = left;        this.right = right;    }    @Override    public void run() {        while (true) {            if (left.tryLock()){                try {                    if (right.tryLock()){                        try {                            eat();                        }finally {                            right.unlock();                        }                    }                }finally {                    left.unlock();                }            }        }    }    Random random = new Random();    private void eat(){        log.debug("eat");        Sleeper.sleep(random.nextInt(1));    }}class Chopsticks extends ReentrantLock {    String name;    public Chopsticks(String name) {        this.name = name;    }    @Override    public String toString() {        return "Chopsticks{" +                "name='" + name + '\'' +                '}';    }}
```



## 公平锁

ReentrantLock默认是不公平的

## 条件变量

和synchronized相比，synchronized也有条件变量，类似于waitset休息室这种机制，当调价不满足时候进入waitset等待。

ReentrantLock的条件变量比synchronized强大在于，它支持多个条件变量

**使用流程**

- await前需要获得锁
- await执行后，会释放锁，进入 conditionObject 等待
- await的线程被唤醒（或打断、超时）去重新竞争lock锁
- 竞争lock锁成功后，从await后继续执行

await signal

```java
@Slf4j(topic = "c.ReentranctLockCondition")
public class ReentranctLockCondition {
    static ReentrantLock lock = new ReentrantLock();
    static boolean cigarette = false;
    static boolean takeout = false;
    static Condition waitCigaretteSet = lock.newCondition();
    static Condition waitTakeoutSet = lock.newCondition();

    public static void main(String[] args) {

        new Thread(()->{
            lock.lock();
            while (!cigarette) {
                try {
                    log.debug("没有烟，不干活");
                    waitCigaretteSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            log.debug("开始干活了");
        },"等烟的").start();
        new Thread(()->{
            lock.lock();
            while (!takeout) {
                try {
                    log.debug("没有外卖，不干活");
                    waitTakeoutSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            log.debug("开始干活了");
        },"等外卖的").start();

        Sleeper.sleep(1);
        new Thread(()->{
            // 要先获得锁
            lock.lock();
            try {
                takeout = true;
                log.debug("外卖到了");
                waitTakeoutSet.signal();
            }finally {
                lock.unlock();
            }
        },"送外卖的").start();

        new Thread(()->{
            lock.lock();
            try {
                cigarette = true;
                log.debug("烟到了");
                waitCigaretteSet.signal();
            }finally {
                lock.unlock();
            }
        },"送烟的").start();
    }
}
```



## 同步模式之顺序

wait notify

park unpark

await signal

```java
public class TestOrder1 {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{
            waitNotify.print(1,2);
        }).start();
        new Thread(()->{
            waitNotify.print(2,3);
        }).start();
        new Thread(()->{
            waitNotify.print(3,1);
        }).start();
    }
}

class WaitNotify{
    private int flag;
    private int loopNumber;

    public void print(int waitFlag, int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(waitFlag);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
        System.out.println();
    }
}
```

# JMM

共享变量在多线程之间的【可见性】问题与多条指令执行时【有序性】问题

## 内存模型

JMM体现在以下方面：

- 原子性 - 保证指令不会收到线程上下文切换的影响
- 可见性 - 保证指令不会受 cpu 缓存的影响
- 有序性 - 保证指令不会受 cpu 指令并行优化的影响

## 可见性

```java
pubic class Test{
    static boolean result = false;
    
    public staic void main(String[] args) throws InterruptedException{
        new Thread(()->{
            while(!result){
                // 
            }
        }.start();
    }
    Thread.sleep(1000);               
    result = true;
}
// JIT编译处理，导致无法实时读取最新的result值
```

解决方法：

**volatile**

## 可见性 vs 原子性

前面的例子体现的是可见性，它保证的是多个线程之间，一个线程对volatile变量的修改对另一个线程可见，不能保证原子性，仅作用在一个写线程，多个读线程的情况。

> **注意：**
>
> synchoronized 语句块既可以保证代码块的原子性，也可以保证代码块内变量的可见性；但缺点是 synchronized 是属于重量级操作，性能相对较低

## 可见性

JVM调整指令的顺序的特性叫做【指令重排】，多线程下【指令重排】会影响正确性

在不改变程序结果的前提下，指令的各个阶段都可以通过 **重排序** 和 **组合** 来实现 **指令级并行**



## 有序性

jcstress

```
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jcstress -DarchetypeArtifactId=jcstress-java-test-archetype -DgroupId=com.study -DartifactId=ordering -Dversion=1.0
```



# 模式

## 终止模式-两阶段终止模式

## 同步模式-Balking

Balking模式用于一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做，直接返回

```java
public class MonitorService{
    private volatile boolean starting;
    
    public void start(){
        log.info("启动监控线程");
        synchronized(this){
            if(starting){
                return;
            }
            starting = true;
        }
        
        // 启动监控线程逻辑
    }
}
```

它可经常用来实现线程安全的单例：

```java
public final class Singleton{
    private Singleton(){}
    
    private static Singleton INSTANCE = new Singleton();
    
    public static synchronized Singleton getInstance(){
        if(INSTANCE != null){
            return INSTANCE;
        }
        
        INSTANCE = new Singleton();
        return INSTANCE;
    }
}
```

