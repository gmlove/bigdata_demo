sync:
	rsync -r ./etc/ rhd01-1:dev/projects/hadoop/hadoop/etc/
	scp Makefile ai01:dev/hadoop/

sync-code:
	rsync -r --exclude='*/target' ./test/ rhd01-1:dev/projects/hadoop/test/

stop-cluster:
	ssh rhd01-1 'cd dev/projects/hadoop/hadoop && sbin/stop-dfs.sh && sbin/stop-yarn.sh && sbin/mr-jobhistory-daemon.sh stop historyserver'

check-status-local:
	for i in 1 2 3 4 5; do echo node $$i; docker exec gmliao-host-hd01-$$i jps; done

check-status:
	ssh ai01 "cd dev/hadoop && make check-status-local"

start-cluster:
	ssh rhd01-1 'cd dev/projects/hadoop/hadoop && sbin/start-dfs.sh && ./sbin/start-secure-dns.sh && sbin/start-yarn.sh && sbin/mr-jobhistory-daemon.sh start historyserver'

restart-cluster:
	make stop-cluster
	make start-cluster


sync-krb5:
	scp krb/kadm5.acl rhd01-6:/var/kerberos/krb5kdc/kadm5.acl
	scp krb/kdc.conf rhd01-6:/var/kerberos/krb5kdc/kdc.conf
	for i in 1 2 3 4 5 6; do scp krb/krb5.conf rhd01-$$i:/etc/; done
