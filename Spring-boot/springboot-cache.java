--------------------
springboot-cache	|
--------------------
	# 参考
		https://blog.csdn.net/u012373815/article/details/54564076

	# 依赖
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-cache</artifactId>
		</dependency>
	
	# 开启
		@EnableCaching
			* Spring Boot根据下面的顺序去侦测缓存提供者： 
				Generic 
				JCache (JSR-107) 
				EhCache 2.x 
				Hazelcast 
				Infinispan 
				Redis 
				Guava 
				Simple 
			* 除了按顺序侦测外,也可以通过配置属性spring.cache.type 来强制指定,默认是simple类型
				

	# spring 缓存支持,抽象出来了一个 CacheManager和Cache接口
		org.springframework.cache.CacheManager
		org.springframework.cache.Cache
	
	# spring支持的缓存框架
		SimpleCacheManager
			* 直接使用了一个 Collection 来存储
		ConcurrentMapCache
			* 使用 ConcurrentMap 来存储
		EhCacheCacheManager
			* 使用EhCache作为缓存技术
		RedisCacheManager
			* 使用Redis作为缓存技术
	
--------------------
声明式注解			|
--------------------
	@Cacheable
		# 如果存在缓存,直接返回,不存在调用方法获取计算结果,存入缓存
		# 当标记在一个类上时则表示该类所有的方法都是支持缓存的
		# 属性
			@AliasFor("cacheNames")
			String[] value() default {};

			@AliasFor("value")
			String[] cacheNames() default {};

			String key() default "";
				* 默认key生成规则
					- 如果没有参数,则使用0作为key 
					- 如果只有一个参数,使用该参数作为key 
						* 在ehcache中,此时,key-type应该是参数类型
					- 如果又多个参数,使用包含所有参数的hashCode作为key

				* 支持使用SpringEL表达式
					@Cacheable(value = "user", key = "#user.id"),使用user的id作为参数
					public User create(User user)

					@Cacheable(cacheNames="books", key="#map['bookid'].toString()")
					public Book findBook(Map<String, Object> map)

			String keyGenerator() default "";
			String cacheManager() default "";
			String cacheResolver() default "";
			String condition() default "";
			String unless() default "";
				* 如果方法返回null,也会被认为是一种返回值,null也会被缓存,有些时候这不是我们希望的
				* 通过该属性来控制,禁止缓存null,如果结果为null,那么就不缓存
					@Cacheable(value = "post",unless="#result == null")

			boolean sync() default false;

	@CachePut
		# 不论怎么样,都会把方法返回值放入缓存中
		# CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果,而是每次都会执行该方法,并将执行结果以键值对的形式存入指定的缓存中
		# 属性
			@AliasFor("cacheNames")
			String[] value() default {};

			@AliasFor("value")
			String[] cacheNames() default {};

			String key() default "";
			String keyGenerator() default "";
			String cacheManager() default "";
			String cacheResolver() default "";
			String condition() default "";
			String unless() default "";
	
	@CacheEvict
		# 标注在需要清除缓存元素的方法或类上的
		# 当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作
		# 属性
			@AliasFor("cacheNames")
			String[] value() default {};
			@AliasFor("value")
			String[] cacheNames() default {};
			String key() default "";
			String keyGenerator() default "";
			String cacheManager() default "";
			String cacheResolver() default "";
			String condition() default "";
			boolean allEntries() default false;
				* 表示是否需要清除缓存中的所有元素,默认为false,表示不需要
				* 有的时候需要Cache一下清除所有的元素,这比一个一个清除元素更有效率

			boolean beforeInvocation() default false;
				* 清除操作默认是在对应方法成功执行之后触发的,即方法如果因为抛出异常而未能成功返回时也不会触发清除操作
				* 该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素

	@Caching
		# 组合注解,把多个注解整合到一个上
		# 属性
			Cacheable[] cacheable() default {};
			CachePut[] put() default {};
			CacheEvict[] evict() default {};
	
	# 使用自定义注解
		* Spring允许我们在配置可缓存的方法时使用自定义的注解
		* 前提是自定义的注解上必须使用对应的注解进行标注
			@Target({ElementType.TYPE, ElementType.METHOD})
			@Retention(RetentionPolicy.RUNTIME)
			@Cacheable(value="users")		//(*^__^*) 
			public @interface MyCacheable {

			}


--------------------
使用redis			|
--------------------
	# 开启redis的start(具体看redis的笔记)
		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-data-redis</artifactId>
    	</dependency>

	# 配置
		spring.cache.type=redis
	
	# 注解配置
		@Cacheable(value = "name")
			* value 属性,指定了redis 的key名称
		

--------------------
Ehcache3			|
--------------------
	# 依赖
		<dependency>
		    <groupId>org.ehcache</groupId>
		    <artifactId>ehcache</artifactId>
		</dependency>
		<dependency>
		    <groupId>javax.cache</groupId>
		    <artifactId>cache-api</artifactId>
		</dependency>

	# 配置
		spring.cache.type=jcache
		spring.cache.jcache.config=classpath:ehcache/ehcache.xml
	
	# 注解配置
		@Cacheable(value = "name")
			* value属性,便是指定了 ehcache.xml 中的 <cache alias="name">
	
	# 注入使用 CacheManager
		@Autowired
		private CacheManager cacheManager;

		* 注意,该 CacheManager 是:javax.cache.CacheManager 接口
		* api跟 ehcache3 差不多
		* 很显然,实现使用的就是 ehcache3

-----------------------------
CachingConfigurerSupport	 |
-----------------------------
	# 自定义缓存中的一些配置
		* 继承:CachingConfigurerSupport,覆写方法

			import java.lang.reflect.Method;
			import org.springframework.cache.annotation.CachingConfigurerSupport;
			import org.springframework.cache.annotation.EnableCaching;
			import org.springframework.cache.interceptor.KeyGenerator;
			import org.springframework.context.annotation.Configuration;
			/**
			 * 
			 * @author KevinBlandy
			 *
			 */
			@EnableCaching
			@Configuration
			public class RedisCacheConfiguration extends CachingConfigurerSupport{

				//自定义key生成策略
				@Override
				public KeyGenerator keyGenerator() {
					KeyGenerator generator = new KeyGenerator() {
						@Override
						public Object generate(Object target, Method method, Object... params) {
							return target.getClass().getSimpleName() + ":" + method.getName();
						}
					};
					return generator;
				}
			}
	