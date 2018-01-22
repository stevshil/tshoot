FROM mysql:5.6
MAINTAINER Steve Shilling steve@therapypages.com
EXPOSE 22
RUN apt-get -y update
RUN apt-get -y install openssh-server
RUN ssh-keygen -A
RUN useradd student
RUN (sleep 2; echo secret; sleep 2; echo secret) | passwd student
COPY sshd_config /etc/ssh/sshd_config
COPY trades_create_mysql.sql /tmp/trades_create_mysql.mysql
COPY sqlschema.sh /tmp/sqlschema.sh
RUN chmod +x /tmp/sqlschema.sh
RUN mysqld
RUN /tmp/sqlschema.sh
WORKDIR /opt/trade-app
ENTRYPOINT ["mysqld"]
