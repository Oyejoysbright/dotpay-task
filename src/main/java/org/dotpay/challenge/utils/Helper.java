package org.dotpay.challenge.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {
    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	static String pattern = "yyyy-MM-dd hh:mm:ss";
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
	public static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Parse string specified to date.
	 * It uses date format specified in this class (yyyy-MM-dd hh:mm:ss)
	 * 
	 * @param date
	 * @return - Date instance
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		if ((date == null) || (date.isBlank())) {
			String today = dateTimeFormatter.format(LocalDateTime.now());
			return dateFormatter.parse(today);
		}
		else {
			try {
				return dateFormatter.parse(date);
			} catch(ParseException e ) {
				return Timestamp.valueOf(LocalDateTime.parse(date, dateTimeFormatter));
			}
		}
	}

	public static Date getPreviousDate(String dateString) throws ParseException {
        LocalDateTime localDate = LocalDate.parse(dateString, dateTimeFormatter).minusDays(1).atStartOfDay();
		Date date = Timestamp.valueOf(localDate);
		String formatted = dateFormatter.format(date);
		return dateFormatter.parse(formatted);
	}

	public static Date getYesterdayDate() throws ParseException {
        LocalDateTime localDate = LocalDate.now().minusDays(1).atStartOfDay();
		Date date = Timestamp.valueOf(localDate);
		String formatted = dateFormatter.format(date);
		return dateFormatter.parse(formatted);
	}

	public static Date getTodayDate() throws ParseException {
        LocalDateTime localDate = LocalDate.now().atStartOfDay();
		Date date = Timestamp.valueOf(localDate);
		String formatted = dateFormatter.format(date);
		return dateFormatter.parse(formatted);
	}

	/**
	 * Check if parameters specified are all null.
	 * Return true if they are.
	 * 
	 * @param objects
	 * @return - Boolean
	 */
	public static boolean areNull(Object...objects) {
		boolean res = true;
		for (int i = 0; i < objects.length; i++) {
			if(objects[i] != null) {
				return res = false;
			}
		}
		return res;
	}

	/**
	 * Generate random alphanumeric string.
	 * 
	 * @param length - Length of the string
	 * @return - String
	 */
	public static String randomString(int length) {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwxyz";
		String res = "";
		for (int i = 0; i < length; i++) {
			int pos = (int) Math.floor(Math.random() * chars.length());
			res += chars.substring(pos, pos + 1);
		}
		return res;
	}

	/**
	 * Log parameter in json format
	 * 
	 * @param object
	 */
	public static void logInfo(Object object) {
		try {
			log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
