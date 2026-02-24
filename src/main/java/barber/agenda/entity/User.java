package barber.agenda.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tab_user") // O nome tab_user é ótimo para evitar conflitos com a palavra reservada 'user' do Postgres
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Alterado para Long para manter consistência com Cliente/Barbeiro


    @Column(length = 20, nullable = false, unique = true) // Adicionado unique para evitar usernames duplicados
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tab_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name") // Alterado para role_name, já que salvamos Strings como "ADMIN" ou "USER"
    private List<String> roles = new ArrayList<>();

    // Construtores
    public User() {}

    public User(String name, String username, String password) {

        this.username = username;
        this.password = password;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }


}