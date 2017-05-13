package zesp03.webapp.service;

public interface AdminMailService {
    boolean send(String targetMail, String subject, String htmlBody);
}
