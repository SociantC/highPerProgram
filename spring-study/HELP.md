# 静态通知调用

- 不同通知统一转换为环绕通知，适配器模式体现
- 无参数绑定通知链执行过程，责任链模式体现.
- 模拟实现 `MethodInvocation`

@Before 前置通知会被转换为 `AspectJMethodBeforeAdvice` 形式，该对象包含以下信息：

- 通知代码从哪儿来
- 切点
- 通知对象如何创建

类型通知： 

`AspectJAroundAdvice` (环绕通知) 

`AspectJAfterReturningAdvice`

`AspectJAfterThrowingAdvice` (环绕通知)

`AspectJAfterAdvice`(环绕通知)



统一转换为`MethodInterceptor`

无论 `ProxyFactory` 基于哪种方式创建代理, 最后干活(调用 advice)的是一个 `MethodInvocation` 对象

- 因为 `advisor` 有多个, 且一个套一个调用, 因此需要一个调用链对象, 即 `MethodInvocation`
- `MethodInvocation` 要知道 advice 有哪些, 还要知道目标, 调用次序如下   

```
将 MethodInvocation 放入当前线程
        |-> before1 ----------------------------------- 从当前线程获取 MethodInvocation
        |                                             |
        |   |-> before2 --------------------          | 从当前线程获取 MethodInvocation
        |   |                              |          |
        |   |   |-> target ------ 目标   advice2    advice1
        |   |                              |          |
        |   |-> after2 ---------------------          |
        |                                             |
        |-> after1 ------------------------------------
```

- 从上图看出, 环绕通知才适合作为 advice, 因此其他 `before`、`afterReturning` 都会被转换成环绕通知
- 统一转换为环绕通知, 体现的是设计模式中的适配器模式

    - 对外是为了方便使用要区分 `before`、`afterReturning`
        - 对内统一都是环绕通知, 统一用 `MethodInterceptor` 表示

此步获取所有执行时需要的 advice (静态)
    a. 即统一转换为 `MethodInterceptor` 环绕通知, 这体现在方法名中的 Interceptors 上
    b. 适配如下

      - `MethodBeforeAdviceAdapter` 将 **@Before** `AspectJMethodBeforeAdvice` 适配为 `MethodBeforeAdviceInterceptor`
            - `AfterReturningAdviceAdapter` 将 **@AfterReturning** `AspectJAfterReturningAdvice` 适配为 `AfterReturningAdviceInterceptor`



静态通知调用

`@Before("execution(* foo(..))")` 不带参数绑定，执行时不需要切点

动态通知调用

`@Before("execution(* foo(..) && arg(x))")` 需要参数绑定，执行时需要切点对象



有参数绑定的通知调用还需要切点，用于对参数进行匹配及绑定

复杂程度高，性能比无参数绑定的通知调用低





# RequestMappingHandleMapping与RequestMappingHandlerAdapter

## DispatcherServlet初始化时机



## DispatcherServlet初始化做了什么

```java
// 处理文件上传
initMultipartResolver(context);
// 本地化
initLocaleResolver(context);

initThemeResolver(context);
// *** 路径映射 *** 
initHandlerMappings(context);
// ***适配不同形式的控制器方法并进行调用 *** 
initHandlerAdapters(context);
// ***解析异常 *** 
initHandlerExceptionResolvers(context);

initRequestToViewNameTranslator(context);
initViewResolvers(context);
initFlashMapManager(context);
```

## RequestMappingHandlerMapping基本用途

```java
public static void main(String[] args) throws Exception {
    // AnnotationConfig：java 配置类构建
    // ServletWebServer: 支持内嵌容器
    AnnotationConfigServletWebServerApplicationContext context =
            new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    // 解析@RequestMapping 注解和派生注解，生成路径与控制器方法之间的映射关系，在bean初始化时候就生成
    RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
    // 获取映射结果 HandlerMethod 控制器方法
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

    for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
        System.out.println(entry.getKey()+"="+entry.getValue());
    }

    // 返回处理器执行链对象
    HandlerExecutionChain chain = handlerMapping
            .getHandler(new MockHttpServletRequest("GET", "/test1"));

    System.out.println(chain);
}
```

