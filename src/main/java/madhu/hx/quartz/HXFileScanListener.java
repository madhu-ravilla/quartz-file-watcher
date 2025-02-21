package madhu.hx.quartz;

import org.quartz.jobs.FileScanListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HXFileScanListener implements FileScanListener {

	public static final String LISTENER_NAME = "hxFileScanListenerName";

	public void fileUpdated(String fileName) {
		log.info("File update to {}", fileName);
	}

	public String getName() {
		return LISTENER_NAME;
	}

}
