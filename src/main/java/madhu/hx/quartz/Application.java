package madhu.hx.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.FileScanJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

	public static void main(String[] args) {
		
//		java -jar quartz-file-watcher-1.0.0-jar-with-dependencies.jar C:/temp/log_test1.txt
//		javaw -jar quartz-file-watcher-1.0.0-jar-with-dependencies.jar C:/temp/log_test1.txt
		if (args == null || args.length < 1 || args[0] == null || args[0].isEmpty()) {
			log.error("You must provide a filename to this program.");
			return;
		}

		String fileName = args[0];
		//String fileName = "C:/temp/log_test1.txt";

		log.info("Scheduler started monitoring filename: {}", fileName);
		fileScan(fileName);
	}

	private static void fileScan(String filename) {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("hxFileScanListenerName", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();

		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();

			JobKey jobKey = new JobKey("hxFileScanJobName", "group1");
			JobDetail job = JobBuilder.newJob(FileScanJob.class).withIdentity(jobKey).build();
			job.getJobDataMap().put(FileScanJob.FILE_NAME, filename);
			job.getJobDataMap().put(FileScanJob.FILE_SCAN_LISTENER_NAME, HXFileScanListener.LISTENER_NAME);
			scheduler.getContext().put(HXFileScanListener.LISTENER_NAME, new HXFileScanListener());
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}

}
