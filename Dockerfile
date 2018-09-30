
FROM ubuntu:16.04
MAINTAINER James Alastair McLaughlin <j.a.mclaughlin@ncl.ac.uk>

RUN apt update && apt dist-upgrade -y

RUN apt install -y default-jdk maven

RUN useradd biocad -p biocad -m -s /bin/bash

ADD . /opt/ConversionProxy

RUN cd /opt/ConversionProxy && mvn install
RUN chown -R biocad:biocad /opt/ConversionProxy

COPY startup.sh /
RUN chmod 777 /startup.sh

EXPOSE 9995

ENTRYPOINT ["/startup.sh"]



