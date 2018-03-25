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

public class ClamAVAsyncSessionClient extends AbstractClamavSessionClient<Future<String>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVAsyncSessionClient.class);

	private Map<Integer, CompletableFuture<String>> resultMap;
	
	private Thread thread;

	private boolean isStop;
	
	private int counter;

	public ClamAVAsyncSessionClient(String host, int port) throws IOException {
		super(InetAddress.getByName(host), port);
		this.isStop = false;
		this.counter = 0;
		this.resultMap = new HashMap<>();
		startInputReader();
	}

	@Override
	public synchronized Future<String> sendCommand(SessionCommand command) throws IOException {
		CompletableFuture<String> result = new CompletableFuture<>();
		resultMap.put(++counter, result);
		sendCommandWithoutResponse(command);
		return result;
	}
	
	public int getCommandCounter() {
		return counter;
	}

	private void startInputReader() {
		thread = new Thread(() -> {
			StringBuilder builder = new StringBuilder();
			InputStream inputStream = getInputStream();
			
			try {
				int opByte = -1;
				int available = 0;
				while(!isStop) {
					if(( available = inputStream.available()) != 0) {
						while (available-- != 0) {
							opByte = inputStream.read();
							if ( opByte != ClamAVSeparator.NULL.getSeparator()) {
								builder.append((char) opByte);
							} else if( opByte == -1 ) {
								break;
							} else {
								int index = builder.indexOf(":");
								if (index != -1) {
									int resultIndex = Integer.parseInt(builder.substring(0, index));
									CompletableFuture<String> result = resultMap.remove(resultIndex);
									result.complete(builder.toString());
									builder.setLength(0);						
								} else {
									LOGGER.warn("Undefined response from ClamAV: {}", builder.toString().getBytes());
								}
							}
						}
					}
					Thread.sleep(10);
				}
				LOGGER.debug("The input reader closed silently.");
			} catch (SocketException e) {
				if (getSocket().isClosed()) {
					LOGGER.debug("Socket is closed for {}:{}", getSocket().getInetAddress(), getSocket().getPort());
				} else {
					LOGGER.warn(e.getMessage(), e);
				}
			} catch (IOException | InterruptedException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		});
		thread.start();
	}

	@Override
	public void close() throws Exception {
		while (resultMap.size() != 0) {
			Thread.sleep(100);
		}
		isStop = true;
		thread.join();
		super.close();
	}

}
