package pl.coderslab.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "working_time")
public class WorkingTime {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn (name = "id_investity")
    @NotNull
    private Investity investity;

    @NotNull
    private int numberOfHours;

    private LocalDate localDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Investity getInvestity() {
        return investity;
    }

    public void setInvestity(Investity investity) {
        this.investity = investity;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}