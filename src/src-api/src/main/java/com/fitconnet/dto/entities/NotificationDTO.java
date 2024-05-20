package com.fitconnet.dto.entities;

import java.util.Date;

import com.fitconnet.persitence.model.User;

import lombok.Data;

@Data
public class NotificationDTO {
	/**
	 * The message content of the notification.
	 */
	private String message;

	/**
	 * The date when the notification was created.
	 */
	private Date date;

	/**
	 * The user who receives the notification.
	 */
	private User receiver;
}
