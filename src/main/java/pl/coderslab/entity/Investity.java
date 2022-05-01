package pl.coderslab.entity;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Investity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Musisz wpisać nazwę Inwestycji!")
    @Column(unique = true)
    private String investityName;
    @Min(value = 0, message = "budżet nie może być mniejszy od 0!")
    private int budget;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvestityName() {
        return investityName;
    }

    public void setInvestityName(String investityName) {
        this.investityName = investityName;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Investity{" +
                "id=" + id +
                ", investityName='" + investityName + '\'' +
                ", budget=" + budget +
                '}';
    }
}
