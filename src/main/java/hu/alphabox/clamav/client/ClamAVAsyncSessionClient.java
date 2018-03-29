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

/**
 * <p>
 * It is an asyncron session client for ClamAV.
 * You can send {@code SessionCommand} commands,and you
 * will get the result as the ClamAV daemon responds with the right id.
 * </p>
 * <p>
 * It is only use one connection for the whole session.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 */
public class ClamAVAsyncSessionClient extends AbstractClamavSessionClient<Future<String>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVAsyncSessionClient.class);

	/**
	 * Store the waiting futures which wait for response.
	 */
	private Map<Integer, CompletableFuture<String>> resultMap;
	
	/**
	 * Store the reading thread.
	 */
	private Thread thread;

	/**
	 * If it is true, than the reading thread will stop.
	 */
	private boolean isStop;
	
	/**
	 * A counter which store the biggest id number.
	 */
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
	
	/**
	 * Returns the last id.
	 * @return the last id
	 */
	public int getCommandCounter() {
		return counter;
	}

	/**
	 * Create a new thread for {@code InputStream}, and it will run while we wait
	 * for response or do not close the connection.
	 */
	private void startInputReader() {
		thread = new Thread(() -> {
			StringBuilder builder = new StringBuilder();
			InputStream inputStream = getInputStream();
			
			try {
				int opByte = -1;
				int available = 0;
				while(!isStop) {
					if(( available = inputStream.available()) != 0) {
						while (available-- != 0 && (opByte = inputStream.read()) != -1) {
							if ( opByte != ClamAVSeparator.NULL.getSeparator()) {
								builder.append((char) opByte);
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
				LOGGER.debug("The input reader of {}:{} closed silently.", getSocket().getInetAddress(), getSocket().getPort());
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
