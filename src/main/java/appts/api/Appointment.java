package appts.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Appointment {
	@Getter
	private final String userid;
	@Getter
	private final String date;
	@Getter
	private final String time;
	@Getter
	private final int duration = 30;
}