```java
@Configuration
@ComponentScan  //默认扫描配置类所在的包和子包
@PropertySource("classpath:application.properties")
// webMvcProperties(spring.mvc)
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})    // 绑定配置文件键值
public class WebConfig {
    /*
     * 1.内嵌 web容器工厂
     * 2.创建DispatcherServlet
     * 3.注册DispatcherServlet SpringMVC的入口
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
    // 如果使用DispatcherServlet 初始化时候添加的组件，不会作为bean
    // 1. 加入RequestMappingHandlerMapping
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet,WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());   //如果有多个servlet,数字小优先级顺序高 默认-1,代表启动时不需要初始化
        return registrationBean;   // 如果一个请求过来，没有其他servlet路径，所有进行匹配
    }

}
```

## RequestMappingHandlerAdapter基本用途

```java
public static void main(String[] args) throws Exception {
        // AnnotationConfig：java 配置类构建
        // ServletWebServer: 支持内嵌容器
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        // 解析@RequestMapping 注解和派生注解，生成路径与控制器方法之间的映射关系，在bean初始化时候就生成
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 获取映射结果 HandlerMethod 控制器方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }

        // 返回处理器执行链对象
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test2");
        request.setParameter("name","张三");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutionChain chain = handlerMapping
                .getHandler(request);
        System.out.println(">>>>>>>>>>>>>");
        MyRequestMappingHandlerAdapter adapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        adapter.invokeHandlerMethod(request, response, (HandlerMethod) chain.getHandler());

        System.out.println(">>>>>>>>>>>>>");
        List<HandlerMethodArgumentResolver> resolvers = adapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver resolver : resolvers) {
            System.out.println(resolver);
        }

        System.out.println(">>>>>>>>>>>>>返回值解析器");
        for (HandlerMethodReturnValueHandler returnValueHandler : adapter.getReturnValueHandlers()) {
            System.out.println(returnValueHandler);

        }
    }
```



## 自定义参数和返回值处理器





# 参数解析器

@RequestParam 

url param/form-data

@RequestParam 类型转换

@RequestParam(name="",defaultValue="${JAVA_HOME}")	// 从spring中获取数据

@RequestParam MultipartFile file 文件

@PathVariable	/test/{id}

@RequestHeader("Content-Type")

@CookieValue

@Value("${JAVA_HOME}") String home,// 从spring中获取数据

特殊类型的参数：HTTPServletRequest request

@ModelAttribute User user1 //name=zhangsan&age=18

User user1

@RequestBody User user3	// 从请求体中获取数据 json





javac 手动编译 

`javac -parameter` MethodParameters 可通过反射获取，接口和不同类都可以

`javac -g`  LocalVariableTable 通过ASM获取，接口无效，普通类可以

javap 反编译

javap -c -v



spring boot 在编译时会加 -parameters

大部分IDE编译时会加 -g

```java
// 反射获取参数名
Method foo = Bean2.class.getMethod("foo",String,class,in.class);
foo.getParameters();
// 
```

# 对象绑定和类型转换

- **两套底层转换接口，一套高层转换接口**

![image-20220503155227327](https://cc-shine.oss-cn-hangzhou.aliyuncs.com/image-20220503155227327.png)

* Printer 把其它类型转为 String
* Parser 把 String 转为其它类型
* Formatter 综合  Printer 与 Parser 功能
* Converter 把类型 S 转为类型 T
* Printer、Parser、Converter 经过适配转换成 GenericConverter 放入 Converters 集合
* FormattingConversionService 利用其它们实现转换



- **数据绑定**
- **类型转换扩展与绑定器工厂**
- **@DateTimeFormat 注解解析**
- **spring提供的泛型操作技巧**
