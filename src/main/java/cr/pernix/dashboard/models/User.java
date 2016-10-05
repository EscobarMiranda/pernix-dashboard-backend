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
@Table(name = "User", schema = "public")
@XmlRootElement
public class User implements java.io.Serializable {

    private static final long serialVersionUID = 7796949849496424702L;
    private int id;
    private String email;
    private String lastname;
    private String name;
    private boolean active;
    private UserType usertype;
    private Manager manager;
    private Set<Answer> costumerSatisfactions = new HashSet<Answer>(0);

    public User() {
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
    
    @Column (name = "active")
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active){
        this.active = active;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id", nullable = false)
    public UserType getUserType() {
        return this.usertype;
    }

    public void setUserType(UserType userType) {
        this.usertype = userType;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = true)
    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<Answer> CostumerSatisfactions() {
        return this.costumerSatisfactions;
    }

    public void setCostumerSatisfactions(Set<Answer> costumerSatisfactions) {
        this.costumerSatisfactions = costumerSatisfactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        if (id != user.id)
            return false;
        return true;
    }
}
