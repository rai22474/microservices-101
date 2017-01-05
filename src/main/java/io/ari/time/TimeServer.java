package io.ari.time;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeServer {

	public Date currentDate() {
		return new Date();
	}
}
