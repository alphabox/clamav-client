# clamav-client
## Introduction
clamav-client is a simple, solid library in Java to communicate with ClamaAV daemon.
You can send any message type (typically email) for ClamAV to check for viruses.

The library implements all commands, except which only works on UNIX domain sockets.
You can find more information on [clamd manual](https://linux.die.net/man/8/clamd).

## Maven dependency
```xml
<dependency>
    <groupId>hu.alphabox</groupId>
    <artifactId>clamav-client</artifactId>
    <version>0.1</version>
</dependency>
```

## Building
To make the clamav-client-0.1-SNAPSHOT.jar, you will need the Apache Maven installed.
Run the following command:
```
mvn clean package
```

## Simple Usage
A simple usage example to send EICAR message with the simplest implementation.
```java
public static void main(String[] args) throws Exception {
    ClamAVClient client = new ClamAVClient("localhost", 3310);
    //EICAR message string
    String message = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";
    String reply = client.sendCommand(new InStreamCommand(message));
    LOGGER.info("Reply of the message: {}", reply);
}
```

## Async usage
Send messages in async mode with only one connection (Session mode).
You get the results as a Future<String>.
```java
public static void main(String[] args) throws Exception {
	List<Future<String>> result = new ArrayList<>();
	try (ClamAVAsyncSessionClient client = new ClamAVAsyncSessionClient("192.168.1.105", 3310)) {
		String message = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";		
		for(int i = 0; i < 1000; i++) {
			result.add(client.sendCommand(new InStreamCommand(message)));
		}
		//Collections.shuffle(result);
		for(Future<String> future : result) {
			LOGGER.info(future.get());
		}
	}
}
```

## License
This project licensed under Apache 2.0 License - see the [LICENSE](LICENSE) file for details