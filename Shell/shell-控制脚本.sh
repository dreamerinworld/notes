--------------------------------
信号的处理						|
--------------------------------
	# 信号
		信号	值		描 述
		1		SIGHUP	挂起进程
		2		SIGINT	终止进程
		3		SIGQUIT 停止进程
		9		SIGKILL 无条件终止进程
		15		SIGTERM 尽可能终止进程
		17		SIGSTOP 无条件停止进程，但不是终止进程
		18		SIGTSTP 停止或暂停进程，但不终止进程
		19		SIGCONT 继续运行停止的进程

	# 中断进程
		ctrl + c
	

	# 使用trap捕获信号
		* 在信号出现时捕获它们并执行自定义的命令

		trap commands sgnals

		commands
			* 执行的命令
		sgnals
			* 捕获(监听)的信号
		

		* 捕获 ctrl + c
			trap "echo '我知道你摁下了 ctrl + c'" SIGINT
			val=1
			while [ $val -le 10 ]; do
				echo "$val"
				sleep 1
				val=$[$val + 1]
			done
			echo "end"
		

	# 捕获脚本的退出
		* 在trap命令后加上EXIT信号
			trap "echo 'bye...'" EXIT
			echo "Hello"
	
	# 修改或移除捕获
		* 在脚本中的不同位置进行不同的捕获处理,只需重新使用带有新选项的trap命令
		* 修改了信号捕获之后,脚本处理信号的方式就会发生变化
		* 但如果一个信号是在捕获被修改前接收到的,那么脚本仍然会根据最初的trap命令进行处理
	
		* 删除已设置好的捕获,在trap命令与希望恢复默认行为的信号列表之间加上两个破折号
			trap -- SIGINT
	
--------------------------------
后台模式处理					|
--------------------------------
	# 只要在命令后加个&符
		[1]15515	[作业号]:[pid]

	# 当后台进程运行时,它仍然会使用终端显示器来显示STDOUT和STDERR消息

	# 在非控制台下运行脚本,使用 nohup

--------------------------------
作业控制						|
--------------------------------

--------------------------------
调整谦让度						|
--------------------------------
	nice
	renice

--------------------------------
定时作业						|
--------------------------------
	at
	cron



		


		
		
		
		