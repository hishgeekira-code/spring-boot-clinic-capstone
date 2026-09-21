package mn.icode.dto;

public class DashboardStatsDto {
	private long totalDoctors;
	private long totalPatients;
	private long todayAppointments;
	private long bookedAppointments;
	private long completedAppointments;
	private long cancelledAppointments;
	
	public DashboardStatsDto(long totalDoctors, long totalPatients, long todayAppointments, long bookedAppointments,
			long completedAppointments, long cancelledAppointments) {
		this.totalDoctors = totalDoctors;
		this.totalPatients = totalPatients;
		this.todayAppointments = todayAppointments;
		this.bookedAppointments = bookedAppointments;
		this.completedAppointments = completedAppointments;
		this.cancelledAppointments = cancelledAppointments;
	}

	public long getTotalDoctors() {
		return totalDoctors;
	}

	public void setTotalDoctors(long totalDoctors) {
		this.totalDoctors = totalDoctors;
	}

	public long getTotalPatients() {
		return totalPatients;
	}

	public void setTotalPatients(long totalPatients) {
		this.totalPatients = totalPatients;
	}

	public long getTodayAppointments() {
		return todayAppointments;
	}

	public void setTodayAppointments(long todayAppointments) {
		this.todayAppointments = todayAppointments;
	}

	public long getBookedAppointments() {
		return bookedAppointments;
	}

	public void setBookedAppointments(long bookedAppointments) {
		this.bookedAppointments = bookedAppointments;
	}

	public long getCompletedAppointments() {
		return completedAppointments;
	}

	public void setCompletedAppointments(long completedAppointments) {
		this.completedAppointments = completedAppointments;
	}

	public long getCancelledAppointments() {
		return cancelledAppointments;
	}

	public void setCancelledAppointments(long cancelledAppointments) {
		this.cancelledAppointments = cancelledAppointments;
	}
}
