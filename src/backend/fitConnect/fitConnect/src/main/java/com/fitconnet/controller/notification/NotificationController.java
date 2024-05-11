package com.fitconnet.controller.notification;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitconnet.error.GlobalExceptionHandler;
import com.fitconnet.persitence.model.Notification;
import com.fitconnet.persitence.model.User;
import com.fitconnet.service.interfaces.entity.NotificationServiceI;
import com.fitconnet.service.interfaces.entity.ProcessingResponseI;
import com.fitconnet.service.interfaces.entity.UserServiceI;
import com.fitconnet.utils.Constants;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class NotificationController {

	@Qualifier("notificationService")
	private final NotificationServiceI notificationService;
	@Qualifier("userService")
	private final UserServiceI userService;
	@Qualifier("processingResponseI")
	private final ProcessingResponseI processingResponseI;
	@Qualifier("globalExceptionHandler")
	private final GlobalExceptionHandler globalExceptionHandler;

	private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> createNotification(@RequestBody Notification notification) {
		logger.info("NotificationController :: createNotification");
		ResponseEntity<String> response = null;
		Boolean exist = notificationService.existByDate(notification.getDate());
		response = processingResponseI.processStringResponse(exist,
				() -> ResponseEntity.status(HttpStatus.CONFLICT).body("La notificación ya existe"), () -> {
					Notification newNotification = new Notification();
					notificationService.setAttributes(notification, newNotification);
					notificationService.create(newNotification);
					return ResponseEntity.ok().body("Notificación creada correctamente.");
				});
		return response;
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Optional<Set<Notification>>> getNotifications() {
		logger.info("## NotificationController :: getNotifications");
		Optional<Set<Notification>> notifications = notificationService.getAll();
		return ResponseEntity.ok().body(notifications);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
	public ResponseEntity<Optional<Set<Notification>>> getNotificationsByUserId(@PathVariable Long id) {
		logger.info("NotificationController :: getNotificationsByUserId");
		ResponseEntity<Optional<Set<Notification>>> response = null;
		User user = userService.getById(id);
		Optional<User> existingUser = Optional.of(user);
		response = processingResponseI.processOptionalResponse(existingUser,
				() -> ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.NOTIFICATION_NOT_FOUND),
				() -> ResponseEntity.ok().body(notificationService.getByRecipient(existingUser)));
		return response;
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> patchNotification(@PathVariable Long id, @RequestBody Notification notification) {
		logger.info("NotificationController :: patchNotification");
		ResponseEntity<String> response = null;
		Boolean exist = notificationService.existById(id);
		response = processingResponseI.processStringResponse(exist,
				() -> ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.NOTIFICATION_NOT_FOUND), () -> {
					notificationService.patch(id, notification);
					return ResponseEntity.ok().body("Usuario actualizado");
				});
		return response;
	}

	@DeleteMapping("/notification/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
		logger.info("ActivityController :: deleteNotification");
		ResponseEntity<String> response = null;
		Boolean exist = notificationService.existById(id);
		response = processingResponseI.processStringResponse(exist,
				() -> ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.NOTIFICATION_NOT_FOUND), () -> {
					notificationService.delete(id);
					return ResponseEntity.ok().body("Notificacion ha eliminado correctamente");
				});
		return response;
	}

}
