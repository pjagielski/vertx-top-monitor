vertx-raspberry-console
=======================

Raspberry Pi web console showing top processes, cpu and memory usage. Written in Vert.x

Installation
============
Install latests Java 8 build from [Oracle website](http://jdk8.java.net/fxarmpreview).
<br/>
Install vert.x via GVM 
```
curl -s get.gvmtool.net | bash
gvm install vertx
```

Running
=======
```
gradle build
./run.sh
```

Now go to `http://localhost:8080`
