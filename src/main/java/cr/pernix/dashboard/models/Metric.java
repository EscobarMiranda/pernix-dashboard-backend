package cr.pernix.dashboard.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Metric", schema = "public")
@XmlRootElement
public class Metric implements java.io.Serializable {

    private int id;
    private String name;
    private String description;
    private boolean active;
    private Set<CustomerSatisfaction> customerSatisfaction = new HashSet<CustomerSatisfaction>(0);
    
    public Metric() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "active")

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerSatisfaction")
    public Set<CustomerSatisfaction> customerSatisfaction() {
        return this.customerSatisfaction;
    }

    public void setUsers(Set<CustomerSatisfaction> customerSatisfaction) {
        this.customerSatisfaction = customerSatisfaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Metric metric = (Metric) o;
        if (id != metric.id)
            return false;
        return true;
    }

}
