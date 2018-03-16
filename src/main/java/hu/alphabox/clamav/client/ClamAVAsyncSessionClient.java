package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.alphabox.clamav.client.command.SessionCommand;

public class ClamAVAsyncSessionClient extends AbstractClamavSessionClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVAsyncSessionClient.class);

	private Map<Integer, CompletableFuture<String>> resultMap;

	private int counter;

	public ClamAVAsyncSessionClient(String host, int port) throws IOException {
		super(InetAddress.getByName(host), port);

		this.counter = 0;
		this.resultMap = new HashMap<>();
		startInputReader();
	}

	public synchronized Future<String> sendCommandWithResponse(SessionCommand command) throws IOException {
		CompletableFuture<String> result = new CompletableFuture<>();
		resultMap.put(++counter, result);
		sendCommandWithoutResponse(command);
		return result;
	}

	private void startInputReader() {
		new Thread(() -> {
			StringBuilder builder = new StringBuilder();
			InputStream inputStream = getInputStream();
			try {
				while (!getSocket().isClosed()) {
					int opByte = -1;
					do {
						opByte = inputStream.read();
						builder.append((char) opByte);
					} while (opByte != -1 && opByte != ClamAVSeparator.NULL.getSeparator());
					int index = builder.toString().indexOf(':');
					if (index != -1) {
						CompletableFuture<String> result = resultMap
								.remove(Integer.parseInt(builder.toString().substring(0, index)));
						result.complete(builder.toString());
						builder.setLength(0);
					} else {
						LOGGER.warn("Undefined response from ClamAV: {}", builder);
					}
				}
			} catch (SocketException e) {
				if( getSocket().isClosed()) {
					LOGGER.debug("Socket is closed for {}:{}", getSocket().getInetAddress(), getSocket().getPort());
				}
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}).start();
	}

}
