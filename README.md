vertx-raspberry-console
=======================

Raspberry Pi web console showing top processes, cpu and memory usage. Written in Vert.x

Installation
============
Install latests Java 8 build from [Oracle website](http://jdk8.java.net/fxarmpreview).
<br/>
Install gradle and vert.x via GVM 
```
curl -s get.gvmtool.net | bash
gvm install gradle
gvm install vertx
```

Running
=======
```
gradle runVertx
```

or 

```
gradle build
./run.sh
```

Now go to `http://localhost:8080`
