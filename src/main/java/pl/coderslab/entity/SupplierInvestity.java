package pl.coderslab.entity;

import javax.persistence.*;

@Entity
public class SupplierInvestity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="suplier_id")
    private Supplier suplier;

    @ManyToOne
    @JoinColumn(name="ivestity_id")
    private Investity investity;

    private int cost;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Supplier getSuplier() {
        return suplier;
    }

    public void setSuplier(Supplier suplier) {
        this.suplier = suplier;
    }

    public Investity getInvestity() {
        return investity;
    }

    public void setInvestity(Investity investity) {
        this.investity = investity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SuplierInvestity{" +
                "id=" + id +
                ", suplier=" + suplier.getName() +
                ", investity=" + investity.getInvestityName() +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                '}';
    }
}
