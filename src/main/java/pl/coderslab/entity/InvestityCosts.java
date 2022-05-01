package pl.coderslab.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
public class InvestityCosts {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name="ivestity_id")
    private Investity investity;

    @Min(value = 0, message = "Koszt nie może być mniejszy od 0!")
    private int cost;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier p) {
        this.supplier = p;
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
                ", suplier=" + supplier.getName() +
                ", investity=" + investity.getInvestityName() +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                '}';
    }
}
