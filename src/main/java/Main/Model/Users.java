package Main.Model;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Users class
 *
 * @author - GuiBatalhoti
 * @author - Gabriel Nozawa
 */
@Entity
@Table(name = "users")
public class Users implements UserDetails, Serializable {

    /**
     * Attributes
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "username", unique = true)
    private String username;
    @Basic(optional = false)
    @Column(name = "name", unique = true)
    private String name;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Collection<MangaList> mangaListCollection;

    /**
     * Constructor
     */
    public Users() {
    }

    /**
     * Getters and Setters
     */
    public Integer getIdUser() {
        return idUser;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Collection<MangaList> getMangaListCollection() {
        return mangaListCollection;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMangaListCollection(Collection<MangaList> mangaListCollection) {
        this.mangaListCollection = mangaListCollection;
    }

    /**
     * User Datails methods
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * HashCode and Equals methods
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }
}
