package pl.coderslab.entity;


import javax.persistence.*;

@Entity
public class Investity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String investityName;

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
}
