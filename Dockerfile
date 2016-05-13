FROM vyolin/alpine-maven:latest

WORKDIR /usr/local/src/ycsb
ADD . /usr/local/src/ycsb/
RUN mvn -pl com.yahoo.ycsb:playcloud-binding -am clean package
