FROM centos:7.2.1511
MAINTAINER Steve Shilling steve@therapypages.com
RUN yum -y update
# Install useful tools
RUN yum -y install wget openssh-server net-tools nmap telnet mysql file sudo nano
# Install Oracle Java for the app
RUN wget -nv 'https://www.dropbox.com/s/lqqp8zjc1ibmk8e/jdk-8u131-linux-x64.rpm?dl=0' -O /tmp/jdk-8u131-linux-x64.rpm
RUN wget 'https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.42.tar.gz' -O /tmp/mysql-connector-java-5.1.42.tar.gz
RUN yum -y install /tmp/jdk-8u131-linux-x64.rpm
# Put the trade app on the system
COPY tradeapp.tgz /tmp/tradeapp.tgz
COPY start.sh /opt
RUN chmod +x /opt/start.sh
RUN cd /opt && mkdir trade-app && cd trade-app && tar zxvf /tmp/tradeapp.tgz
COPY application-injector.properties /opt/trade-app/target/application-injector.properties
COPY application.properties /opt/trade-app/target/application.properties
COPY application.properties /opt/trade-app/target/application-prod.properties
COPY runwebapp.sh /opt/trade-app/bin/runwebapp.sh
RUN cd /opt/trade-app && tar xvf /tmp/mysql-connector-java-5.1.42.tar.gz && mv mysql-connector-java-5.1.42/mysql-connector* ./target
EXPOSE 8080
EXPOSE 80
EXPOSE 22
VOLUME ["/var/testresults"]
ENV APPVERSION 0.1.0
RUN rm -f /tmp/mysql* /tmp/jdk*
RUN ssh-keygen -A
RUN useradd -m -s /bin/bash student
RUN mkdir /var/.user && chown student:student /var/.user
RUN (sleep 2; echo secret; sleep 2; echo secret) | passwd student
COPY sshd_config /etc/ssh/sshd_config
COPY bashrc /home/student/.bashrc
COPY completed /bin/completed
RUN chown root:root /bin/completed && chmod 700 /bin/completed
RUN chown student:student /home/student/.bashrc
COPY student_sudoers /etc/sudoers.d/student
RUN chown root:root /etc/sudoers.d/student && chmod 600 /etc/sudoers.d/student
WORKDIR /opt/trade-app
ENTRYPOINT ["/opt/start.sh"]
