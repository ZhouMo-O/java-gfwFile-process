# java-gfwFile-process

java 编写的 gfw 文件处理程序
主要功能有：  
批量替换转发 ip 和端口  
删除一条规则  
新增一条规则

ps:用 java 来做这个 纯粹是为了练手，学习 java，毕竟用屠龙刀杀鸡还是很 emmm.  
如果真的有全局替换这方面需求，建议：

sed -i 's/127.0.0.1#5353/ip#port/' dnsmasq_gfwlist_ipset.conf;  
更多的就要去详细学习 sed 命令了。
