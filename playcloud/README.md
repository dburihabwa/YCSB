# Playcloud driver for YCSB

This module is a YCSB binding for the playcloud project.

## Quick start

### 1. Start playcloud
### 2. Install Java and Maven (version +3)
### 3. Compile YCSB and playcloud binding

At the root of the YCSB repository, run:
```bash
mvn -pl com.yahoo.ycsb:playcloud-binding -am clean package
```

### 4. Load data

From the root of the YCSB project, run:
```bash
bin/ycsb load playcloud -s -P workloads/workloada -p "playcloud.host=127.0.0.1" -p "playcloud.port=3000"
```

See [Runtime parameters] for the playcloud options

## Runtime parameters

The binding expects two parameters to run properly:

 * playcloud.host: The host where the playcloud proxy is running
 * playcloud.port: The port number on which the playcloud proxy is listening
