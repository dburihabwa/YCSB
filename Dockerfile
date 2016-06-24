FROM vyolin/alpine-maven:latest
RUN apk update && apk add git python && git clone https://github.com/dburihabwa/YCSB YCSB
WORKDIR YCSB
RUN mvn -pl com.yahoo.ycsb:playcloud-binding -am clean package
