package pl.coderslab.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Investity investity;

    @NotNull
    @Min(value = 0, message = "Wartość nie może być mniejsza od 0!")
    @Max(value = 24, message = "Wartość nie może być większa od 24!")
    private int numberOfHours;

    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @Min(value = 0, message = "Wartość nie może być mniejsza od 0!")
    private int salaryPerHours;
    @Min(value = 1, message = "Mnożnik nie może być mniejszy od 1")
    @Max(value = 5, message = "Mnożnik nie może być większy od 5")
    private double multiplier;

    private String description;

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

    public int getSalaryPerHours() {
        return salaryPerHours;
    }

    public void setSalaryPerHours(int salaryPerHours) {
        this.salaryPerHours = salaryPerHours;
    }

    public double getMultiplier() {
        return multiplier;
    }
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WorkingTime{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", investity=" + investity.getInvestityName() +
                ", numberOfHours=" + numberOfHours +
                ", localDate=" + localDate +
                ", salaryPerHours=" + salaryPerHours +
                ", multiplier=" + multiplier +
                '}';
    }
}
