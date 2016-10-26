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
@Table(name = "OnTrack", schema = "public")
@XmlRootElement
public class OnTrack implements java.io.Serializable {
    
    private static final long serialVersionUID = -2299646453001250553L;
    private int id;
    private String name;
    private Set<Project> projects = new HashSet<Project>(0);

    public OnTrack() {
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
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "onTrack")
    public Set<Project> projects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnTrack onTrack = (OnTrack) o;
        if (id != onTrack.id)
            return false;
        return true;
    }

}
