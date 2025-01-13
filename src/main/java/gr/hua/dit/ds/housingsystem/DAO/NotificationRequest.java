package gr.hua.dit.ds.housingsystem.DAO;

import javax.validation.constraints.NotBlank;


public class NotificationRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
