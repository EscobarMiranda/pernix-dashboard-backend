package cr.pernix.dashboard.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Manager", schema = "public")
@XmlRootElement
public class Manager implements java.io.Serializable {

    private int id;
    private String name;
    private String lastname;
    private String email;
    private Company company;
    private Set<User> users = new HashSet<User>(0);
    private Set<CustomerSatisfaction> costumerSatisfactions = new HashSet<CustomerSatisfaction>(0);

    public Manager() {
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

    @Column(name = "lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "manager")
    public Set<User> users() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "manager")
    public Set<CustomerSatisfaction> CostumerSatisfactions() {
        return this.costumerSatisfactions;
    }

    public void setCostumerSatisfactions(Set<CustomerSatisfaction> costumerSatisfactions) {
        this.costumerSatisfactions = costumerSatisfactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Manager manager = (Manager) o;
        if (id != manager.id)
            return false;
        return true;
    }

}
