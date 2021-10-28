package domain;

import java.time.LocalDateTime;

public abstract class SportEvent {
    private String title;
    private LocalDateTime startDate;

    public SportEvent() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

}
