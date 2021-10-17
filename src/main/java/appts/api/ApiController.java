package appts.api;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	private static final String TIME_FORMAT = "^([01][0-9]|2[0-3]):([03]0)$";
	private static final String DATE_FORMAT = "^(\\d{4})-(\\d{2})-(\\d{2})$";
	private final Map<String, Map<String, Appointment>> apptBook = new HashMap<>();

	@GetMapping("/appts/{userid}")
	public List<Appointment> getAppointments(@PathVariable String userid) {
		List<Appointment> appts = new ArrayList<>();
		if (!apptBook.containsKey(userid))
			return appts;
		System.out.println(apptBook.get(userid).toString());
		for (Appointment appt : apptBook.get(userid).values())
			appts.add(appt);

		return appts;
	}

	private LocalDate checkDate(String dateString) {
		Pattern p = Pattern.compile(DATE_FORMAT);
		Matcher m = p.matcher(dateString);
		if (!m.matches())
			return null;

		int year = Integer.parseInt(m.group(1));
		int month = Integer.parseInt(m.group(2));
		int day = Integer.parseInt(m.group(3));
		LocalDate date;
		try {
			date = LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			return null;
		}

		return date;
	}

	private LocalTime checkTime(String timeString) {
		Pattern p = Pattern.compile(TIME_FORMAT);
		Matcher m = p.matcher(timeString);
		if (!m.matches())
			return null;

		int hour = Integer.parseInt(m.group(1));
		int minute = Integer.parseInt(m.group(2));
		LocalTime time;
		try {
			time = LocalTime.of(hour, minute);
		} catch (DateTimeException e) {
			return null;
		}

		return time;
	}

	private boolean isPresentOrFutureDateTime(LocalDate date, LocalTime time) {
		LocalDateTime dt = date.atTime(time);
		LocalDateTime now = LocalDateTime.now();

		return dt.compareTo(now) >= 0;
	}

	@PostMapping("/appts")
	public ResponseEntity<String> addAppointment(@RequestBody Appointment appt) {
		LocalDate date = checkDate(appt.getDate());
		if (date == null)
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
					"Invalid date format (" + appt.getDate() + "). Please enter a valid date in YYYY-MM-DD format.");

		LocalTime time = checkTime(appt.getTime());
		if (time == null)
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body("Invalid time format (" + appt.getTime() + "). Please enter time in HH:MM format."
							+ " Times can only be scheduled on the hour or half hour (e.g. HH:00 or HH:30).");

		if (!isPresentOrFutureDateTime(date, time))
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Appointments cannot be scheduled in the past.");

		Map<String, Appointment> userAppts = apptBook.getOrDefault(appt.getUserid(),
				new HashMap<String, Appointment>());

		if (userAppts.containsKey(appt.getDate())) {
			Appointment userAppt = userAppts.get(appt.getDate());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User " + userAppt.getUserid()
					+ " already has an appointment scheduled on " + userAppt.getDate() + " at " + userAppt.getTime());
		}

		userAppts.put(appt.getDate(), appt);
		if (!apptBook.containsKey(appt.getUserid()))
			apptBook.put(appt.getUserid(), userAppts);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
