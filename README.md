###Overview
JRMI is a simple Java implementation of [RMI(Remote Method Invocation)](http://en.wikipedia.org/wiki/RMI). The main idea is same as the implementation of [Oracle's Java RMI](http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136424.html). The structure of the whole project is as below:

* `jrmi-common` - The RMI library, which contains all RMI interfaces
* `jrmi-compiler` - The RMI compiler, which is used to generate stub files for communication.
* `jrmi-server` - The implementation of RMI Server and RMI Registry Server.
* `jrmi-examples` - An example which uses our RMI library.

###How to build
Our project is managed by [Apache Maven](http://maven.apache.org/). In order to build our project, you should install maven first. After successfully installing maven, you can build our project as below:

<pre>
$ mvn package
</pre>

###How to run
First you should start the RMI Registry Server, the command is:

<pre>
$ dist/bin/jrmi-server --as-registry-server --registry-port rPORT
</pre>

The option `--as-registry-server` tells RMI server it should be started as a registry server. Also, you can specify the registry server's port by using the option `--registry-port`. If the `--as-registry-server` is not set, the RMI server will be started as a normal server, which will handle the request of remote calls. Other options for `jrmi-server` are listed as below:

<pre>
$ bin/dist/jrmi-server -h
Usage: jrmi-server [options]
  Options:
    -R, --as-registry-server
       start up as a registry server
       Default: false
    -h, --help
    -p, --port
       the listening port of remote server
       Default: 15440
    -rh, --registry-host
       the host of registry server
    -rp, --registry-port
       the listening port of registry server
       Default: 15640
    -r, --remote
       the remote object name and service name, use format 
       className@serviceName
       Default: []
    -t, --threads
       the size of the thread pool
       Default: 32
</pre>

Next, you should start a normal RMI server to handle request of remote class. The RMI server needs to know which class should be binded to which service name on the registry server. So you should set the map between class name and service name by using the option `-r` or `--remote`, the format of this option's value is `ClassName@ServiceName`. Hence you can start a normal RMI server like this:

<pre>
$ dist/bin/jrmi-server -r com.example.HelloWorldImpl@helloworld
</pre>

The command above bind the class `com.example.HelloWorldImpl` to service name `helloworld`. If you would like to use our example, you can start a server for calculating the value of [PI](http://en.wikipedia.org/wiki/Pi):

<pre>
$ dist/bin/calculatepi-server -rh rHOST -rp rPORT -p PORT
</pre>

After the RMI server is started, you can execute a remote call from other machines by using our library. The `jrmi-examples/calculatepi` will give you more information about how to write code of RMI. We also provide a client example for you. Now you can start our client example:

<pre>
$ dist/bin/calculatepi-client -rh rHOST -rp rPORT
</pre>

After a couple of seconds, you will get the value of PI.

###How to generate stub files

We offered `jrmi-compiler` to generate the stub files for remote interface. Here's the options of `jrmi-compiler`:

<pre>
$ dist/bin/jrmi-compiler -h
Usage: jrmi-compiler [options] package-qualified-class-name(s)
  Options:
    -cp, --classpath
       the path jrmi-compiler uses to look up classes.
       Default: []
    -d, --directory
       the output directory of the stub file(s)
       Default: .
    -h, --help
</pre>

Here's a example of how to use the `jrmi-compiler`:

<pre>
$ dist/bin/jrmi-compiler -cp dist/bin/calculatepi-server-1.0.jar -cp dist/bin/calculatepi-common-1.0.jar -d /tmp CalculatePIImpl
</pre>

We first tell `jrmi-compiler` where to find the definition of the remote interface and the implementation of the remote interface, then we set the output directory of the stub file to `/tmp`. Now `jrmi-compiler` will generate a stub file `/tmp/CalculatePIImpl_Stub.java` for `CalculatePIImpl`.

Now you are on board! Cheers!




