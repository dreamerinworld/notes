--------------------------------
WeakHashMap						|
--------------------------------
	# 实现了 Map 接口
		* 而且键和值都可以是null

	# 特点
		* 在 WeakHashMap 中,当某个键不再正常使用时,会被从 WeakHashMap 中被自动移除
		* 更精确地说, 对于一个给定的键,其映射的存在并不阻止垃圾回收器对该键的丢弃,这就使该键成为可终止的,被终止,然后被回收
		* 某个键被终止时, 它对应的键值对也就从映射中有效地移除了
	
