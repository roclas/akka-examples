FROM ubuntu:precise
MAINTAINER C Hernandez (Stratio) <chernandez@stratio.com>

# Install dependencies
RUN apt-get -y update
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y --force-yes software-properties-common python-software-properties
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get -y update
RUN /bin/echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install oracle-java7-installer oracle-java7-set-default
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y wget dnsmasq-base python2.7 vim less iputils-ping git jsvc maven fakeroot

# Install software
ADD start_server.sh / 
RUN chmod +x start_server.sh 
RUN "echo" "\n\n\n installing cluster server \n\n\n"
RUN mkdir -p /home/projects/
RUN cd /home/projects/ && git clone https://github.com/roclas/akka-examples.git && ls /home/projects
RUN cd /home/projects/akka-examples && git checkout develop && git pull && cd load-balancing 

# scala instalation
RUN apt-get remove scala-library scala
RUN wget http://www.scala-lang.org/files/archive/scala-2.11.4.deb
RUN dpkg -i scala-2.11.4.deb
RUN apt-get update
RUN apt-get install scala
 
# sbt installation
# remove sbt:>  sudo apt-get purge sbt.
RUN wget http://dl.bintray.com/sbt/debian/sbt-0.13.6.deb
RUN dpkg -i sbt-0.13.6.deb 
RUN apt-get update
RUN apt-get install sbt

#
CMD ["/start_server.sh"]

#EXPOSE 9160 9042
