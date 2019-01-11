#多线程并发编程
##java基础
###java程序运行原理分析





# 锁的概念和synchronized关键字

##JAVA中锁的概念
+ 自旋锁（乐观锁）

为了不放弃CPU执行事件，循环的使用CAS技术对数据尝试进行更新，直到成功
+ 悲观锁

假定会发生并发放冲突，同步所有对数据的相关操作，从读数据开始上锁
+ 乐观锁

假定没有冲突，在修改数据时吐过发现数据和之前的不一致，则读最新数据，修改后重新修改。
+独享锁（写）

给资源加上写锁，线程可以修改资源，其他线程不能再加锁（单写）

    publi你

+共享锁（读）

给资源加上读锁后只能读不能改，其他线程也只能加读锁，不能加写锁（多读）

